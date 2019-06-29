package com.kodekonveyor.cdd.testdata;

import java.util.List;

import org.junit.runner.Description;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kodekonveyor.cdd.ContractInfo;
import com.kodekonveyor.cdd.dto.ContractRunnerData;
import com.kodekonveyor.cdd.impl.RunnerDataCreationServiceImpl;
import com.kodekonveyor.cdd.testartifacts.ExampleService;
import com.kodekonveyor.cdd.testartifacts.TestContract;

@Component
public class ContractTestData {

  @Autowired
  public RunnerDataCreationServiceImpl<?> runnerDataCreationServiceImpl;

  @Autowired
  TestContractRunnerDataCreator testContractRunnerDataCreator =
      new TestContractRunnerDataCreator();
  public final static String CONTRACT_PASSING_RETURN =
      "contract_passing_return";

  public ExampleService serviceInstance = new ExampleService();

  public TestContract contractInstance = new TestContract();

  public Description testDescription = Description
      .createTestDescription(
          TestContract.class,
          CONTRACT_PASSING_RETURN
      );

  public ContractInfo<ExampleService> contract;

  public List<ContractInfo<ExampleService>> contractList;

  public ContractRunnerData<ExampleService> contractRunnerData;

  public ContractRunnerData<ExampleService> contractRunnerDataEmpy =
      new ContractRunnerData<ExampleService>();

  public ContractTestData() {

    contract = new ContractInfo<ExampleService>(serviceInstance);
    contract.setDefiningFunction(CONTRACT_PASSING_RETURN);
    contract.runnerDataCreationServiceImpl = runnerDataCreationServiceImpl;

    contractList = List.of(contract);

    contractRunnerData =
        testContractRunnerDataCreator.createTestContractRunnerData(
            contractInstance, contractList, serviceInstance
        );

  }

}
