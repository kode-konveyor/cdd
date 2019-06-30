package com.kodekonveyor.cdd;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import com.kodekonveyor.cdd.annotations.ContractFactory;
import com.kodekonveyor.cdd.annotations.ContractRule;
import com.kodekonveyor.cdd.annotations.Subject;
import com.kodekonveyor.cdd.impl.ChildDescriptionServiceImpl;
import com.kodekonveyor.cdd.testartifacts.ExampleService;
import com.kodekonveyor.cdd.testdata.ContractTestData;

@RunWith(ContractRunner.class)
public class ChildDescriptionServiceContract {

  @Subject
  @Autowired
  public ChildDescriptionServiceImpl<
      ExampleService> childDescriptionServiceImpl;

  @Autowired
  public ContractTestData testData;

  @ContractFactory
  public ContractInfo<ChildDescriptionService<ExampleService>> it;

  @ContractRule(
    "describeChild uses the class and method name of the contract to describe a child"
  )
  public void describeChild_good_values() {

    it.returns(testData.testDescription)
        .describeChild(
            testData.contract,
            testData.contractRunnerData
        );

  }

}
