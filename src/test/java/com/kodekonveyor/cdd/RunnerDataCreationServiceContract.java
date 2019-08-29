package com.kodekonveyor.cdd;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Method;
import java.util.Map;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import com.kodekonveyor.cdd.annotation.ContractFactory;
import com.kodekonveyor.cdd.annotation.ContractRule;
import com.kodekonveyor.cdd.annotation.ReturnDetail;
import com.kodekonveyor.cdd.annotation.Subject;
import com.kodekonveyor.cdd.build.impl.RunnerDataCreationServiceImpl;
import com.kodekonveyor.cdd.run.dto.ContractRunnerData;
import com.kodekonveyor.cdd.testartifacts.ExampleService;
import com.kodekonveyor.cdd.testdata.ContractTestData;

@RunWith(ContractRunner.class)
public class RunnerDataCreationServiceContract {

  @Subject
  @Autowired
  public RunnerDataCreationServiceImpl<
      ExampleService> runnerDataCreationServiceImpl;

  @ContractFactory
  public ContractInfo<RunnerDataCreationServiceImpl<ExampleService>> it; //NOPMD ShortVariable

  @Autowired
  public ContractTestData contractTestData;

  @ContractRule(
    "makeRunnerDataFromTestClass creates the data needed for the runner"
  )
  public void makeRunnerDataFromTestClassCreatesTheDataNeeded()
      throws Throwable {
    it.returns(contractTestData.contractRunnerData)
        .suchThat(
            "The test class is the class of the contract",
            "Return details are recorded",
            "The contract list contains contract for the tested service",
            "The suite description is the contract class name",
            "The service is the Subject of the contract",
            "The type of the it field is ContractInfo",
            "The it field is field of the contract",
            "The test instance is an instance of the contract"
        )
        .when()
        .makeRunnerDataFromTestClass(
            contractTestData.contractInstance.getClass()
        );
  }

  @ReturnDetail("The test class is the class of the contract")
  public void theTestClassIsTheClassOfTheContract(
      final ContractRunnerData<ExampleService> returnValue
  ) {
    assertEquals(
        contractTestData.contractInstance.getClass(), returnValue.getTestClass()
    );
  }

  @ReturnDetail("Return details are recorded")
  public void returnDetailAreRecorded(
      final ContractRunnerData<ExampleService> returnValue
  ) {
    final Map<String, Method> returnDetails =
        returnValue.getReturnValueContracts();
    assertEquals(
        TestContractTestData.RETURN_DETAIL_FUNCTION_NAME,
        returnDetails.get(TestContractTestData.RETURN_DEATIL_NAME).getName()
    );
  }

  @ReturnDetail(
    "The contract list contains contract for the tested service"
  )
  public void theContractLisContainsContract(
      final ContractRunnerData<ExampleService> returnValue
  ) {
    assertEquals(1, returnValue.getContracts().size());
    assertEquals(
        ExampleService.class, returnValue.getContracts().get(0).getService().getClass()
    );
  }

  @ReturnDetail("The suite description is the contract class name")
  public void suiteDescriptionIsTheContractClassName(
      final ContractRunnerData<ExampleService> returnValue
  ) {
    assertEquals(
        contractTestData.contractInstance.getClass().getName(),
        returnValue.getSuiteDescription().getDisplayName()
    );
  }

  @ReturnDetail("The service is the Subject of the contract")
  public void theServiceIsExampleService(
      final ContractRunnerData<ExampleService> returnValue
  ) {
    assertEquals(
        contractTestData.serviceInstance.getClass(),
        returnValue.getServiceInstance().getClass()
    );
  }

  @ReturnDetail("The type of the it field is ContractInfo")
  public void itFieldTypeIsContractInfo(
      final ContractRunnerData<ExampleService> returnValue
  ) {
    assertEquals(
        ContractInfo.class,
        returnValue.getItField().getType()
    );
  }

  @ReturnDetail("The it field is field of the contract")
  public void itFieldIsFieldOfTheContract(
      final ContractRunnerData<ExampleService> returnValue
  ) {
    assertEquals(
        contractTestData.contractInstance.getClass(),
        returnValue.getItField().getDeclaringClass()
    );
  }

  @ReturnDetail("The test instance is an instance of the contract")
  public void instanceClassEquals(
      final ContractRunnerData<ExampleService> returnValue
  ) {
    assertEquals(
        contractTestData.contractInstance.getClass(),
        returnValue.getTestInstance().getClass()
    );
  }

}
