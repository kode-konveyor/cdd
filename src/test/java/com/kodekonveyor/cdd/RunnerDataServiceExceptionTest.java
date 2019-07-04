package com.kodekonveyor.cdd;

import static com.kodekonveyor.cdd.exception.ThrowableTester.assertThrows;
import static org.mockito.Mockito.doReturn;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import com.kodekonveyor.cdd.exception.StackTraceSetterService;
import com.kodekonveyor.cdd.impl.ChildDescriptionServiceImpl;
import com.kodekonveyor.cdd.impl.FieldGetterServiceImpl;
import com.kodekonveyor.cdd.impl.RunnerDataCreationServiceImpl;
import com.kodekonveyor.cdd.testartifacts.ExampleService;
import com.kodekonveyor.cdd.testartifacts.TestContractNoIT;

@RunWith(MockitoJUnitRunner.class)
public class RunnerDataServiceExceptionTest {

  @InjectMocks
  RunnerDataCreationServiceImpl<ExampleService> runnerDataService;

  @Mock
  FieldGetterServiceImpl fieldGetterService;

  @Mock
  ChildDescriptionServiceImpl<ExampleService> childDescriptionService;

  @Mock
  ContractCreationService<ExampleService> contractCreationService;

  @Mock
  private AutowireCapableBeanFactory beanFactory;

  @Mock
  StackTraceSetterService stackTraceSetterService;

  @Mock
  TestContractNoIT testInstance;

  @Test
  public void throws_an_exception_when_no__field_annotated_as_ContractFactory()
      throws Throwable {
    doReturn(testInstance).when(beanFactory).createBean(TestContractNoIT.class);
    runnerDataService.stackTraceSetterService = new StackTraceSetterService();

    assertThrows(
        () -> runnerDataService
            .makeRunnerDataFromTestClass(TestContractNoIT.class)
    )
        .assertException(AssertionError.class)
        .assertMessageContains(RunnerDataCreationServiceImpl.NO_IT_FIELD)
        .assertMessageContains(testInstance.getClass().getSimpleName())
        .assertStackClass(0, TestContractNoIT.class.getName());
  }

  @Test
  public void throws_an_exception_when_no_bean_can_be_created()
      throws Throwable {
    doReturn(null).when(beanFactory).createBean(TestContractNoIT.class);
    runnerDataService.stackTraceSetterService = new StackTraceSetterService();

    assertThrows(
        () -> runnerDataService
            .makeRunnerDataFromTestClass(TestContractNoIT.class)
    )
        .assertException(AssertionError.class)
        .assertMessageContains(RunnerDataCreationServiceImpl.NO_TEST_INSTANCE)
        .assertMessageContains(
            testInstance.getClass().getName().replaceAll("\\$.*", "")
        )
        .assertStackClass(0, TestContractNoIT.class.getName());
  }

}
