package com.kodekonveyor.cdd.dto;

import java.lang.reflect.Method;
import java.util.List;

import com.kodekonveyor.cdd.ContractInfo;
import com.kodekonveyor.cdd.run.dto.ContractRunnerData;
import com.kodekonveyor.cdd.testartifacts.ExampleService;

public class ContractJunitTestData {

  public ContractInfo<ExampleService> contract;
  public ContractRunnerData<ExampleService> contractRunnerData;
  public List<ContractInfo<ExampleService>> contracts;
  public Method contractMethod;

}
