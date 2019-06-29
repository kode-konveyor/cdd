package com.kodekonveyor.cdd.dto;

import javax.annotation.PostConstruct;

import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import com.kodekonveyor.cdd.ContractInfo;
import com.kodekonveyor.cdd.ContractRunner;
import com.kodekonveyor.cdd.annotations.Contract;
import com.kodekonveyor.cdd.annotations.ContractFactory;
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

  @Contract("getSuiteDescription returns the suite description")
  public void suiteDescription() {
    it.returns(
        Description.createSuiteDescription(testData.contractInstance.getClass())
    )
        .getSuiteDescription();
  }

  @Contract("getTestClass returns the contract class")
  public void getTestClass() {
    it.returns(testData.contractInstance.getClass()).getTestClass();
  }

  @Contract("getTestInstance returns the test instance")
  public void getTestInstance() {
    it.returns(testData.contractInstance).getTestInstance();
  }

  @Contract("getItField returns the It field")
  public void getItField() throws NoSuchFieldException, SecurityException {
    it.returns(testData.contractInstance.getClass().getDeclaredField("it"))
        .getItField();
  }

  @Contract("getServiceInstance returns the service instance")
  public void getServiceInstance()
      throws NoSuchFieldException, SecurityException {
    it.returns(testData.serviceInstance)
        .getServiceInstance();
  }

  @Contract("getContracts returns the contract list")
  public void getContracts() {
    it.returns(testData.contractList).getContracts();
  }
}
