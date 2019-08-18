package com.kodekonveyor.cdd.run.impl;

import static org.mockito.Mockito.mockingDetails;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.invocation.Invocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodekonveyor.cdd.ContractInfo;
import com.kodekonveyor.cdd.build.impl.ChildDescriptionServiceImpl;
import com.kodekonveyor.cdd.exception.StackTraceSetterService;
import com.kodekonveyor.cdd.run.ContractRunnerService;
import com.kodekonveyor.cdd.run.dto.ContractRunnerData;

import lombok.Setter;

@Service
public class ContractRunnerServiceImpl<ServiceType>
    implements ContractRunnerService<ServiceType> {

  @Autowired
  private ChildDescriptionServiceImpl<ServiceType> childDescriptionService;
  @Autowired
  @Setter
  private StackTraceSetterService stackTraceSetterService;

  @Override
  public void runChild(
      final ContractInfo<ServiceType> contract, final RunNotifier notifier,
      final ContractRunnerData<ServiceType> data
  ) {
    final Description description =
        this.childDescriptionService.describeChild(contract, data);
    data.getTestInstance();
    notifier.fireTestStarted(description);
    runChildWithResult(contract, notifier, description);
    notifier.fireTestFinished(description);
  }

  private void runChildWithResult(
      final ContractInfo<ServiceType> contract, final RunNotifier notifier,
      final Description description
  ) {
    if (contract.getExceptionClass() == null)
      testReturningContract(contract, notifier, description);
    else
      testThrowingContract(contract, notifier, description);
  }

  private void testThrowingContract(
      final ContractInfo<?> contract, final RunNotifier notifier,
      final Description description
  ) {
    final StubbedInvocationMatcher throwingStubbing =
        (StubbedInvocationMatcher) mockingDetails(contract.getStub()).getStubbings().iterator().next();
    final Invocation throwingInvocation = throwingStubbing.getInvocation();
    throwingStubbing.markStubUsed(throwingInvocation);

    try {
      throwingInvocation.callRealMethod();
      final AssertionError originalException = new AssertionError(
          "Expected " + contract.getExceptionClass().getSimpleName() +
              ", but no exception thrown"
      );
      final Throwable exception = this.stackTraceSetterService
          .changeStackWithMethod(
              originalException, throwingStubbing.getMethod()
          );
      notifier.fireTestFailure(
          new Failure(
              description, exception
          )
      );
    } catch (final Throwable thrown) { //NOPMD AvoidCatchingThrowable
      if (
        !(thrown.getClass().equals(contract.getExceptionClass()) &&
            thrown.getMessage().equals(contract.getExceptionMessage()))
      ) {
        final Throwable thrownException = this.stackTraceSetterService
            .changeStackWithMethod(
                new AssertionError(
                    "Expected " +
                        contract.getExceptionClass().getSimpleName() + "(" +
                        contract.getExceptionMessage() + "), but see cause",
                    thrown
                ), throwingStubbing.getMethod()
            );
        notifier.fireTestFailure(
            new Failure(
                description,
                thrownException
            )
        );
      }
    }
  }

  private void testReturningContract(
      final ContractInfo<ServiceType> contract, final RunNotifier notifier,
      final Description description
  ) {
    final StubbedInvocationMatcher stubbing =
        (StubbedInvocationMatcher) mockingDetails(contract.getStub()).getStubbings().iterator().next();
    final Invocation invocation = stubbing.getInvocation();
    final Object answer;
    try {
      stubbing.markStubUsed(invocation);
      answer = stubbing.answer(invocation);
    } catch (final Throwable thrown) { //NOPMD AvoidCatchingThrowable
      final Throwable exception = this.stackTraceSetterService
          .changeStackWithMethod(thrown, stubbing.getMethod());
      notifier.fireTestFailure(new Failure(description, exception));
      return;
    }

    runOneReturningContract(
        contract, notifier, description, invocation, answer
    );
  }

  private void runOneReturningContract(
      final ContractInfo<ServiceType> contract, final RunNotifier notifier,
      final Description description, final Invocation invocation,
      final Object answer
  ) {
    Object result;
    final Method method = invocation.getMethod();
    try {
      final Object[] arguments = invocation.getArguments();

      final ServiceType serviceInstance =
          contract.getSuiteData().getServiceInstance();
      result = method.invoke(serviceInstance, arguments);

    } catch (final InvocationTargetException thrown) {
      final Throwable exception = this.stackTraceSetterService
          .changeStackWithMethod(thrown.getCause(), method);
      notifier.fireTestFailure(new Failure(description, exception));
      return;
    } catch (IllegalAccessException | IllegalArgumentException e) {
      final Throwable exception = this.stackTraceSetterService
          .changeStackWithMethod(e, contract.getDefiningFunction());
      notifier.fireTestFailure(new Failure(description, exception));
      return;
    }
    if (!isAnswerEquals(answer, result)) {
      final Throwable exception = this.stackTraceSetterService
          .changeStackWithMethod(
              new AssertionError(
                  "Bad return, expected " + answer + " got " + result
              ), contract.getDefiningFunction()
          );
      notifier.fireTestFailure(
          new Failure(
              description,
              exception
          )
      );
      return;
    }
  }

  private boolean
      isAnswerEquals(final Object returnValue, final Object answer) {
    if (returnValue == null)
      return answer == null;
    return returnValue.equals(answer);
  }

}
