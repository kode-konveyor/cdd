package com.kodekonveyor.cdd.impl;

import static org.mockito.Mockito.mockingDetails;

import java.lang.reflect.Method;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.mockito.invocation.Invocation;
import org.mockito.stubbing.Stubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodekonveyor.cdd.ContractInfo;
import com.kodekonveyor.cdd.ContractRunnerService;
import com.kodekonveyor.cdd.dto.ContractRunnerData;

@Service
public class ContractRunnerServiceImpl<ServiceClass>
    implements ContractRunnerService<ServiceClass> {

  @Autowired
  ChildDescriptionServiceImpl<ServiceClass> childDescriptionService;
  @Autowired
  RunnerDataCreationServiceImpl<ServiceClass> runnerDataCreationServiceImpl;

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

  private void runChildWithResult(
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

    final Stubbing throwingStubbing =
        mockingDetails(contract.getStub()).getStubbings().iterator().next();
    final Invocation throwingInvocation = throwingStubbing.getInvocation();

    try {
      throwingInvocation.callRealMethod();
      notifier.fireTestFailure(
          new Failure(description, new AssertionError("Expected exception "))
      );
    } catch (final Throwable thrown) {
      if (
        !(thrown.getClass().equals(contract.getExceptionClass()) &&
            thrown.getMessage().equals(contract.getExceptionMessage()))
      ) {

        notifier.fireTestFailure(
            new Failure(description, new AssertionError("Bad exception "))
        );
      }
    }
  }

  private void testReturningContract(
      final ContractInfo<ServiceClass> contract, final RunNotifier notifier,
      final Description description
  ) {
    final Stubbing stubbing =
        mockingDetails(contract.getStub()).getStubbings().iterator().next();
    final Invocation invocation = stubbing.getInvocation();
    final Object answer;
    try {
      answer = stubbing.answer(invocation);
    } catch (final Throwable thrown) {
      notifier.fireTestFailure(new Failure(description, thrown));
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
    try {
      Object[] arguments = invocation.getArguments();
      Method method = invocation.getMethod();
      ServiceClass serviceInstance =
          contract.getSuiteData().getServiceInstance();
      result = method.invoke(serviceInstance, arguments);

    } catch (final Throwable thrown) {
      notifier.fireTestFailure(new Failure(description, thrown));
      return;
    }
    if (!equals(answer, result)) {
      notifier.fireTestFailure(
          new Failure(
              description,
              new AssertionError(
                  "Bad return,expected " + answer + " got " + result
              )
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
