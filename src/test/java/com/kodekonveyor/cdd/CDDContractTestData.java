package com.kodekonveyor.cdd;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.junit.runner.Description;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.kodekonveyor.cdd.run.dto.ContractRunnerData;
import com.kodekonveyor.cdd.testartifacts.ExampleService;
import com.kodekonveyor.cdd.testartifacts.TestContract;
import com.kodekonveyor.cdd.testdata.ContractTestData;

public class CDDContractTestData {//NOPMD ClassNamingConventions

  public static final Description DESCRIPTION = mock(Description.class);

  public static final ContractRunnerData<ExampleService> DATA =
      mockContractRunnerData();
  public static final ContractRunnerData<ExampleService> BAD_PARAMETERS_DATA =
      mockContractRunnerData();
  public static final ContractRunnerData<
      ExampleService> RETURNING_SERVICE_WITH_THROWING_STUB_DATA =
          mockContractRunnerData();

  public static final ContractInfo<ExampleService> THROWING_CONTRACT =
      createThrowingContract();
  public static final ContractInfo<
      ExampleService> NOT_THROWING_THROWING_CONTRACT =
          createNotThrowingThrowingContract();
  public static final ContractInfo<
      ExampleService> THROWING_CONTRACT_THROWING_ANOTHER_EXCEPTION =
          createThrowinContractThrowingAnotherException();
  public static final ContractInfo<
      ExampleService> OTHER_MESSAGE_THROWING_CONTRACT =
          createOtherMessageThrowingContract();
  public static final ContractInfo<
      ExampleService> RETURNING_CONTRACT_WITH_THROWING_STUB =
          createReturningContractWithThrowingStub();
  public static final ContractInfo<
      ExampleService> RETURNING_CONTRACT_WITH_THROWING_SERVICE =
          createReturningContractWithThrowingService();

  public static final ContractInfo<
      ExampleService> RETURNING_CONTRACT_CALLED_WITH_BAD_PARAMETERS =
          createReturningContractCalledWithBadParameters();

  public static final ContractInfo<
      ExampleService> RETURNING_CONTRACT_RETURNING_NULL =
          createNullReturningContract();

  public static final ContractInfo<
      ExampleService> RETURNING_CONTRACT_WITH_FAILING_PREDICATE =
          createReturningContractWithFailingReturnPredicate();

  public static final ContractInfo<
      ExampleService> CONTRACT_EXPECTING_NULL_RETURN_BUT_SERVICE_RETURNING_OTHER_VALUE =
          createContractExpectingNullButServiceReturningOtherValue();

  @SuppressWarnings("unchecked")
  private static ContractRunnerData<ExampleService> mockContractRunnerData() {
    return mock(ContractRunnerData.class);
  }

  private static ContractInfo<ExampleService> createThrowingContract() {
    final ContractInfo<ExampleService> throwingContract = new ContractInfo<>();
    final ExampleService throwingExampleService = mock(ExampleService.class);
    final IllegalArgumentException exception =
        new IllegalArgumentException(TestContractTestData.BAD_PARAMETER);
    final ExampleService stub = doThrow(exception).when(throwingExampleService);
    stub.testedMethod(1);
    throwingContract.setStub(stub);
    throwingContract.setExceptionClass(IllegalArgumentException.class);
    throwingContract.setExceptionMessage(TestContractTestData.BAD_PARAMETER);
    return throwingContract;
  }

  private static ContractInfo<ExampleService>
      createNotThrowingThrowingContract() {
    final ContractInfo<ExampleService> notThrowingThrowingContract =
        new ContractInfo<>();
    final ExampleService exampleService = mock(ExampleService.class);
    final ExampleService stub =
        doReturn(TestContractTestData.GOOD_RETURN_VALUE).when(exampleService);
    stub.testedMethod(TestContractTestData.GOOD_PARAMETER);
    notThrowingThrowingContract
        .setExceptionClass(IllegalArgumentException.class);
    notThrowingThrowingContract.setStub(stub);
    return notThrowingThrowingContract;
  }

  private static ContractInfo<ExampleService>
      createThrowinContractThrowingAnotherException() {
    final ContractInfo<
        ExampleService> throwingContractThrowingAnotherException =
            new ContractInfo<>();
    final ExampleService exampleService = mock(ExampleService.class);
    final ExampleService stub =
        doThrow(new IllegalArgumentException(TestContractTestData.BAD_PARAMETER)).when(exampleService);
    stub.testedMethod(1);
    throwingContractThrowingAnotherException
        .setExceptionClass(ArithmeticException.class);
    throwingContractThrowingAnotherException
        .setExceptionMessage(TestContractTestData.BAD_PARAMETER);
    throwingContractThrowingAnotherException.setStub(stub);
    return throwingContractThrowingAnotherException;
  }

  private static ContractInfo<ExampleService>
      createOtherMessageThrowingContract() {
    final ContractInfo<ExampleService> otherMessageThrowingContract =
        new ContractInfo<>();
    final ExampleService exampleService = mock(ExampleService.class);
    final ExampleService stub =
        doThrow(new IllegalArgumentException(TestContractTestData.BAD_PARAMETER)).when(exampleService);
    stub.testedMethod(1);
    otherMessageThrowingContract
        .setExceptionClass(IllegalArgumentException.class);
    otherMessageThrowingContract.setExceptionMessage("bad parameter: 42");
    otherMessageThrowingContract.setStub(stub);
    return otherMessageThrowingContract;
  }

