package com.kodekonveyor.cdd.testartifacts;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodekonveyor.cdd.ContractInfo;
import com.kodekonveyor.cdd.TestContractTestData;
import com.kodekonveyor.cdd.annotation.ContractFactory;
import com.kodekonveyor.cdd.annotation.ContractRule;
import com.kodekonveyor.cdd.annotation.ReturnDetail;
import com.kodekonveyor.cdd.annotation.Subject;

@Service
public class TestContract {

  @Subject
  @Autowired
  public ExampleService service;

  @ContractFactory
  public ContractInfo<ExampleService> it; //NOPMD ShortVariable

  @ContractRule("a contract with return definition")
  public void contractPassingReturn() {

    it.returns(
        TestContractTestData.GOOD_RETURN_VALUE
    ).when()
        .testedMethod(TestContractTestData.GOOD_PARAMETER);
  }

  public void methodWithParameter(final Object foo) {
  }

  @ReturnDetail("Returns 42")
  public void returnDetailPredicate(final Integer returnValue) {
    assertEquals((Integer) 43, returnValue);
  }

}
