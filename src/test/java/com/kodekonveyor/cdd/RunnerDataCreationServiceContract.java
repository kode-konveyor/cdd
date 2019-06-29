package com.kodekonveyor.cdd;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import com.kodekonveyor.cdd.annotations.Contract;
import com.kodekonveyor.cdd.annotations.ContractFactory;
import com.kodekonveyor.cdd.annotations.Subject;
import com.kodekonveyor.cdd.impl.RunnerDataCreationServiceImpl;
import com.kodekonveyor.cdd.testartifacts.ExampleService;
import com.kodekonveyor.cdd.testdata.ContractTestData;

@RunWith(ContractRunner.class)
public class RunnerDataCreationServiceContract {

  @Subject
  @Autowired
  public RunnerDataCreationServiceImpl<
      ExampleService> runnerDataCreationServiceImpl;

  @ContractFactory
  public ContractInfo<RunnerDataCreationService<ExampleService>> it;

  @Autowired
  public ContractTestData testData;

  @Contract(
    "makeRunnerDataFromTestClass creates the data needed for the runner"
  )
  public void makeRunnerDataFromTestClass_good_values() throws Throwable {
    it.returns(testData.contractRunnerData)
        .makeRunnerDataFromTestClass(
            testData.contractInstance.getClass()
        );
  }
}
