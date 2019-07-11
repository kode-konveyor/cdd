package com.kodekonveyor.cdd.assemble;

import java.lang.reflect.Method;

import com.kodekonveyor.cdd.run.dto.ContractRunnerData;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class ContractInfoData<ServiceClass> {

  private ServiceClass service;
  private Object stub;
  private Object returnValue;
  private Class<? extends RuntimeException> exceptionClass;
  private String exceptionMessage;
  private Method definingFunction;
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private ContractRunnerData<ServiceClass> suiteData;
  private Class<? extends Object> returnValueContracts;

}
