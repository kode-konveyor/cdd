package com.kodekonveyor.cdd.testdata;

import java.util.List;

import org.junit.runner.Description;
import org.springframework.stereotype.Service;

import com.kodekonveyor.cdd.ContractInfo;
import com.kodekonveyor.cdd.TestContractTestData;
import com.kodekonveyor.cdd.run.dto.ContractRunnerData;
import com.kodekonveyor.cdd.testartifacts.ExampleService;
import com.kodekonveyor.cdd.testartifacts.TestContract;

@Service
public class TestContractRunnerDataCreator {

  public ContractRunnerData<ExampleService> createTestContractRunnerData(
      final TestContract contractInstance,
      final List<ContractInfo<ExampleService>> contractList,
      final ExampleService serviceInstance
  ) throws NoSuchMethodException {
    final ContractRunnerData<ExampleService> contractRunnerData =
        new ContractRunnerData<>();

    final Class<? extends Object> contractClass =
        contractInstance.getClass();
    contractRunnerData.setTestClass(contractClass);
    contractRunnerData
        .setSuiteDescription(Description.createSuiteDescription(contractClass));
    contractRunnerData.setTestInstance(contractInstance);
    contractRunnerData.setContracts(contractList);
    contractRunnerData.setServiceInstance(serviceInstance);
    contractRunnerData.getReturnValueContracts()
        .put(
            TestContractTestData.RETURN_DEATIL_NAME,
            TestContract.class.getMethod(
                TestContractTestData.RETURN_DETAIL_FUNCTION_NAME, Integer.class
            )
        );
    try {
      contractRunnerData.setItField(contractClass.getDeclaredField("it"));
    } catch (NoSuchFieldException | SecurityException e) {
      throw new IllegalArgumentException(
          "it field cannot be extracted from " + contractClass, e
      );
    }
    return contractRunnerData;
  }

}
