package com.kodekonveyor.cdd.dto;

import java.util.List;

import org.junit.runner.Description;

import com.kodekonveyor.cdd.ContractInfo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class ContractRunnerData<ServiceClass> {

  private Class<? extends Object> testClass;
  private Object testInstance;
  private ServiceClass serviceInstance;
  private Description suiteDescription;
  @EqualsAndHashCode.Exclude
  private List<ContractInfo<ServiceClass>> contracts;
  @EqualsAndHashCode.Exclude
  private Object testData;
  private ServiceClass mock;

}
