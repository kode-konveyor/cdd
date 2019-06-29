package com.kodekonveyor.cdd.dto;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.runner.Description;
import org.springframework.stereotype.Component;

import com.kodekonveyor.cdd.ContractInfo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Component
public class ContractRunnerData<ServiceClass> {

  private Class<? extends Object> testClass;
  private Object testInstance;
  private Description suiteDescription;
  @EqualsAndHashCode.Exclude
  private List<ContractInfo<ServiceClass>> contracts;
  private Field itField;
  private ServiceClass serviceInstance;

}
