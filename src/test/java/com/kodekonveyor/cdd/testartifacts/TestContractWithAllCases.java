package com.kodekonveyor.cdd.testartifacts;

import org.mockito.InjectMocks;

import com.kodekonveyor.cdd.ContractInfo;
import com.kodekonveyor.cdd.annotations.Contract;
import com.kodekonveyor.cdd.annotations.Subject;;

public class TestContractWithAllCases {

  @Subject
  @InjectMocks
  public ExampleService service;

  @InjectMocks
  public Data testdata;

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

  @Contract("a failing contract with return definition")
  public void contract_failing_return(
      final Data testData,
      final ContractInfo<ExampleService> contract
  ) {

    contract.returns(
        testData.illegalOutputValue
    )
        .testedMethod(testData.goodParameter);
  }

  @Contract("a contract with exception definition")
  public void contract_passing_exception(
      final Data testData,
      final ContractInfo<ExampleService> contract
  ) {

    contract.throwing(
        Data.exceptionThrown,
        testData.exceptionMessage
    )
        .testedMethod(testData.parameterInducingException);
  }

  @Contract("a contract failing test by not throwing exception")
  public void contract_fail_no_exception(
      final Data testData,
      final ContractInfo<ExampleService> contract
  ) {

    contract.throwing(
        Data.exceptionThrown,
        testData.exceptionMessage
    )
        .testedMethod(testData.goodParameter);
  }

  @Contract("a contract failing test by throwing unexpected exception")
  public void contract_fail_bad_exception(
      final Data testData,
      final ContractInfo<ExampleService> contract
  ) {

    contract.throwing(
        Data.exceptionNotThrown,
        testData.exceptionMessage
    )
        .testedMethod(testData.parameterInducingException);
  }

}
