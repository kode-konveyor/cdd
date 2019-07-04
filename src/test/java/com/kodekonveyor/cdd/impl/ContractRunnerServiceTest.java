package com.kodekonveyor.cdd.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.kodekonveyor.cdd.ContractInfo;
import com.kodekonveyor.cdd.dto.ContractRunnerData;
import com.kodekonveyor.cdd.exception.StackTraceSetterService;
import com.kodekonveyor.cdd.testartifacts.ExampleService;
import com.kodekonveyor.cdd.testartifacts.TestContract;
import com.kodekonveyor.cdd.testdata.ContractTestData;

@RunWith(MockitoJUnitRunner.class)
public class ContractRunnerServiceTest {

  private static final int VALUE_NO_EXCEPTION = 42;

  private static final String BAD_PARAMETER = "bad parameter: 1";

  @InjectMocks
  ContractRunnerServiceImpl<ExampleService> contractRunnerServiceImpl;

  @Mock
  ChildDescriptionServiceImpl<ExampleService> childDescriptionService;
  @Mock
  RunnerDataCreationServiceImpl<ExampleService> runnerDataCreationServiceImpl;

  @Mock
  private ContractInfo<ExampleService> contract;

  @Mock
  RunNotifier notifier;
  @Mock
  ContractRunnerData<ExampleService> data;
  @Mock
  Description description;
  @Mock
  ExampleService exampleService;

  @Test
  public void runs_throwing_contract() throws Throwable {
    ExampleService stub =
        doThrow(new IllegalArgumentException(BAD_PARAMETER)).when(exampleService);
    stub.testedMethod(1);
    doReturn(IllegalArgumentException.class).when(contract).getExceptionClass();
    doReturn(BAD_PARAMETER).when(contract).getExceptionMessage();
    doReturn(stub).when(contract).getStub();
    doReturn(description).when(childDescriptionService)
        .describeChild(contract, data);
    contractRunnerServiceImpl
        .runChild(contract, notifier, data);
    verify(notifier, never()).fireTestFailure(any());
    verify(notifier, times(1)).fireTestStarted(any());
    verify(notifier, times(1)).fireTestFinished(any());
  }

  @Test
  public void fails_if_throwing_contract_does_not_throw() throws Throwable {
    contractRunnerServiceImpl.stackTraceSetterService =
        new StackTraceSetterService();
    ExampleService stub =
        doReturn(VALUE_NO_EXCEPTION).when(exampleService);
    stub.testedMethod(VALUE_NO_EXCEPTION);
    doReturn(IllegalArgumentException.class).when(contract).getExceptionClass();
    doReturn(stub).when(contract).getStub();
    doReturn(description).when(childDescriptionService)
        .describeChild(contract, data);
    contractRunnerServiceImpl
        .runChild(contract, notifier, data);
    ArgumentCaptor<Failure> captor = ArgumentCaptor.forClass(Failure.class);

    verify(notifier, times(1)).fireTestFailure(captor.capture());
    Failure captured = captor.getValue();
    assertEquals(AssertionError.class, captured.getException().getClass());
    assertEquals(
        "Expected IllegalArgumentException, but no exception thrown",
        captured.getException().getMessage()
    );
    assertEquals(
        "ExampleService.java",
        captured.getException().getStackTrace()[0].getFileName()
    );
    verify(notifier, times(1)).fireTestStarted(any());
    verify(notifier, times(1)).fireTestFinished(any());
  }

  @Test
  public void fails_if_throwing_contract_throws_other_exception()
      throws Throwable {
    contractRunnerServiceImpl.stackTraceSetterService =
        new StackTraceSetterService();
    ExampleService stub =
        doThrow(new IllegalArgumentException(BAD_PARAMETER)).when(exampleService);
    stub.testedMethod(1);
    doReturn(java.lang.ArithmeticException.class).when(contract)
        .getExceptionClass();
    doReturn(BAD_PARAMETER).when(contract).getExceptionMessage();
    doReturn(stub).when(contract).getStub();
    doReturn(description).when(childDescriptionService)
        .describeChild(contract, data);
    contractRunnerServiceImpl
        .runChild(contract, notifier, data);
    ArgumentCaptor<Failure> captor = ArgumentCaptor.forClass(Failure.class);

    verify(notifier, times(1)).fireTestFailure(captor.capture());
    Failure captured = captor.getValue();
    assertEquals(AssertionError.class, captured.getException().getClass());
    assertEquals(
        "Expected ArithmeticException(bad parameter: 1), but see cause",
        captured.getException().getMessage()
    );
    assertEquals(
        IllegalArgumentException.class,
        captured.getException().getCause().getClass()
    );
    assertEquals(
        BAD_PARAMETER,
        captured.getException().getCause().getMessage()
    );
    assertEquals(
        "ExampleService.java",
        captured.getException().getStackTrace()[0].getFileName()
    );
    verify(notifier, times(1)).fireTestStarted(any());
    verify(notifier, times(1)).fireTestFinished(any());
  }

