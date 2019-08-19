package com.kodekonveyor.cdd;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import com.kodekonveyor.cdd.annotation.ContractFactory;
import com.kodekonveyor.cdd.annotation.ContractRule;
import com.kodekonveyor.cdd.annotation.Subject;
import com.kodekonveyor.cdd.build.impl.RunnerDataCreationServiceImpl;
import com.kodekonveyor.cdd.run.dto.ContractRunnerData;
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
        .withReturnPredicate((self, other) -> contractRunnerDataEquals(self, other))
        .when()
        .makeRunnerDataFromTestClass(
            contractTestData.contractInstance.getClass()
        );
  }

  @SuppressWarnings("unchecked")
  private boolean contractRunnerDataEquals(
      final Object self,
      final Object other
  ) {
    final ContractRunnerData<ExampleService> returnValue =
        (ContractRunnerData<ExampleService>) self;
    System.out.println(returnValue.getTestClass());
    final ContractRunnerData<ExampleService> otherValue =
        (ContractRunnerData<ExampleService>) other;
    System.out.println(otherValue.getTestClass());

    return self.getClass().equals(other.getClass()) &&
        returnValue.getTestInstance().equals(otherValue.getTestInstance()) &&
        returnValue.getItField().equals(otherValue.getItField()) &&
        returnValue.getServiceInstance().getClass()
            .equals(otherValue.getServiceInstance().getClass()) &&
        returnValue.getSuiteDescription()
            .equals(otherValue.getSuiteDescription()) &&
        returnValue.getContracts().get(0).getDefiningFunction()
            .equals(otherValue.getContracts().get(0).getDefiningFunction()) &&
        returnValue.getTestClass().equals(otherValue.getTestClass());
  }

}
