package com.kodekonveyor.cdd.dto;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import com.kodekonveyor.cdd.ContractInfo;
import com.kodekonveyor.cdd.ContractRunner;
import com.kodekonveyor.cdd.ContractTestData;
import com.kodekonveyor.cdd.annotations.Contract;
import com.kodekonveyor.cdd.annotations.Sample;
import com.kodekonveyor.cdd.annotations.TestData;
import com.kodekonveyor.cdd.testartifacts.ExampleService;

@RunWith(ContractRunner.class)
public class ContractRunnerDataContract {

  @TestData
  @Autowired
  public ContractTestData contractTestData;

  @Sample
  public ContractRunnerData<ExampleService>
      getSample(ContractTestData testData) {
    return testData.contractRunnerData;
  }

  @Contract("getServiceInstance returns the serviceInstance")
  public void serviceInstance_is_contractRunnerData(
      final ContractTestData testData,
      final ContractInfo<ContractRunnerData<?>> contract
  ) {
    contract.returns(testData.serviceInstance)
        .getServiceInstance();
  }

  @Contract("getSuiteDescription returns the suite description")
  public void suiteDescription(
      final ContractTestData testData,
      final ContractInfo<ContractRunnerData<?>> contract
  ) {
    contract.returns(testData.suiteDescription).getSuiteDescription();
  }

  @Contract("getTestClass returns the contract class")
  public void getTestClass(
      final ContractTestData testData,
      final ContractInfo<ContractRunnerData<?>> contract
  ) {
    contract.returns(testData.contractClass).getTestClass();
  }

  @Contract("getTestData returns the test data")
  public void getTestData(
      final ContractTestData testData,
      final ContractInfo<ContractRunnerData<?>> contract
  ) {
    contract.returns(testData.testData).getTestData();
  }

  @Contract("getTestInstance returns the test instance")
  public void getTestInstance(
      final ContractTestData testData,
      final ContractInfo<ContractRunnerData<?>> contract
  ) {
    contract.returns(testData.testInstance).getTestInstance();
  }

  //@Contract("getContracts returns the contract list")
  public void getContracts(
      final ContractTestData testData,
      final ContractInfo<ContractRunnerData<?>> contract
  ) {
    contract.returns(testData.contractList).getContracts();
  }
}
