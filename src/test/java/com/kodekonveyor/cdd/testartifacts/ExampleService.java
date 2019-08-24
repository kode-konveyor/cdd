package com.kodekonveyor.cdd.testartifacts;

import org.springframework.stereotype.Service;

import com.kodekonveyor.cdd.TestContractTestData;

@Service
public class ExampleService {

  public Integer testedMethod(final Integer parameter) {
    if (parameter == TestContractTestData.GOOD_PARAMETER)
      return TestContractTestData.GOOD_RETURN_VALUE;
    throw new IllegalArgumentException("bad parameter: " + parameter);
  }

}
