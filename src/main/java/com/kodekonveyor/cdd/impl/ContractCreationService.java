package com.kodekonveyor.cdd.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kodekonveyor.cdd.ContractInfo;
import com.kodekonveyor.cdd.dto.ContractRunnerData;

@Service
public class ContractCreationService<ServiceClass> {

  public void createContract(
      final List<ContractInfo<ServiceClass>> contracts, final Method method,
      ContractRunnerData<ServiceClass> data
  ) throws AssertionError, Throwable {
    Object testInstance = data.getTestInstance();
    Object testData = data.getTestData();
    ContractInfo<ServiceClass> contractInfo =
        new ContractInfo<ServiceClass>(data.getMock());
    final ContractInfo<ServiceClass> contract =
        contractInfo;
    contract.setSuiteData(data);
    contract.setDefiningFunction(method.getName());
    try {
      method.invoke(testInstance, testData, contract);
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
  }

}
