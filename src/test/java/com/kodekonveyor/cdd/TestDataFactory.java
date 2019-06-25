package com.kodekonveyor.cdd;

import java.util.ArrayList;

import org.junit.runner.Description;
import org.springframework.stereotype.Service;

import com.kodekonveyor.cdd.dto.ContractRunnerData;
import com.kodekonveyor.cdd.impl.DataFactory;
import com.kodekonveyor.cdd.testartifacts.Data;
import com.kodekonveyor.cdd.testartifacts.ExampleService;
import com.kodekonveyor.cdd.testartifacts.TestContract;

@Service
public class TestDataFactory implements DataFactory {

  @Override
  public ContractTestData createTestData() {

    ContractTestData testData = new ContractTestData();
    testData.serviceInstance = new ExampleService();
    testData.testInstance = new TestContract();
    testData.testData = new Data();
    testData.contract =
        new ContractInfo<ExampleService>(testData.serviceInstance);
    testData.contract
        .setDefiningFunction(testData.CONTRACT_PASSING_RETURN);

    testData.contractList = new ArrayList<>();
    testData.contractList.add(testData.contract);

    testData.contractRunnerData =
        new ContractRunnerData<ExampleService>();
    testData.contractRunnerData.setTestClass(testData.contractClass);
    testData.contractRunnerData.setServiceInstance(testData.serviceInstance);
    testData.contractRunnerData
        .setSuiteDescription(
            Description.createSuiteDescription(testData.contractClass)
        );
    testData.contractRunnerData.setTestData(testData);
    testData.contractRunnerData.setTestInstance(testData.testInstance);
    testData.contractRunnerData.setContracts(testData.contractList);

    testData.suiteDescription =
        Description.createSuiteDescription(TestContract.class);
    testData.contractRunnerDataEmpy = new ContractRunnerData<ExampleService>();
    testData.testDescription = Description.createTestDescription(
        TestContract.class, testData.CONTRACT_PASSING_RETURN
    );
    return testData;
  }

}
