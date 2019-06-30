package com.kodekonveyor.cdd.dto;

import javax.annotation.PostConstruct;

import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import com.kodekonveyor.cdd.ContractInfo;
import com.kodekonveyor.cdd.ContractRunner;
import com.kodekonveyor.cdd.annotations.ContractFactory;
import com.kodekonveyor.cdd.annotations.ContractRule;
import com.kodekonveyor.cdd.annotations.Subject;
import com.kodekonveyor.cdd.testartifacts.ExampleService;
import com.kodekonveyor.cdd.testdata.ContractTestData;

@RunWith(ContractRunner.class)
public class ContractRunnerDataContract {

  @ContractFactory
  public ContractInfo<ContractRunnerData<ExampleService>> it;

  @Autowired
  public ContractTestData testData;

  @Subject
  public ContractRunnerData<ExampleService> foo;

  @PostConstruct
  void initialize() {
    foo = testData.contractRunnerData;
  }

  @ContractRule("getSuiteDescription returns the suite description")
  public void suiteDescription() {
    it.returns(
        Description.createSuiteDescription(testData.contractInstance.getClass())
    )
        .getSuiteDescription();
  }

  @ContractRule("getTestClass returns the contract class")
  public void getTestClass() {
    it.returns(testData.contractInstance.getClass()).getTestClass();
  }

  @ContractRule("getTestInstance returns the test instance")
  public void getTestInstance() {
    it.returns(testData.contractInstance).getTestInstance();
  }

  @ContractRule("getItField returns the It field")
  public void getItField() throws NoSuchFieldException, SecurityException {
    it.returns(testData.contractInstance.getClass().getDeclaredField("it"))
        .getItField();
  }

  @ContractRule("getServiceInstance returns the service instance")
  public void getServiceInstance()
      throws NoSuchFieldException, SecurityException {
    it.returns(testData.serviceInstance)
        .getServiceInstance();
  }

  @ContractRule("getContracts returns the contract list")
  public void getContracts() {
    it.returns(testData.contractList).getContracts();
  }
}
