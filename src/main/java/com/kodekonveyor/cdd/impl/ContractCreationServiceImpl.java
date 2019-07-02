package com.kodekonveyor.cdd.impl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodekonveyor.cdd.ContractCreationService;
import com.kodekonveyor.cdd.ContractInfo;
import com.kodekonveyor.cdd.dto.ContractRunnerData;
import com.kodekonveyor.cdd.exception.StackTraceCreatorService;

@Service
public class ContractCreationServiceImpl<ServiceClass>
    implements ContractCreationService<ServiceClass> {

  @Autowired
  StackTraceCreatorService stackTraceCreatorService =
      new StackTraceCreatorService();

  public static final String NO_IT_FIELD =
      "No field marked with @ContractFactory in contract";
  @Autowired
  public ContractInfoFactory<ServiceClass> contractInfoFactory;

  @Override
  public ContractInfo<ServiceClass> createContract(
      final List<ContractInfo<ServiceClass>> contracts, final Method method,
      ContractRunnerData<ServiceClass> data
  ) throws AssertionError, Throwable {
    Object testInstance = data.getTestInstance();
    final ContractInfo<ServiceClass> contract = contractInfoFactory.getObject();
    ServiceClass serviceInstance = data.getServiceInstance();
    contract.setService(serviceInstance);

    contract.setSuiteData(data);
    contract.setDefiningFunction(method.getName());
    Field itField = data.getItField();
    StackTraceElement[] stack =
        stackTraceCreatorService.createStackTrace(method);
    if (null == itField) {
      AssertionError exceptionObject = new AssertionError(NO_IT_FIELD);
      exceptionObject.setStackTrace(stack);
      throw exceptionObject;
    }
    try {
      itField.set(testInstance, contract);
    } catch (IllegalArgumentException | IllegalAccessException e) {
      AssertionError assertionError = new AssertionError(e);
      assertionError.setStackTrace(stack);
      throw assertionError;
    }

    try {
      method.invoke(testInstance);
    } catch (
        IllegalAccessException | IllegalArgumentException e
    ) {
      AssertionError assertionError = new AssertionError(e);
      assertionError.setStackTrace(stack);
      throw assertionError;
    } catch (
      InvocationTargetException e
    ) {
      e.printStackTrace();
      Throwable cause =
          e.getCause().getClass().getConstructor(String.class)
              .newInstance(e.getCause().getMessage());
      cause.setStackTrace(stack);
      throw cause;
    }

    contracts.add(contract);
    return contract;
  }

}
