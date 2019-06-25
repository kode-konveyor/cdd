package com.kodekonveyor.cdd;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import com.kodekonveyor.cdd.annotations.Contract;
import com.kodekonveyor.cdd.annotations.Subject;
import com.kodekonveyor.cdd.annotations.TestData;
import com.kodekonveyor.cdd.dto.ContractRunnerData;
import com.kodekonveyor.cdd.impl.RunnerDataCreationServiceImpl;
import com.kodekonveyor.cdd.testartifacts.ExampleService;

@RunWith(ContractRunner.class)
public class RunnerDataCreationServiceContract {

  @Subject
  @Autowired
  public RunnerDataCreationServiceImpl<
      ExampleService> runnerDataCreationServiceImpl;

  @TestData
  @Autowired
  public TestDataFactory testdataFactory;

  @Contract(
    "makeRunnerDataFromTestClass creates the data needed for the runner"
  )
  public void makeRunnerDataFromTestClass_good_values(
      final ContractTestData testData,
      final ContractInfo<RunnerDataCreationService<ExampleService>> contract
  ) throws Throwable {
    ContractRunnerData<ExampleService> returnValue =
        testData.contractRunnerData;

    contract.returns(returnValue, ContractRunnerDataContract.class)
        .makeRunnerDataFromTestClass(testData.testInstance.getClass());
  }
}
