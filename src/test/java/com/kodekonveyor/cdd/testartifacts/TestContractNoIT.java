package com.kodekonveyor.cdd.testartifacts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodekonveyor.cdd.ContractInfo;
import com.kodekonveyor.cdd.annotation.ContractRule;
import com.kodekonveyor.cdd.annotation.Subject;;

@Service
public class TestContractNoIT {

  @Subject
  @Autowired
  public ExampleService service;

  @Autowired
  Data testData;

  final public ContractInfo<ExampleService> it = null;

  @ContractRule("a contract with return definition")
  public void contract_passing_return() {

    it.returns(
        testData.goodReturnValue
    )
        .testedMethod(testData.goodParameter);
  }

}
