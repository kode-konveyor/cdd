package com.kodekonveyor.cdd.run.dto;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.runner.Description;

import com.kodekonveyor.cdd.ContractInfo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContractRunnerData<ServiceType> {

  private Class<? extends Object> testClass;
  private Object testInstance;
  private Description suiteDescription;
  @EqualsAndHashCode.Exclude
  private List<ContractInfo<ServiceType>> contracts;
  private Field itField;
  private ServiceType serviceInstance;
  private Map<String, Method> returnValueContracts = new HashMap<>();//NOPMD UseConcurrentHashMap

}
