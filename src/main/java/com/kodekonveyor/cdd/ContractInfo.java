package com.kodekonveyor.cdd;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import java.lang.reflect.InvocationTargetException;

import org.springframework.beans.factory.annotation.Autowired;

import com.kodekonveyor.cdd.dto.ContractRunnerData;
import com.kodekonveyor.cdd.impl.RunnerDataCreationServiceImpl;
import com.kodekonveyor.cdd.predicates.Identity;
import com.kodekonveyor.cdd.predicates.SameClass;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
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
  @Setter
  private ContractRunnerData<ServiceClass> suiteData;

  private Class<? extends Object> returnValueContracts;

  private EqualityPredicate equalityPredicate;

  public ContractInfo(final ServiceClass service) {
    this.service = service;
  }

  @SuppressWarnings("unchecked")
  ContractInfo() {
    service = (ServiceClass) this;
  }

  public ServiceClass
      returns(final Object returnValue) {
    return returnsWith(returnValue, new Identity());
  }

  @SuppressWarnings("unchecked")
  private ServiceClass returns_nopredicate(final Object returnValue) {
    this.returnValue = returnValue;
    stub = doReturn(returnValue).when(mock(service.getClass()));
    return (ServiceClass) stub;
  }

  public ServiceClass returns(
      Object returnValue,
      Class<? extends Object> contractForReturnValue
  ) throws Throwable {
    this.returnValueContracts = contractForReturnValue;
    return returns(returnValue);
  }

  public ServiceClass returnsClassOf(Object obj) {
    EqualityPredicate predicate = new SameClass();
    return returnsWith(obj, predicate);
  }

  public ServiceClass returnsWith(Object obj, EqualityPredicate predicate) {
    this.equalityPredicate = predicate;
    return returns_nopredicate(obj);
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
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return (ServiceClass) stub;
  }

}
