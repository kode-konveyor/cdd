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
import com.kodekonveyor.cdd.exception.StackTraceSetterService;

@Service
public class ContractCreationServiceImpl<ServiceClass>
    implements ContractCreationService<ServiceClass> {

  @Autowired
  StackTraceSetterService stackTraceSetterService =
      new StackTraceSetterService();

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
    contract.setDefiningFunction(method);
    Field itField = data.getItField();
    if (null == itField) {
      throw stackTraceSetterService
          .changeStackWithMethod(new AssertionError(NO_IT_FIELD), method);
    }
    try {
      itField.set(testInstance, contract);
    } catch (IllegalArgumentException | IllegalAccessException e) {
      throw stackTraceSetterService
          .changeStackWithMethod(new AssertionError(e), method);
    }

    try {
      method.invoke(testInstance);
    } catch (
        IllegalAccessException | IllegalArgumentException e
    ) {
      throw stackTraceSetterService
          .changeStackWithMethod(new AssertionError(e), method);
    } catch (
      InvocationTargetException e
    ) {
      throw stackTraceSetterService
          .changeStackWithMethod(e.getCause(), method);
    }

    contracts.add(contract);
    return contract;
  }

}
