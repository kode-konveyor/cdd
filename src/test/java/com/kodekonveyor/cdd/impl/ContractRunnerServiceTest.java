package com.kodekonveyor.cdd.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runner.notification.RunNotifier;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.kodekonveyor.cdd.ContractInfo;
import com.kodekonveyor.cdd.dto.ContractRunnerData;
import com.kodekonveyor.cdd.testartifacts.ExampleService;

@RunWith(MockitoJUnitRunner.class)
public class ContractRunnerServiceTest {

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
        doThrow(new IllegalArgumentException("bad parameter: 1")).when(exampleService);
    stub.testedMethod(1);
    doReturn(IllegalArgumentException.class).when(contract).getExceptionClass();
    doReturn("bad parameter: 1").when(contract).getExceptionMessage();
    doReturn(stub).when(contract).getStub();
    doReturn(description).when(childDescriptionService)
        .describeChild(contract, data);
    contractRunnerServiceImpl
        .runChild(contract, notifier, data);
    verify(notifier, never()).fireTestFailure(any());
    verify(notifier, times(1)).fireTestStarted(any());
    verify(notifier, times(1)).fireTestFinished(any());
  }

}
