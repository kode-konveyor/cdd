package com.kodekonveyor.cdd;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import com.kodekonveyor.cdd.annotation.ContractFactory;
import com.kodekonveyor.cdd.annotation.ContractRule;
import com.kodekonveyor.cdd.annotation.Subject;
import com.kodekonveyor.cdd.build.impl.RunnerDataCreationServiceImpl;
import com.kodekonveyor.cdd.testartifacts.ExampleService;
import com.kodekonveyor.cdd.testdata.ContractTestData;

@RunWith(ContractRunner.class)
public class RunnerDataCreationServiceContract {

  @Subject
  @Autowired
  public RunnerDataCreationServiceImpl<
      ExampleService> runnerDataCreationServiceImpl;

  @ContractFactory
  public ContractInfo<RunnerDataCreationServiceImpl<ExampleService>> it;

  @Autowired
  public ContractTestData contractTestData;

  @ContractRule(
    "makeRunnerDataFromTestClass creates the data needed for the runner"
  )
  public void makeRunnerDataFromTestClass_good_values() throws Throwable {
    it.returns(contractTestData.contractRunnerData)
        .withReturnPredicate((self, other) -> (self.getClass() == other.getClass()))
        .when()
        .makeRunnerDataFromTestClass(
            contractTestData.contractInstance.getClass()
        );
  }

}
