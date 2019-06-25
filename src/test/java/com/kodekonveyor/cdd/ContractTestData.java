package com.kodekonveyor.cdd;

import java.util.ArrayList;
import java.util.List;

import org.junit.runner.Description;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kodekonveyor.cdd.dto.ContractRunnerData;
import com.kodekonveyor.cdd.testartifacts.Data;
import com.kodekonveyor.cdd.testartifacts.ExampleService;
import com.kodekonveyor.cdd.testartifacts.TestContract;

import lombok.EqualsAndHashCode;

@Component
public class ContractTestData implements Specimen {

  @Autowired
  public ExampleService serviceInstance;

  @Autowired
  public TestContract testInstance;

  @Autowired
  public Data testData;

  public final String CONTRACT_PASSING_RETURN =
      "contract_passing_return";
  public Class<TestContract> contractClass = TestContract.class;
  public Description suiteDescription;
  public ContractInfo<ExampleService> contract;
  @EqualsAndHashCode.Exclude
  public List<ContractInfo<ExampleService>> contractList;
  public ContractRunnerData<ExampleService> contractRunnerData;
  public ContractRunnerData<ExampleService> contractRunnerDataEmpy;
  public Description testDescription;

  @Override
  public void init() {
    contract = new ContractInfo<ExampleService>(serviceInstance);
    contract.setDefiningFunction(CONTRACT_PASSING_RETURN);

    contractList = new ArrayList<>();
    contractList.add(contract);

    contractRunnerData = new ContractRunnerData<ExampleService>();
    contractRunnerData.setTestClass(contractClass);
    contractRunnerData.setServiceInstance(serviceInstance);
    contractRunnerData
        .setSuiteDescription(Description.createSuiteDescription(contractClass));
    contractRunnerData.setTestData(testData);
    contractRunnerData.setTestInstance(testInstance);
    contractRunnerData.setContracts(contractList);

    suiteDescription = Description.createSuiteDescription(TestContract.class);
    contractRunnerDataEmpy = new ContractRunnerData<ExampleService>();
    testDescription = Description
        .createTestDescription(TestContract.class, CONTRACT_PASSING_RETURN);
  }
}
