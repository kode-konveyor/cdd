package com.kodekonveyor.cdd.dto;

import javax.annotation.PostConstruct;

import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import com.kodekonveyor.cdd.ContractInfo;
import com.kodekonveyor.cdd.ContractRunner;
import com.kodekonveyor.cdd.annotation.ContractFactory;
import com.kodekonveyor.cdd.annotation.ContractRule;
import com.kodekonveyor.cdd.annotation.Subject;
import com.kodekonveyor.cdd.run.dto.ContractRunnerData;
import com.kodekonveyor.cdd.testartifacts.ExampleService;
import com.kodekonveyor.cdd.testdata.ContractTestData;

@RunWith(ContractRunner.class)
public class ContractRunnerDataContract {

  @ContractFactory
  public ContractInfo<ContractRunnerData<ExampleService>> it; //NOPMD ShortVariable

  @Autowired
  public ContractTestData contractTestData;

  @Subject
  public ContractRunnerData<ExampleService> foo;

  @PostConstruct
  private void initialize() {
    foo = contractTestData.contractRunnerData;
  }

  @ContractRule("getSuiteDescription returns the suite description")
  public void suiteDescription() {
    it.returns(
        Description
            .createSuiteDescription(contractTestData.contractInstance.getClass())
    ).when()
        .getSuiteDescription();
  }

  @ContractRule("getTestClass returns the contract class")
  public void checkGetTestClass() {
    it.returns(contractTestData.contractInstance.getClass()).when()
        .getTestClass();
  }

  @ContractRule("getTestInstance returns the test instance")
  public void checkGetTestInstance() {
    it.returns(contractTestData.contractInstance).when().getTestInstance();
  }

  @ContractRule("getItField returns the It field")
  public void checkGetItField() throws NoSuchFieldException {
    it.returns(
        contractTestData.contractInstance.getClass().getDeclaredField("it")
    ).when()
        .getItField();
  }

  @ContractRule("getServiceInstance returns the service instance")
  public void checkGetServiceInstance()
      throws NoSuchFieldException {
    it.returns(contractTestData.serviceInstance).when()
        .getServiceInstance();
  }

  @ContractRule("getContracts returns the contract list")
  public void checkGetContracts() {
    it.returns(contractTestData.contractList).when().getContracts();
  }
}