  @Test
  public void
      fails_if_throwing_contract_throws_same_exception_with_other_message()
          throws Throwable {
    contractRunnerServiceImpl.stackTraceSetterService =
        new StackTraceSetterService();
    ExampleService stub =
        doThrow(new IllegalArgumentException(BAD_PARAMETER)).when(exampleService);
    stub.testedMethod(1);
    doReturn(IllegalArgumentException.class).when(contract)
        .getExceptionClass();
    doReturn("bad parameter: 42").when(contract).getExceptionMessage();
    doReturn(stub).when(contract).getStub();
    doReturn(description).when(childDescriptionService)
        .describeChild(contract, data);
    contractRunnerServiceImpl
        .runChild(contract, notifier, data);
    ArgumentCaptor<Failure> captor = ArgumentCaptor.forClass(Failure.class);

    verify(notifier, times(1)).fireTestFailure(captor.capture());
    Failure captured = captor.getValue();
    assertEquals(AssertionError.class, captured.getException().getClass());
    assertEquals(
        "Expected IllegalArgumentException(bad parameter: 42), but see cause",
        captured.getException().getMessage()
    );
    assertEquals(
        IllegalArgumentException.class,
        captured.getException().getCause().getClass()
    );
    assertEquals(
        BAD_PARAMETER,
        captured.getException().getCause().getMessage()
    );
    assertEquals(
        "ExampleService.java",
        captured.getException().getStackTrace()[0].getFileName()
    );
    verify(notifier, times(1)).fireTestStarted(any());
    verify(notifier, times(1)).fireTestFinished(any());
  }

  @Test
  public void
      fails_if_returning_contract_has_throwing_stub()
          throws Throwable {
    contractRunnerServiceImpl.stackTraceSetterService =
        new StackTraceSetterService();
    ExampleService stub =
        doThrow(new IllegalArgumentException(BAD_PARAMETER)).when(exampleService);
    stub.testedMethod(1);
    doReturn(stub).when(contract).getStub();
    doReturn(description).when(childDescriptionService)
        .describeChild(contract, data);
    contractRunnerServiceImpl
        .runChild(contract, notifier, data);
    ArgumentCaptor<Failure> captor = ArgumentCaptor.forClass(Failure.class);

    verify(notifier, times(1)).fireTestFailure(captor.capture());
    Failure captured = captor.getValue();
    assertEquals(
        IllegalArgumentException.class, captured.getException().getClass()
    );
    assertEquals(
        "bad parameter: 1",
        captured.getException().getMessage()
    );
    assertEquals(
        "ExampleService.java",
        captured.getException().getStackTrace()[0].getFileName()
    );
    verify(notifier, times(1)).fireTestStarted(any());
    verify(notifier, times(1)).fireTestFinished(any());
  }

  @Test
  public void
      fails_if_returning_contract_throws()
          throws Throwable {
    contractRunnerServiceImpl.stackTraceSetterService =
        new StackTraceSetterService();
    ExampleService stub =
        doReturn(VALUE_NO_EXCEPTION).when(exampleService);
    stub.testedMethod(1);
    doReturn(stub).when(contract).getStub();
    doReturn(data).when(contract).getSuiteData();
    doReturn(exampleService).when(data).getServiceInstance();
    doThrow(new IllegalArgumentException("bad parameter: 1"))
        .when(exampleService).testedMethod(1);
    doReturn(description).when(childDescriptionService)
        .describeChild(contract, data);
    contractRunnerServiceImpl
        .runChild(contract, notifier, data);
    ArgumentCaptor<Failure> captor = ArgumentCaptor.forClass(Failure.class);

    verify(notifier, times(1)).fireTestFailure(captor.capture());
    Failure captured = captor.getValue();
    assertEquals(
        IllegalArgumentException.class, captured.getException().getClass()
    );
    assertEquals(
        "bad parameter: 1",
        captured.getException().getMessage()
    );
    assertEquals(
        "ExampleService.java",
        captured.getException().getStackTrace()[0].getFileName()
    );
    verify(notifier, times(1)).fireTestStarted(any());
    verify(notifier, times(1)).fireTestFinished(any());
  }

