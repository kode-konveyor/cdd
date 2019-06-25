package com.kodekonveyor.cdd.testartifacts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import com.kodekonveyor.cdd.ContractInfo;
import com.kodekonveyor.cdd.annotations.Contract;
import com.kodekonveyor.cdd.annotations.Subject;
import com.kodekonveyor.cdd.annotations.TestData;;

@SpringBootTest
@Component
public class TestContract {

  @Subject
  @Autowired
  public ExampleService service;

  @TestData
  @Autowired
  public TestArtifactDataFactory testdataFactory;

  @SpringBootApplication
  static class TestConfiguration {
  }

  @Contract("a contract with return definition")
  public void contract_passing_return(
      final Data testData,
      final ContractInfo<ExampleService> contract
  ) {

    contract.returns(
        testData.goodReturnValue
    )
        .testedMethod(testData.goodParameter);
  }

}
