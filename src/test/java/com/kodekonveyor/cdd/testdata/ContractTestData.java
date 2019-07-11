package com.kodekonveyor.cdd.testdata;

import java.util.List;

import javax.annotation.PostConstruct;

import org.junit.runner.Description;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kodekonveyor.cdd.ContractInfo;
import com.kodekonveyor.cdd.assemble.ContractInfoFactory;
import com.kodekonveyor.cdd.build.impl.RunnerDataCreationServiceImpl;
import com.kodekonveyor.cdd.run.dto.ContractRunnerData;
import com.kodekonveyor.cdd.testartifacts.ExampleService;
import com.kodekonveyor.cdd.testartifacts.TestContract;

@Component
public class ContractTestData {

  @Autowired
  public RunnerDataCreationServiceImpl<?> runnerDataCreationServiceImpl;

  @Autowired
  ContractInfoFactory<ExampleService> contractInfoFactory;

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

  @PostConstruct
  void initialize() throws NoSuchMethodException, SecurityException {

    contract = contractInfoFactory.getObject();
    contract.setService(serviceInstance);

    contractList = List.of(contract);

    contractRunnerData =
        testContractRunnerDataCreator.createTestContractRunnerData(
            contractInstance, contractList, serviceInstance
        );
    contract.setDefiningFunction(
        contractInstance.getClass()
            .getMethod(CONTRACT_PASSING_RETURN)
    );

  }

}
