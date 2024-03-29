package com.kodekonveyor.cdd.assemble;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.kodekonveyor.cdd.run.dto.ContractRunnerData;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class ContractInfoData<ServiceType> {

  private ServiceType service;
  private Object stub;
  private Object returnValue;
  private Class<? extends RuntimeException> exceptionClass;
  private String exceptionMessage;
  private Method definingFunction;
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private ContractRunnerData<ServiceType> suiteData;
  private List<String> checkedReturnDetails = new ArrayList<>();

}
