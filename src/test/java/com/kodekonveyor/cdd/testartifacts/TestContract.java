package com.kodekonveyor.cdd.testartifacts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodekonveyor.cdd.ContractInfo;
import com.kodekonveyor.cdd.annotation.ContractFactory;
import com.kodekonveyor.cdd.annotation.ContractRule;
import com.kodekonveyor.cdd.annotation.Subject;;

@Service
public class TestContract {

  @Subject
  @Autowired
  public ExampleService service;

  @Autowired
  Data testData;

  @ContractFactory
  public ContractInfo<ExampleService> it;

  @ContractRule("a contract with return definition")
  public void contract_passing_return() {

    it.returns(
        testData.goodReturnValue
    )
        .testedMethod(testData.goodParameter);
  }

  @Override
  public boolean equals(Object other) {
    if (other == null)
      return false;
    return other.getClass().equals(this.getClass());
  }
}
