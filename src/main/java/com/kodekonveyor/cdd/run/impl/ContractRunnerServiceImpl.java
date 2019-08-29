package com.kodekonveyor.cdd.run.impl;

import static org.mockito.Mockito.mockingDetails;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

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
          MessageFormat.format(
              "Expected {0}, but no exception thrown",
              contract.getExceptionClass().getSimpleName()
          )
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
    isAnswerEquals(contract, answer, result, notifier, description);
  }

  private void notifyFailureForBadResult(
      final ContractInfo<ServiceType> contract, final RunNotifier notifier,
      final Description description, final Object answer, final Object result
  ) {
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
  }

  private void
      isAnswerEquals(
          final ContractInfo<ServiceType> contract, final Object returnValue,
          final Object answer, final RunNotifier notifier,
          final Description description
      ) {

    final List<String> checkedReturnDetails =
        contract.getReturnDetailChecks();
    checkReturnDetails(
        contract, returnValue, notifier, description, checkedReturnDetails
    );
    if (checkedReturnDetails.isEmpty())
      checkReturnValue(contract, returnValue, answer, notifier, description);
  }

  private void checkReturnValue(
      final ContractInfo<ServiceType> contract, final Object returnValue,
      final Object answer, final RunNotifier notifier,
      final Description description
  ) {
    if (returnValue == null) {
      if (answer == null)
        return;
    } else if (returnValue.equals(answer))
      return;
    notifyFailureForBadResult(
        contract, notifier, description, answer, returnValue
    );
  }

  private void checkReturnDetails(
      final ContractInfo<ServiceType> contract, final Object returnValue,
      final RunNotifier notifier, final Description description,
      final List<String> checkedReturnDetails
  ) throws AssertionError {
    for (final String name : checkedReturnDetails) {
      final Map<String, Method> returnValueContracts =
          contract.getSuiteData().getReturnValueContracts();
      final Method predicate = returnValueContracts.get(name);
      final Object testInstance = contract.getSuiteData().getTestInstance();
      if (null == predicate) {
        notifyNullPredicateFailure(notifier, description, name, testInstance);
        return;
      }
      invokePredicate(
          returnValue, notifier, description, predicate, testInstance
      );
    }
  }

  private void invokePredicate(
      final Object returnValue, final RunNotifier notifier,
      final Description description, final Method predicate,
      final Object testInstance
  ) throws AssertionError {
    try {
      predicate
          .invoke(testInstance, returnValue);
    } catch (
        IllegalAccessException | IllegalArgumentException e
    ) {
      throw new AssertionError(e);
    } catch (final InvocationTargetException e) {
      final AssertionError exception = new AssertionError(e.getCause());
      notifyFailure(notifier, description, predicate, exception);

    }
  }

  private void notifyNullPredicateFailure(
      final RunNotifier notifier, final Description description,
      final String name, final Object testInstance
  ) {
    final AssertionError exception =
        new AssertionError("no predicate: " + name);
    stackTraceSetterService
        .changeStackWithClass(exception, testInstance.getClass());
    notifier.fireTestFailure(
        new Failure(
            description,
            exception
        )
    );
  }

  private void notifyFailure(
      final RunNotifier notifier, final Description description,
      final Method predicate, final AssertionError exception
  ) {
    stackTraceSetterService.changeStackWithMethod(exception, predicate);
    notifier.fireTestFailure(
        new Failure(
            description,
            exception
        )
    );
  }

}
