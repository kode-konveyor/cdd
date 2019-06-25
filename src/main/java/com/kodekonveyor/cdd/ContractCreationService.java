package com.kodekonveyor.cdd;

import java.lang.reflect.Method;
import java.util.List;

import com.kodekonveyor.cdd.ContractInfo;
import com.kodekonveyor.cdd.dto.ContractRunnerData;

public interface ContractCreationService<ServiceClass> {

  void createContract(
      List<ContractInfo<ServiceClass>> contracts, Method method,
      ContractRunnerData<ServiceClass> data
  ) throws AssertionError, Throwable;

}
