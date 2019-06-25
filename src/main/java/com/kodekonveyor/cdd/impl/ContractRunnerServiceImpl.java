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
    runChildWithResult(contract, notifier, data);
  }

  private boolean runChildWithResult(
      final ContractInfo<ServiceClass> contract, final RunNotifier notifier,
      ContractRunnerData<ServiceClass> data
  ) {

    final Description description =
        childDescriptionService.describeChild(contract, data);
    notifier.fireTestStarted(description);
    if (contract.getExceptionClass() == null)
      return testReturningContract(contract, notifier, description);
    else
      return testThrowingContract(contract, notifier, description);
  }

  private boolean testThrowingContract(
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
      return false;
    } catch (final Throwable thrown) {
      if (
        !(thrown.getClass().equals(contract.getExceptionClass()) &&
            thrown.getMessage().equals(contract.getExceptionMessage()))
      ) {

        notifier.fireTestFailure(
            new Failure(description, new AssertionError("Bad exception "))
        );
        return false;
      }
    }
    notifier.fireTestFinished(description);
    return true;
  }

  private boolean testReturningContract(
      final ContractInfo<?> contract, final RunNotifier notifier,
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
      notifier.fireTestFinished(description);
      return false;
    }

    boolean isPassed = runOneReturningContract(
        contract, notifier, description, invocation, answer
    );
    if (isPassed)
      notifier.fireTestFinished(description);
    return isPassed;
  }

  private boolean runOneReturningContract(
      final ContractInfo<?> contract, final RunNotifier notifier,
      final Description description, final Invocation invocation,
      final Object answer
  ) {
    System.out.println("runOneReturningContract " + invocation);
    Object result;
    try {
      Object[] arguments = invocation.getArguments();
      Method method = invocation.getMethod();
      Object serviceInstance = contract.getSuiteData().getServiceInstance();
      result = method.invoke(serviceInstance, arguments);

    } catch (final Throwable thrown) {
      notifier.fireTestFailure(new Failure(description, thrown));
      notifier.fireTestFinished(description);
      return false;
    }
    Class<? extends Object> contracts = contract.getReturnValueContracts();
    System.out.println("answer:" + answer);
    if (null != contracts) {
      System.out.println("contract:" + contracts);
      runAssertionsOnValue(answer, contract, notifier, description, contracts);
      //runAssertionsOnValue(result, contract, notifier, description, contracts);
    } else {
      if (!contract.getEqualityPredicate().equals(answer, result)) {
        System.out.println(answer + "<->" + result);
        notifier.fireTestFailure(
            new Failure(
                description,
                new AssertionError(
                    "Bad return,expected " + answer + " got " + result
                )
            )
        );
        return false;
      }
    }
    return true;
  }

  private void runAssertionsOnValue(
      Object answer, ContractInfo<?> parentContract, RunNotifier notifier,
      Description description, Class<? extends Object> contracts
  ) {
    ContractRunnerData<ServiceClass> theData;
    try {
      theData = runnerDataCreationServiceImpl
          .makeRunnerDataFromTestClass(contracts, answer);
    } catch (Throwable e) {
      notifier.fireTestFailure(new Failure(description, e));
      return;
    }
    for (ContractInfo<ServiceClass> theContract : theData.getContracts()) {
      boolean passed = runChildWithResult(theContract, notifier, theData);
      System.out.println("passed: " + passed);
      if (!passed)
        return;
    }
  }

  private boolean equals(final Object returnValue, final Object answer) {
    if (returnValue == null)
      return answer == null;
    return returnValue.equals(answer);
  }

}
