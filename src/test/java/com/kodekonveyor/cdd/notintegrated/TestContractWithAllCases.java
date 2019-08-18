package com.kodekonveyor.cdd.notintegrated;

import org.mockito.InjectMocks;

import com.kodekonveyor.cdd.ContractInfo;
import com.kodekonveyor.cdd.annotation.ContractRule;
import com.kodekonveyor.cdd.annotation.Subject;
import com.kodekonveyor.cdd.testartifacts.Data;
import com.kodekonveyor.cdd.testartifacts.ExampleService;;

public class TestContractWithAllCases {

  @Subject
  @InjectMocks
  public ExampleService service;

  @InjectMocks
  public Data testdata;

  @ContractRule("a contract with return definition")
  public void contract_passing_return(
      final Data testData,
      final ContractInfo<ExampleService> contract
  ) {

    contract.returns(
        testData.goodReturnValue
    ).when()
        .testedMethod(testData.goodParameter);
  }

  @ContractRule("a failing contract with return definition")
  public void contract_failing_return(
      final Data testData,
      final ContractInfo<ExampleService> contract
  ) {

    contract.returns(
        testData.illegalOutputValue
    ).when()
        .testedMethod(testData.goodParameter);
  }

  @ContractRule("a contract with exception definition")
  public void contract_passing_exception(
      final Data testData,
      final ContractInfo<ExampleService> contract
  ) {

    contract.throwing(
        Data.exceptionThrown,
        testData.exceptionMessage
    ).when()
        .testedMethod(testData.parameterInducingException);
  }

  @ContractRule("a contract failing test by not throwing exception")
  public void contract_fail_no_exception(
      final Data testData,
      final ContractInfo<ExampleService> contract
  ) {

    contract.throwing(
        Data.exceptionThrown,
        testData.exceptionMessage
    ).when()
        .testedMethod(testData.goodParameter);
  }

  @ContractRule("a contract failing test by throwing unexpected exception")
  public void contract_fail_bad_exception(
      final Data testData,
      final ContractInfo<ExampleService> contract
  ) {

    contract.throwing(
        Data.exceptionNotThrown,
        testData.exceptionMessage
    ).when()
        .testedMethod(testData.parameterInducingException);
  }

}
