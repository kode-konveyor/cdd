package com.kodekonveyor.cdd;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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

  @Setter
  private ServiceClass service;
  private Object stub;
  private Object returnValue;
  private Class<? extends RuntimeException> exceptionClass;
  private String exceptionMessage;
  @Setter
  private Method definingFunction;
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  @Setter
  private ContractRunnerData<ServiceClass> suiteData;

  private Class<? extends Object> returnValueContracts;

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
      Class<? extends Object> serviceClass = service.getClass();
      Object serviceMock = mock(serviceClass);
      stub =
          doThrow(exceptionClass.getConstructor(String.class)
              .newInstance(exceptionMessage)
          )
              .when(serviceMock);
    } catch (
        InstantiationException | IllegalAccessException |
        IllegalArgumentException | InvocationTargetException |
        NoSuchMethodException | SecurityException e
    ) {
      throw new AssertionError("cannot stub", e);
    }
    return (ServiceClass) stub;
  }

}
