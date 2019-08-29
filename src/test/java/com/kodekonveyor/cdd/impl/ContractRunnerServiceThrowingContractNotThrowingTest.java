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
  "Throwing contract fails if the service does not throw exception defined in the contract"
)
public class ContractRunnerServiceThrowingContractNotThrowingTest {

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
            CDDContractTestData.NOT_THROWING_THROWING_CONTRACT, notifier,
            CDDContractTestData.DATA
        );

    final ArgumentCaptor<Failure> captor =
        ArgumentCaptor.forClass(Failure.class);

    Mockito.verify(notifier, Mockito.times(1))
        .fireTestFailure(captor.capture());
    captured = captor.getValue();
  }

  @Test
  public void the_test_result_is_associated_with_the_test_description()
      throws Throwable {
    assertStartAndEndSignaledForTheDescription(
        notifier, CDDContractTestData.DESCRIPTION
    );
  }

  @Test
  public void the_service_is_referenced_in_the_stack_trace()
      throws Throwable {
    assertEquals(
        TestContractTestData.SERVICE_FILE_NAME,
        captured.getException().getStackTrace()[0].getFileName()
    );
  }

  @Test
  public void an_AssertionError_is_signaled()
      throws Throwable {
    assertEquals(AssertionError.class, captured.getException().getClass());
  }

  @Test
  public void the_error_message_says_so()
      throws Throwable {
    assertEquals(
        TestContractTestData.NO_EXCEPTION_MESSAGE,
        captured.getException().getMessage()
    );
  }

}