  private static ContractInfo<ExampleService>
      createReturningContractWithThrowingStub() {
    final ContractInfo<ExampleService> returningContractWithThrowingService =
        new ContractInfo<>();
    final ExampleService exampleService = mock(ExampleService.class);
    final ExampleService stub =
        doThrow(new IllegalArgumentException(TestContractTestData.BAD_PARAMETER)).when(exampleService);
    stub.testedMethod(1);
    returningContractWithThrowingService.setStub(stub);
    return returningContractWithThrowingService;
  }

  private static ContractInfo<ExampleService>
      createReturningContractWithThrowingService() {
    final ContractInfo<ExampleService> returningContractWithThrowingService =
        new ContractInfo<>();
    final ExampleService exampleService =
        mock(ExampleService.class);

    final ExampleService stub =
        doReturn(TestContractTestData.GOOD_RETURN_VALUE).when(exampleService);
    stub.testedMethod(1);
    returningContractWithThrowingService.setStub(stub);
    doThrow(new IllegalArgumentException(TestContractTestData.BAD_PARAMETER))
        .when(exampleService).testedMethod(1);
    returningContractWithThrowingService
        .setSuiteData(RETURNING_SERVICE_WITH_THROWING_STUB_DATA);
    doReturn(exampleService)
        .when(RETURNING_SERVICE_WITH_THROWING_STUB_DATA)
        .getServiceInstance();

    return returningContractWithThrowingService;
  }

  private static ContractInfo<ExampleService>
      createReturningContractCalledWithBadParameters() {
    final ContractInfo<
        ExampleService> returningContractCalledWithBadParameters =
            new ContractInfo<>();
    final ExampleService exampleService = mock(ExampleService.class);
    final ExampleService stub =
        doReturn(TestContractTestData.GOOD_RETURN_VALUE).when(exampleService);
    stub.testedMethod(1);
    returningContractCalledWithBadParameters.setStub(stub);
    try {
      returningContractCalledWithBadParameters.setDefiningFunction(
          TestContract.class
              .getMethod(TestContractTestData.CONTRACT_PASSING_RETURN)
      );
    } catch (NoSuchMethodException | SecurityException e) {
      throw new AssertionError(e);
    }
    returningContractCalledWithBadParameters
        .setSuiteData(BAD_PARAMETERS_DATA);

    doReturn(new ExampleService()).when(BAD_PARAMETERS_DATA)
        .getServiceInstance();

    return returningContractCalledWithBadParameters;
  }

  private static ContractInfo<ExampleService> createNullReturningContract() {
    final ContractInfo<ExampleService> nullReturningContract =
        new ContractInfo<>();
    final ContractRunnerData<ExampleService> data = mockContractRunnerData();

    final ExampleService exampleService = mock(ExampleService.class);
    final ExampleService stub =
        doReturn(null).when(exampleService);
    stub.testedMethod(TestContractTestData.GOOD_PARAMETER);
    nullReturningContract.setStub(stub);
    nullReturningContract.setSuiteData(data);
    doReturn(exampleService).when(data)
        .getServiceInstance();
    doReturn(null).when(exampleService)
        .testedMethod(TestContractTestData.GOOD_PARAMETER);
    return nullReturningContract;
  }

  private static ContractInfo<ExampleService>
      createReturningContractWithFailingReturnPredicate() {
    final ConfigurableApplicationContext ctx =
        new ClassPathXmlApplicationContext("applicationContext.xml");
    final ContractTestData contractTestData =
        (ContractTestData) ctx.getBean("contractTestData");
    ctx.close();
    final ExampleService exampleService = mock(ExampleService.class);

    final ExampleService stub =
        doReturn(null).when(exampleService);
    stub.testedMethod(TestContractTestData.GOOD_PARAMETER);
    final ContractInfo<ExampleService> contract = new ContractInfo<>();
    contract.setStub(stub);
    contract.setSuiteData(CDDContractTestData.DATA);
    contract.setReturnDetailChecks(
        List.of(TestContractTestData.RETURN_DEATIL_NAME)
    );
    final Map<String, Method> returnDetails =
        contractTestData.contractRunnerData.getReturnValueContracts();
    doReturn(returnDetails)
        .when(CDDContractTestData.DATA).getReturnValueContracts();
    doReturn(exampleService).when(CDDContractTestData.DATA)
        .getServiceInstance();
    doReturn(new TestContract()).when(CDDContractTestData.DATA)
        .getTestInstance();
    doReturn(returnDetails).when(CDDContractTestData.DATA)
        .getReturnValueContracts();
    doReturn(null).when(exampleService)
        .testedMethod(TestContractTestData.GOOD_PARAMETER);
    return contract;
  }

  private static ContractInfo<ExampleService>
      createContractExpectingNullButServiceReturningOtherValue() {
    final ContractInfo<
        ExampleService> contractExpectingNullButServiceReturningOtherValue =
            new ContractInfo<>();
    final ExampleService exampleService = mock(ExampleService.class);
    final ExampleService stub =
        doReturn(null).when(exampleService);
    stub.testedMethod(TestContractTestData.GOOD_PARAMETER);
    contractExpectingNullButServiceReturningOtherValue.setStub(stub);
    contractExpectingNullButServiceReturningOtherValue
        .setSuiteData(DATA);
    try {
      contractExpectingNullButServiceReturningOtherValue.setDefiningFunction(
          TestContract.class
              .getMethod(TestContractTestData.CONTRACT_PASSING_RETURN)
      );
    } catch (NoSuchMethodException | SecurityException e) {
      throw new AssertionError(e);
    }
    doReturn(exampleService).when(DATA)
        .getServiceInstance();
    doReturn(TestContractTestData.GOOD_RETURN_VALUE + 1).when(exampleService)
        .testedMethod(TestContractTestData.GOOD_PARAMETER);
    return contractExpectingNullButServiceReturningOtherValue;
  }

}
