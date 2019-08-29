package com.kodekonveyor.cdd.impl;

import static com.kodekonveyor.cdd.TestHelper.assertStartAndEndSignaledForTheDescription;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.kodekonveyor.cdd.CDDContractTestData;
import com.kodekonveyor.cdd.ChildDescriptionServiceStubs;
import com.kodekonveyor.cdd.TestContractTestData;
import com.kodekonveyor.cdd.annotation.Behaviour;
import com.kodekonveyor.cdd.build.impl.ChildDescriptionServiceImpl;
import com.kodekonveyor.cdd.exception.impl.StackTraceSetterServiceImpl;
import com.kodekonveyor.cdd.run.impl.ContractRunnerServiceImpl;
import com.kodekonveyor.cdd.testartifacts.ExampleService;

@Behaviour("Returning contract fails if service throws exception")
@RunWith(MockitoJUnitRunner.class)
public class ContractRunnerServiceReturningContractWithThrowingServiceTest {

  @InjectMocks
  private ContractRunnerServiceImpl<ExampleService> contractRunnerServiceImpl;

  @Mock
  private ChildDescriptionServiceImpl<ExampleService> childDescriptionService;

  private RunNotifier notifier;

  private Failure captured;

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
            CDDContractTestData.RETURNING_CONTRACT_WITH_THROWING_SERVICE,
            notifier,
            CDDContractTestData.DATA
        );
    final ArgumentCaptor<Failure> captor =
        ArgumentCaptor.forClass(Failure.class);

    Mockito.verify(notifier, Mockito.times(1))
        .fireTestFailure(captor.capture());
    captured = captor.getValue();
  }

  @Test
  public void
      the_test_result_is_associated_with_the_test_description()
          throws Throwable {
    assertStartAndEndSignaledForTheDescription(
        notifier, CDDContractTestData.DESCRIPTION
    );
  }

  @Test
  public void
      signals_the_exception_thrown_by_the_service()
          throws Throwable {

    assertEquals(
        IllegalArgumentException.class, captured.getException().getClass()
    );
  }

  @Test
  public void
      message_is_the_exception_message_thrown()
          throws Throwable {

    assertEquals(
        TestContractTestData.BAD_PARAMETER,
        captured.getException().getMessage()
    );
  }

  @Test
  public void
      the_stack_trace_refers_to_the_service_class()
          throws Throwable {

    assertEquals(
        TestContractTestData.SERVICE_FILE_NAME,
        captured.getException().getStackTrace()[0].getFileName()
    );
  }

}
