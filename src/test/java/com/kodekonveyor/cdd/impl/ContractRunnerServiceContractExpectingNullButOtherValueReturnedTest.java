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
import com.kodekonveyor.cdd.annotation.Behaviour;
import com.kodekonveyor.cdd.build.impl.ChildDescriptionServiceImpl;
import com.kodekonveyor.cdd.exception.impl.StackTraceSetterServiceImpl;
import com.kodekonveyor.cdd.run.impl.ContractRunnerServiceImpl;
import com.kodekonveyor.cdd.testartifacts.ExampleService;

@RunWith(MockitoJUnitRunner.class)
@Behaviour(
  "contract fails when contract expects null but other value is returned"
)
public class ContractRunnerServiceContractExpectingNullButOtherValueReturnedTest {

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
            CDDContractTestData.CONTRACT_EXPECTING_NULL_RETURN_BUT_SERVICE_RETURNING_OTHER_VALUE,
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
      signals_AssertionError()
          throws Throwable {

    assertEquals(
        AssertionError.class, captured.getException().getClass()
    );
  }

  @Test
  public void
      message_contains_the_expected_and_returned_value()
          throws Throwable {

    assertEquals(
        "Bad return, expected 43 got null",
        captured.getException().getMessage()
    );
  }

  @Test
  public void
      the_stack_trace_references_the_contract()
          throws Throwable {

    assertEquals(
        "TestContract.java",
        captured.getException().getStackTrace()[0].getFileName()
    );
  }

}
