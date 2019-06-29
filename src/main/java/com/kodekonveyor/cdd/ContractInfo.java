package com.kodekonveyor.cdd;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import java.lang.reflect.InvocationTargetException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kodekonveyor.cdd.dto.ContractRunnerData;
import com.kodekonveyor.cdd.impl.RunnerDataCreationServiceImpl;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@Component
public class ContractInfo<ServiceClass> {

  @Autowired
  public RunnerDataCreationServiceImpl<?> runnerDataCreationServiceImpl;

  private ServiceClass service;
  private Object stub;
  private Object returnValue;
  private Class<? extends RuntimeException> exceptionClass;
  private String exceptionMessage;
  @Setter
  private String definingFunction;
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  @Setter
  private ContractRunnerData<ServiceClass> suiteData;

  private Class<? extends Object> returnValueContracts;

  public ContractInfo(final ServiceClass service) {
    this.service = service;
  }

  @SuppressWarnings("unchecked")
  public ServiceClass
      returns(final Object returnValue) {
    this.returnValue = returnValue;
    stub = doReturn(returnValue).when(mock(service.getClass()));
    return (ServiceClass) stub;
  }

  @SuppressWarnings("unchecked")
  public ServiceClass throwing(
      final Class<? extends RuntimeException> exceptionClass,
      final String exceptionMessage
  ) {
    this.exceptionClass = exceptionClass;
    this.exceptionMessage = exceptionMessage;
    try {
      stub =
          doThrow(exceptionClass.getConstructor(String.class)
              .newInstance(exceptionMessage)
          )
              .when(mock(service.getClass()));
    } catch (
        InstantiationException | IllegalAccessException |
        IllegalArgumentException | InvocationTargetException |
        NoSuchMethodException | SecurityException e
    ) {
      e.printStackTrace();
    }
    return (ServiceClass) stub;
  }

}
