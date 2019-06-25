package com.kodekonveyor.cdd;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import com.kodekonveyor.cdd.annotations.Contract;
import com.kodekonveyor.cdd.annotations.Sample;
import com.kodekonveyor.cdd.annotations.Subject;
import com.kodekonveyor.cdd.annotations.TestData;
import com.kodekonveyor.cdd.dto.ContractRunnerData;
import com.kodekonveyor.cdd.testartifacts.ExampleService;

@RunWith(ContractRunner.class)
public class ContractRunnerDataContract {

  @Subject
  public ContractRunnerData<ExampleService> testData;

  @TestData
  @Autowired
  public TestDataFactory testdataFactory;

  @Sample
  public ContractRunnerData<ExampleService>
      getSample(final ContractTestData testData) {
    return testData.contractRunnerData;
  }

  @Contract("getServiceInstance returns the serviceInstance")
  public void serviceInstance_is_contractRunnerData(
      final ContractTestData testData,
      final ContractInfo<ContractRunnerData<?>> contract
  ) {
    contract.returnsClassOf(testData.serviceInstance)
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

  //@Contract("getTestData returns the test data")
  public void getTestData(
      final ContractTestData testData,
      final ContractInfo<ContractRunnerData<?>> contract
  ) {
    contract.returns(testData).getTestData();
  }

  //@Contract("getTestInstance returns the test instance")
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
