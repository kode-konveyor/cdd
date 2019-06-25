package com.kodekonveyor.cdd;

import java.util.List;

import org.junit.runner.Description;
import org.springframework.stereotype.Component;

import com.kodekonveyor.cdd.dto.ContractRunnerData;
import com.kodekonveyor.cdd.testartifacts.Data;
import com.kodekonveyor.cdd.testartifacts.ExampleService;
import com.kodekonveyor.cdd.testartifacts.TestContract;

@Component
public class ContractTestData {

  ExampleService serviceInstance;

  public final String CONTRACT_PASSING_RETURN =
      "contract_passing_return";
  Class<TestContract> contractClass = TestContract.class;
  Data testData;
  public TestContract testInstance;
  public Description suiteDescription;
  public ContractInfo<ExampleService> contract;
  public List<ContractInfo<ExampleService>> contractList;
  public ContractRunnerData<ExampleService> contractRunnerData;
  public ContractRunnerData<ExampleService> contractRunnerDataEmpy;
  public Description testDescription;

}
