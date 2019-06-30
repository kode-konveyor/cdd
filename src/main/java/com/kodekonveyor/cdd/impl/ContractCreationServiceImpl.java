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

@Service
public class ContractCreationServiceImpl<ServiceClass>
    implements ContractCreationService<ServiceClass> {

  @Autowired
  public ContractInfoFactory<ServiceClass> contractInfoFactory;

  @Override
  public ContractInfo<ServiceClass> createContract(
      final List<ContractInfo<ServiceClass>> contracts, final Method method,
      ContractRunnerData<ServiceClass> data
  ) throws AssertionError, Throwable {
    System.out.println("createContract" + data);
    Object testInstance = data.getTestInstance();
    final ContractInfo<ServiceClass> contract = contractInfoFactory.getObject();
    ServiceClass serviceInstance = data.getServiceInstance();
    contract.setService(serviceInstance);

    contract.setSuiteData(data);
    contract.setDefiningFunction(method.getName());
    Field itField = data.getItField();
    if (null == itField)
      throw new AssertionError("NO IT FIELD");
    try {
      itField.set(testInstance, contract);
    } catch (IllegalArgumentException | IllegalAccessException e) {
      throw new AssertionError(e);
    }

    try {
      method.invoke(testInstance);
    } catch (
        IllegalAccessException | IllegalArgumentException e
    ) {
      throw new AssertionError(e);
    } catch (
      InvocationTargetException e
    ) {
      throw e.getCause();
    }
    contracts.add(contract);
    return contract;
  }

}
