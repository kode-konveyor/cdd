package com.kodekonveyor.cdd.impl;

import static com.kodekonveyor.cdd.TestHelper.assertStartAndEndSignaledForTheDescription;
import static org.mockito.ArgumentMatchers.any;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.notification.RunNotifier;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.kodekonveyor.cdd.CDDContractTestData;
import com.kodekonveyor.cdd.ChildDescriptionServiceStubs;
import com.kodekonveyor.cdd.annotation.Behaviour;
import com.kodekonveyor.cdd.build.impl.ChildDescriptionServiceImpl;
import com.kodekonveyor.cdd.exception.impl.StackTraceSetterServiceImpl;
import com.kodekonveyor.cdd.run.impl.ContractRunnerServiceImpl;
import com.kodekonveyor.cdd.testartifacts.ExampleService;

@RunWith(MockitoJUnitRunner.class)
public class ContractRunnerServiceReturningContractReturningUnexpectedTest {

  @InjectMocks
  private ContractRunnerServiceImpl<ExampleService> contractRunnerServiceImpl;

  @Mock
  private ChildDescriptionServiceImpl<ExampleService> childDescriptionService;

  private RunNotifier notifier;

  @Before
  public void setUp() {
    notifier = Mockito.mock(RunNotifier.class);
    contractRunnerServiceImpl
        .setStackTraceSetterService(new StackTraceSetterServiceImpl());
    ChildDescriptionServiceStubs.behaviour(
        childDescriptionService
    );
    contractRunnerServiceImpl
        .runChild(
            CDDContractTestData.THROWING_CONTRACT, notifier,
            CDDContractTestData.DATA
        );
  }

  @Behaviour(
    "Throwing contract passes if the service throws the right exception"
  )
  @Test
  public void runs_throwing_contract() throws Throwable {
    Mockito.verify(notifier, Mockito.never()).fireTestFailure(any());
  }

  @Behaviour(
    "Throwing contract passes if the service throws the right exception"
  )
  @Test
  public void
      the_test_result_is_associated_with_the_test_description()
          throws Throwable {
    assertStartAndEndSignaledForTheDescription(
        notifier, CDDContractTestData.DESCRIPTION
    );
  }

}
