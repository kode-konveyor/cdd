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
@Behaviour("returning contract fails if return predicate fails")
public class ContractRunnerServiceReturningContractWithFailingPredicateTest {

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
            CDDContractTestData.RETURNING_CONTRACT_WITH_FAILING_PREDICATE,
            notifier, CDDContractTestData.DATA
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
      fails_if_return_value_predicate_fails_assertion()
          throws Throwable {

    assertEquals(AssertionError.class, captured.getException().getClass());
  }

}
