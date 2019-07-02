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

import com.kodekonveyor.cdd.dto.ContractRunnerData;
import com.kodekonveyor.cdd.impl.ContractCreationServiceImpl;
import com.kodekonveyor.cdd.impl.ContractInfoFactory;
import com.kodekonveyor.cdd.testartifacts.ExampleService;
import com.kodekonveyor.cdd.testartifacts.TestContract;
import com.kodekonveyor.cdd.testartifacts.TestContractNoIT;
import com.kodekonveyor.cdd.testdata.ContractTestData;

@RunWith(MockitoJUnitRunner.class)
public class ContractCreationServiceErrorCasesTest {

  private static final String CAN_NOT_SET_FIELD_MESSAGE =
      ".*Can not set .* field .*TestContract.it to .*";

  private static final String WRONG_PARAMETERS_MESSAGE =
      "java.lang.IllegalArgumentException: wrong number of arguments";

  private static final int EQUALS_LINENUMBER = 35;

  private static final String EQUALS_METHOD_NAME = "equals";

  private static final String CLASS_FILE_NAME = "TestContract.java";

  private static final String FULL_CLASSNAME =
      "com.kodekonveyor.cdd.testartifacts.TestContract";

  private static final int METHOD_LINE_NUMBER = 27;

  private static final String IT_FIELD_NAME = "it";

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
        TestContract.class.getMethod(ContractTestData.CONTRACT_PASSING_RETURN);

    when(contractInfoFactory.getObject())
        .thenReturn(contract);
    when(contractRunnerData.getTestInstance()).thenReturn(contractInstance);
    when(contractRunnerData.getServiceInstance())
        .thenReturn(exampleService);
  }

  @Test
  public void throws_an_error_if_cannot_write_it_field()
      throws NoSuchFieldException, SecurityException {
    when(contractRunnerData.getItField())
        .thenReturn(TestContract.class.getField(IT_FIELD_NAME));
    when(contractRunnerData.getTestInstance()).thenReturn(contractNOITInstance);
    assertThrows(
        () -> contractCreationServiceImpl
            .createContract(
                contracts,
                contractMethod, contractRunnerData
            )
    )
        .assertException(AssertionError.class)
        .assertStackFileName(0, CLASS_FILE_NAME)
        .assertStackClass(0, FULL_CLASSNAME)
        .assertStackLineNumber(0, METHOD_LINE_NUMBER)
        .assertStackMethod(0, ContractTestData.CONTRACT_PASSING_RETURN)
        .assertMessageMatches(CAN_NOT_SET_FIELD_MESSAGE);

  }

  @Test
  public void throws_an_error_if_the_contract_throws_an_exception()
      throws NoSuchFieldException, SecurityException, NoSuchMethodException {
    contractMethod =
        TestContract.class.getMethod(
            ContractTestData.CONTRACT_PASSING_RETURN
        );

    when(contractRunnerData.getItField())
        .thenReturn(TestContract.class.getField(IT_FIELD_NAME));
    when(contractRunnerData.getTestInstance()).thenReturn(contractInstance);
    String message = "problem in contract code";
    doThrow(new IllegalArgumentException(message))
        .when(contractInstance)
        .contract_passing_return();
    assertThrows(
        () -> contractCreationServiceImpl
            .createContract(
                contracts,
                contractMethod, contractRunnerData
            )
    )
        .assertException(IllegalArgumentException.class)
        .assertStackFileName(0, CLASS_FILE_NAME)
        .assertStackClass(0, FULL_CLASSNAME)
        .assertStackLineNumber(0, METHOD_LINE_NUMBER)
        .assertStackMethod(0, ContractTestData.CONTRACT_PASSING_RETURN)
        .assertMessageIs(message);

  }

  @Test
  public void throws_an_error_if_the_contract_method_has_parameters()
      throws NoSuchFieldException, SecurityException, NoSuchMethodException {
    contractMethod =
        TestContract.class.getMethod(
            EQUALS_METHOD_NAME,
            Object.class
        );

    when(contractRunnerData.getItField())
        .thenReturn(TestContract.class.getField(IT_FIELD_NAME));
    when(contractRunnerData.getTestInstance()).thenReturn(contractInstance);
    assertThrows(
        () -> contractCreationServiceImpl
            .createContract(
                contracts,
                contractMethod, contractRunnerData
            )
    )
        .showStackTrace()
        .assertException(AssertionError.class)
        .assertStackFileName(0, CLASS_FILE_NAME)
        .assertStackClass(0, FULL_CLASSNAME)
        .assertStackLineNumber(0, EQUALS_LINENUMBER)
        .assertStackMethod(0, EQUALS_METHOD_NAME)
        .assertMessageIs(
            WRONG_PARAMETERS_MESSAGE
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
    )
        .assertException(AssertionError.class)
        .assertStackFileName(0, CLASS_FILE_NAME)
        .assertStackClass(0, FULL_CLASSNAME)
        .assertStackLineNumber(0, METHOD_LINE_NUMBER)
        .assertStackMethod(0, ContractTestData.CONTRACT_PASSING_RETURN)
        .assertMessageIs(ContractCreationServiceImpl.NO_IT_FIELD);
  }

}
