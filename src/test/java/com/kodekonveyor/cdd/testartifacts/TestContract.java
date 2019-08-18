package com.kodekonveyor.cdd.testartifacts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodekonveyor.cdd.ContractInfo;
import com.kodekonveyor.cdd.annotation.ContractFactory;
import com.kodekonveyor.cdd.annotation.ContractRule;
import com.kodekonveyor.cdd.annotation.Subject;

@Service
public class TestContract {

  @Subject
  @Autowired
  public ExampleService service;

  @Autowired
  Data contractTestData;

  @ContractFactory
  public ContractInfo<ExampleService> it;

  @ContractRule("a contract with return definition")
  public void contract_passing_return() {

    it.returns(
        contractTestData.goodReturnValue
    ).when()
        .testedMethod(contractTestData.goodParameter);
  }

  @Override
  public boolean equals(final Object other) {
    if (other == null)
      return false;
    return other.getClass().equals(this.getClass());
  }

  @Override
  public int hashCode() {
    return this.getClass().getName().hashCode();
  }
}
