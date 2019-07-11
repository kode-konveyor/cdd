package com.kodekonveyor.cdd.build;

import java.lang.reflect.Method;
import java.util.List;

import com.kodekonveyor.cdd.ContractInfo;
import com.kodekonveyor.cdd.run.dto.ContractRunnerData;

public interface ContractCreationService<ServiceClass> {

  ContractInfo<ServiceClass> createContract(
      List<ContractInfo<ServiceClass>> contracts, Method method,
      ContractRunnerData<ServiceClass> data
  ) throws AssertionError, Throwable;

}
