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

@RunWith(MockitoJUnitRunner.class)
@Behaviour(
  "Throwing contract fails if the service throws other exception than defined in the contract"
)
public class ContractRunnerServiceThrowingContractThrowsAnotherExceptionTest {

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
            CDDContractTestData.THROWING_CONTRACT_THROWING_ANOTHER_EXCEPTION,
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
  public void the_stack_trace_references_the_service_tested()
      throws Throwable {
    assertEquals(
        TestContractTestData.SERVICE_FILE_NAME,
        captured.getException().getStackTrace()[0].getFileName()
    );
  }

  @Test
  public void signals_AssertionError()
      throws Throwable {
    assertEquals(AssertionError.class, captured.getException().getClass());
  }

  @Test
  public void the_message_details_the_problem()
      throws Throwable {
    assertEquals(
        TestContractTestData.BAD_EXCEPTION_MESSAGE,
        captured.getException().getMessage()
    );
  }

  @Test
  public void the_cause_contains_the_actual_exception()
      throws Throwable {
    assertEquals(
        IllegalArgumentException.class,
        captured.getException().getCause().getClass()
    );
  }

  @Test
  public void the_message_of_the_cause_is_the_original_cause()
      throws Throwable {
    assertEquals(
        TestContractTestData.BAD_PARAMETER,
        captured.getException().getCause().getMessage()
    );
  }

}
