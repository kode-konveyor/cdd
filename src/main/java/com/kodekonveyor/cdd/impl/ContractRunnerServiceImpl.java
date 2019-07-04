package com.kodekonveyor.cdd.impl;

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
import com.kodekonveyor.cdd.ContractRunnerService;
import com.kodekonveyor.cdd.dto.ContractRunnerData;
import com.kodekonveyor.cdd.exception.StackTraceSetterService;

@Service
public class ContractRunnerServiceImpl<ServiceClass>
    implements ContractRunnerService<ServiceClass> {

  @Autowired
  ChildDescriptionServiceImpl<ServiceClass> childDescriptionService;
  @Autowired
  RunnerDataCreationServiceImpl<ServiceClass> runnerDataCreationServiceImpl;
  @Autowired
  StackTraceSetterService stackTraceSetterService;

  @Override
  public void runChild(
      final ContractInfo<ServiceClass> contract, final RunNotifier notifier,
      ContractRunnerData<ServiceClass> data
  ) {
    final Description description =
        childDescriptionService.describeChild(contract, data);
    data.getTestInstance();
    notifier.fireTestStarted(description);
    runChildWithResult(contract, notifier, data, description);
    notifier.fireTestFinished(description);
  }

  void runChildWithResult(
      final ContractInfo<ServiceClass> contract, final RunNotifier notifier,
      ContractRunnerData<ServiceClass> data, Description description
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
      AssertionError originalException = new AssertionError(
          "Expected " + contract.getExceptionClass().getSimpleName() +
              ", but no exception thrown"
      );
      Throwable exception = stackTraceSetterService
          .changeStackWithMethod(
              originalException, throwingStubbing.getMethod()
          );
      notifier.fireTestFailure(
          new Failure(
              description, exception
          )
      );
    } catch (final Throwable thrown) {
      if (
        !(thrown.getClass().equals(contract.getExceptionClass()) &&
            thrown.getMessage().equals(contract.getExceptionMessage()))
      ) {
        Throwable thrownException = stackTraceSetterService
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
      final ContractInfo<ServiceClass> contract, final RunNotifier notifier,
      final Description description
  ) {
    final StubbedInvocationMatcher stubbing =
        (StubbedInvocationMatcher) mockingDetails(contract.getStub()).getStubbings().iterator().next();
    final Invocation invocation = stubbing.getInvocation();
    final Object answer;
    try {
      stubbing.markStubUsed(invocation);
      answer = stubbing.answer(invocation);
    } catch (final Throwable thrown) {
      Throwable exception = stackTraceSetterService
          .changeStackWithMethod(thrown, stubbing.getMethod());
      notifier.fireTestFailure(new Failure(description, exception));
      return;
    }

    runOneReturningContract(
        contract, notifier, description, invocation, answer
    );
  }

  private void runOneReturningContract(
      final ContractInfo<ServiceClass> contract, final RunNotifier notifier,
      final Description description, final Invocation invocation,
      final Object answer
  ) {
    Object result;
    Method method = invocation.getMethod();
    try {
      Object[] arguments = invocation.getArguments();

      ServiceClass serviceInstance =
          contract.getSuiteData().getServiceInstance();
      result = method.invoke(serviceInstance, arguments);

    } catch (final InvocationTargetException thrown) {
      Throwable exception = stackTraceSetterService
          .changeStackWithMethod(thrown.getCause(), method);
      notifier.fireTestFailure(new Failure(description, exception));
      return;
    } catch (IllegalAccessException | IllegalArgumentException e) {
      Throwable exception = stackTraceSetterService
          .changeStackWithMethod(e, contract.getDefiningFunction());
      notifier.fireTestFailure(new Failure(description, exception));
      return;
    }
    if (!equals(answer, result)) {
      Throwable exception = stackTraceSetterService
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

  private boolean equals(final Object returnValue, final Object answer) {
    if (returnValue == null)
      return answer == null;
    return returnValue.equals(answer);
  }

}
