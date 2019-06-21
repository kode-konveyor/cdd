package com.kodekonveyor.cdd;

public class ExampleService {

  public Integer testedMethod(Integer parameter) {
    if (parameter == 42)
      return 42;
    throw new IllegalArgumentException("bad parameter: " + parameter);
  }

}
