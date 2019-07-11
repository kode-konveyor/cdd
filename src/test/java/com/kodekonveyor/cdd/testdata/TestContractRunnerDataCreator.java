package com.kodekonveyor.cdd.testdata;

import java.util.List;

import org.junit.runner.Description;
import org.springframework.stereotype.Service;

import com.kodekonveyor.cdd.ContractInfo;
import com.kodekonveyor.cdd.run.dto.ContractRunnerData;
import com.kodekonveyor.cdd.testartifacts.ExampleService;
import com.kodekonveyor.cdd.testartifacts.TestContract;

@Service
public class TestContractRunnerDataCreator {

  public ContractRunnerData<ExampleService> createTestContractRunnerData(
      TestContract contractInstance,
      List<ContractInfo<ExampleService>> contractList,
      ExampleService serviceInstance
  ) {
    ContractRunnerData<ExampleService> contractRunnerData =
        new ContractRunnerData<ExampleService>();

    Class<? extends Object> contractClass =
        contractInstance.getClass();
    contractRunnerData.setTestClass(contractClass);
    contractRunnerData
        .setSuiteDescription(Description.createSuiteDescription(contractClass));
    contractRunnerData.setTestInstance(contractInstance);
    contractRunnerData.setContracts(contractList);
    contractRunnerData.setServiceInstance(serviceInstance);
    try {
      contractRunnerData.setItField(contractClass.getDeclaredField("it"));
    } catch (NoSuchFieldException | SecurityException e) {
      throw new IllegalArgumentException(
          "it field cannot be extracted from " + contractClass
      );
    }
    return contractRunnerData;
  }

}
