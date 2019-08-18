package com.kodekonveyor.cdd.build.impl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodekonveyor.cdd.ContractInfo;
import com.kodekonveyor.cdd.assemble.ContractInfoFactory;
import com.kodekonveyor.cdd.build.ContractCreationService;
import com.kodekonveyor.cdd.exception.StackTraceSetterService;
import com.kodekonveyor.cdd.exception.impl.StackTraceSetterServiceImpl;
import com.kodekonveyor.cdd.run.dto.ContractRunnerData;

@Service
public class ContractCreationServiceImpl<ServiceType>
    implements ContractCreationService<ServiceType> {

  @Autowired
  private final StackTraceSetterService stackTraceSetterService =
      new StackTraceSetterServiceImpl(); //FIXME

  public static final String NO_IT_FIELD =
      "No field marked with @ContractFactory in contract";
  @Autowired
  public ContractInfoFactory<ServiceType> contractInfoFactory;

  @Override
  public ContractInfo<ServiceType> createContract(
      final List<ContractInfo<ServiceType>> contracts, final Method method,
      final ContractRunnerData<ServiceType> data
  ) throws AssertionError, Throwable {

    final Field itField = data.getItField();
    checkitField(method, itField);
    final Object testInstance = data.getTestInstance();
    final ContractInfo<ServiceType> contract = constructContract(method, data);

    setItField(method, contract, itField, testInstance);

    invokeTestmethod(method, testInstance);

    contracts.add(contract);
    return contract;
  }

  private void checkitField(final Method method, final Field itField)
      throws Throwable {
    if (null == itField)
      throw this.stackTraceSetterService
          .changeStackWithMethod(new AssertionError(NO_IT_FIELD), method);
  }

  private ContractInfo<ServiceType> constructContract(
      final Method method, final ContractRunnerData<ServiceType> data
  ) {
    final ContractInfo<ServiceType> contract =
        this.contractInfoFactory.getObject();
    final ServiceType serviceInstance = data.getServiceInstance();
    contract.setService(serviceInstance);

    contract.setSuiteData(data);
    contract.setDefiningFunction(method);
    return contract;
  }

  private void invokeTestmethod(final Method method, final Object testInstance) //NOPMD CyclomaticComplexity
      throws Throwable {
    try {
      method.invoke(testInstance);
    } catch (
        IllegalAccessException | IllegalArgumentException e
    ) {
      throw this.stackTraceSetterService
          .changeStackWithMethod(new AssertionError(e), method);
    } catch (
      final InvocationTargetException e
    ) {
      throw this.stackTraceSetterService //NOPMD PreserveStackTrace
          .changeStackWithMethod(e.getCause(), method);
    }
  }

  private void setItField(
      final Method method, final ContractInfo<ServiceType> contract,
      final Field itField, final Object testInstance
  ) throws Throwable {
    try {
      itField.set(testInstance, contract);
    } catch (IllegalArgumentException | IllegalAccessException e) {
      throw this.stackTraceSetterService
          .changeStackWithMethod(new AssertionError(e), method);
    }
  }

}
