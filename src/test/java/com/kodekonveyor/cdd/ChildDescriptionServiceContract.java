package com.kodekonveyor.cdd;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

import com.kodekonveyor.cdd.annotations.Contract;
import com.kodekonveyor.cdd.annotations.Subject;
import com.kodekonveyor.cdd.annotations.TestData;
import com.kodekonveyor.cdd.impl.ChildDescriptionServiceImpl;
import com.kodekonveyor.cdd.testartifacts.ExampleService;

@RunWith(ContractRunner.class)
@SpringBootTest
public class ChildDescriptionServiceContract {

  @Subject
  @Autowired
  public ChildDescriptionServiceImpl<
      ExampleService> childDescriptionServiceImpl;

  @TestData
  @Autowired
  public ContractTestData contractTestData;

  @SpringBootApplication
  static class TestConfiguration {
  }

  @Contract(
    "describeChild uses the class and method name of the contract to describe a child"
  )
  public void describeChild_good_values(
      final ContractTestData testData,
      final ContractInfo<ChildDescriptionService<ExampleService>> contract
  ) {
    contract.returns(testData.testDescription)
        .describeChild(
            testData.contract,
            testData.contractRunnerData
        );
  }

}