  @Test
  public void
      fails_if_returning_contract_called_with_bad_parameters()
          throws Throwable {
    contractRunnerServiceImpl.stackTraceSetterService =
        new StackTraceSetterService();
    ExampleService stub =
        doReturn(VALUE_NO_EXCEPTION).when(exampleService);
    stub.testedMethod(1);
    doReturn(stub).when(contract).getStub();
    doReturn(data).when(contract).getSuiteData();
    doReturn(
        TestContract.class.getMethod(ContractTestData.CONTRACT_PASSING_RETURN)
    ).when(contract)
        .getDefiningFunction();
    doReturn(this).when(data).getServiceInstance();
    doReturn(description).when(childDescriptionService)
        .describeChild(contract, data);
    contractRunnerServiceImpl
        .runChild(contract, notifier, data);
    ArgumentCaptor<Failure> captor = ArgumentCaptor.forClass(Failure.class);

    verify(notifier, times(1)).fireTestFailure(captor.capture());
    Failure captured = captor.getValue();
    assertEquals(
        IllegalArgumentException.class, captured.getException().getClass()
    );
    assertEquals(
        "object is not an instance of declaring class",
        captured.getException().getMessage()
    );
    assertEquals(
        "TestContract.java",
        captured.getException().getStackTrace()[0].getFileName()
    );
    verify(notifier, times(1)).fireTestStarted(any());
    verify(notifier, times(1)).fireTestFinished(any());
  }

  @Test
  public void
      fails_if_returning_contract_return_unexpected_value()
          throws Throwable {
    contractRunnerServiceImpl.stackTraceSetterService =
        new StackTraceSetterService();
    ExampleService stub =
        doReturn(VALUE_NO_EXCEPTION).when(exampleService);
    stub.testedMethod(VALUE_NO_EXCEPTION);
    doReturn(stub).when(contract).getStub();
    doReturn(data).when(contract).getSuiteData();
    doReturn(
        TestContract.class.getMethod(ContractTestData.CONTRACT_PASSING_RETURN)
    ).when(contract)
        .getDefiningFunction();
    doReturn(exampleService).when(data).getServiceInstance();
    doReturn(VALUE_NO_EXCEPTION + 1).when(exampleService)
        .testedMethod(VALUE_NO_EXCEPTION);
    doReturn(description).when(childDescriptionService)
        .describeChild(contract, data);
    contractRunnerServiceImpl
        .runChild(contract, notifier, data);
    ArgumentCaptor<Failure> captor = ArgumentCaptor.forClass(Failure.class);

    verify(notifier, times(1)).fireTestFailure(captor.capture());
    Failure captured = captor.getValue();
    assertEquals(
        AssertionError.class, captured.getException().getClass()
    );
    assertEquals(
        "Bad return, expected 42 got 43",
        captured.getException().getMessage()
    );
    assertEquals(
        "TestContract.java",
        captured.getException().getStackTrace()[0].getFileName()
    );
    verify(notifier, times(1)).fireTestStarted(any());
    verify(notifier, times(1)).fireTestFinished(any());
  }

  @Test
  public void
      works_for_null_answer()
          throws Throwable {
    contractRunnerServiceImpl.stackTraceSetterService =
        new StackTraceSetterService();
    ExampleService stub =
        doReturn(null).when(exampleService);
    stub.testedMethod(VALUE_NO_EXCEPTION);
    doReturn(stub).when(contract).getStub();
    doReturn(data).when(contract).getSuiteData();
    doReturn(exampleService).when(data).getServiceInstance();
    doReturn(null).when(exampleService)
        .testedMethod(VALUE_NO_EXCEPTION);
    doReturn(description).when(childDescriptionService)
        .describeChild(contract, data);
    contractRunnerServiceImpl
        .runChild(contract, notifier, data);

    verify(notifier, times(0)).fireTestFailure(any());
    verify(notifier, times(1)).fireTestStarted(any());
    verify(notifier, times(1)).fireTestFinished(any());
  }

  @Test
  public void
      failing_works_for_null_answer()
          throws Throwable {
    contractRunnerServiceImpl.stackTraceSetterService =
        new StackTraceSetterService();
    ExampleService stub =
        doReturn(null).when(exampleService);
    stub.testedMethod(VALUE_NO_EXCEPTION);
    doReturn(stub).when(contract).getStub();
    doReturn(data).when(contract).getSuiteData();
    doReturn(
        TestContract.class.getMethod(ContractTestData.CONTRACT_PASSING_RETURN)
    ).when(contract)
        .getDefiningFunction();
    doReturn(exampleService).when(data).getServiceInstance();
    doReturn(VALUE_NO_EXCEPTION + 1).when(exampleService)
        .testedMethod(VALUE_NO_EXCEPTION);
    doReturn(description).when(childDescriptionService)
        .describeChild(contract, data);
    contractRunnerServiceImpl
        .runChild(contract, notifier, data);
    ArgumentCaptor<Failure> captor = ArgumentCaptor.forClass(Failure.class);

    verify(notifier, times(1)).fireTestFailure(captor.capture());
    Failure captured = captor.getValue();
    assertEquals(
        AssertionError.class, captured.getException().getClass()
    );
    assertEquals(
        "Bad return, expected null got 43",
        captured.getException().getMessage()
    );
    assertEquals(
        "TestContract.java",
        captured.getException().getStackTrace()[0].getFileName()
    );
    verify(notifier, times(1)).fireTestStarted(any());
    verify(notifier, times(1)).fireTestFinished(any());
  }

}
