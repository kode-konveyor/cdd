package com.kodekonveyor.cdd;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;

import com.kodekonveyor.cdd.annotations.Contract;
import com.kodekonveyor.cdd.annotations.Subject;
import com.kodekonveyor.cdd.annotations.TestData;

@RunWith(ContractRunner.class)
public class RunnerContract {

  @Subject
  @InjectMocks
  public ExampleService service;

  @TestData
  @InjectMocks
  public Data testdata;

  @Contract("")
  public void
      foo(ProjectTestData data, ContractInfo<ExampleService> contract) {
    contract.returns(testdata.goodReturnValue)
        .testedMethod(testdata.goodParameter);
  }
}
