package com.kodekonveyor.cdd;

import static com.kodekonveyor.cdd.exception.ThrowableTester.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.kodekonveyor.cdd.assemble.ContractInfoFactory;
import com.kodekonveyor.cdd.build.impl.ContractCreationServiceImpl;
import com.kodekonveyor.cdd.run.dto.ContractRunnerData;
import com.kodekonveyor.cdd.testartifacts.ExampleService;
import com.kodekonveyor.cdd.testartifacts.TestContract;
import com.kodekonveyor.cdd.testartifacts.TestContractNoIT;

@RunWith(MockitoJUnitRunner.class)
public class ContractCreationServiceErrorCasesTest {

  @InjectMocks
  public ContractCreationServiceImpl<
      ExampleService> contractCreationServiceImpl;

  @Mock
  private ContractInfoFactory<ExampleService> contractInfoFactory;

  @Mock
  private List<ContractInfo<ExampleService>> contracts;
  @Mock
  private ContractRunnerData<ExampleService> contractRunnerData;
  @Mock
  private ExampleService exampleService;
  @Mock
  private TestContract contractInstance;
  @Mock
  private TestContractNoIT contractNOITInstance;
  @Mock
  public ContractInfo<ExampleService> contract;

  private Method contractMethod;

  @Before
  public void setUp() throws Throwable {
    contractMethod =
        TestContract.class
            .getMethod(TestContractTestData.CONTRACT_PASSING_RETURN);

    when(contractInfoFactory.getObject())
        .thenReturn(contract);
    when(contractRunnerData.getTestInstance()).thenReturn(contractInstance);
    when(contractRunnerData.getServiceInstance())
        .thenReturn(exampleService);
  }

  @Test
  public void throws_an_error_if_cannot_write_it_field()
      throws NoSuchFieldException {
    when(contractRunnerData.getItField())
        .thenReturn(
            TestContract.class.getField(TestContractTestData.IT_FIELD_NAME)
        );
    when(contractRunnerData.getTestInstance()).thenReturn(contractNOITInstance);
    assertThrows(
        () -> contractCreationServiceImpl
            .createContract(
                contracts,
                contractMethod, contractRunnerData
            )
    )
        .assertException(AssertionError.class)
        .assertStackFileName(0, TestContractTestData.CLASS_FILE_NAME)
        .assertStackClass(0, TestContractTestData.FULL_CLASSNAME)
        .assertStackLineNumber(0, TestContractTestData.METHOD_LINE_NUMBER)
        .assertStackMethod(0, TestContractTestData.CONTRACT_PASSING_RETURN)
        .assertMessageMatches(TestContractTestData.CAN_NOT_SET_FIELD_MESSAGE);

  }

  @Test
  public void throws_an_error_if_the_contract_throws_an_exception()
      throws NoSuchFieldException, NoSuchMethodException {
    contractMethod =
        TestContract.class.getMethod(
            TestContractTestData.CONTRACT_PASSING_RETURN
        );

    when(contractRunnerData.getItField())
        .thenReturn(
            TestContract.class.getField(TestContractTestData.IT_FIELD_NAME)
        );
    when(contractRunnerData.getTestInstance()).thenReturn(contractInstance);
    final String message = "problem in contract code";
    doThrow(new IllegalArgumentException(message))
        .when(contractInstance)
        .contractPassingReturn();
    assertThrows(
        () -> contractCreationServiceImpl
            .createContract(
                contracts,
                contractMethod, contractRunnerData
            )
    )
        .assertException(IllegalArgumentException.class)
        .assertStackFileName(0, TestContractTestData.CLASS_FILE_NAME)
        .assertStackClass(0, TestContractTestData.FULL_CLASSNAME)
        .assertStackLineNumber(0, TestContractTestData.METHOD_LINE_NUMBER)
        .assertStackMethod(0, TestContractTestData.CONTRACT_PASSING_RETURN)
        .assertMessageIs(message);

  }

  @Test
  public void throws_an_error_if_the_contract_method_has_parameters()
      throws Throwable {
    contractMethod =
        TestContract.class.getMethod(
            TestContractTestData.METHOD_NAME,
            Object.class
        );

    when(contractRunnerData.getItField())
        .thenReturn(
            TestContract.class.getField(TestContractTestData.IT_FIELD_NAME)
        );
    when(contractRunnerData.getTestInstance()).thenReturn(contractInstance);
    assertThrows(
        () -> contractCreationServiceImpl
            .createContract(
                contracts,
                contractMethod, contractRunnerData
            )
    )
        .assertException(AssertionError.class)
        .assertStackFileName(0, TestContractTestData.CLASS_FILE_NAME)
        .assertStackClass(0, TestContractTestData.FULL_CLASSNAME)
        .assertStackLineNumber(0, TestContractTestData.METHOD_LINENUMBER)
        .assertStackMethod(0, TestContractTestData.METHOD_NAME)
        .assertMessageIs(
            TestContractTestData.WRONG_PARAMETERS_MESSAGE
        );

  }

  @Test
  public void throws_an_error_if_no_it_field_defined_in_suite_data() {
    assertThrows(
        () -> contractCreationServiceImpl
            .createContract(
                contracts,
                contractMethod, contractRunnerData
            )
    ).showStackTrace()
        .assertException(AssertionError.class)
        .assertStackFileName(0, TestContractTestData.CLASS_FILE_NAME)
        .assertStackClass(0, TestContractTestData.FULL_CLASSNAME)
        .assertStackLineNumber(0, TestContractTestData.METHOD_LINE_NUMBER)
        .assertStackMethod(0, TestContractTestData.CONTRACT_PASSING_RETURN)
        .assertMessageIs(ContractCreationServiceImpl.NO_IT_FIELD);
  }

}
