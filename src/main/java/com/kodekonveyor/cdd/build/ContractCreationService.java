package com.kodekonveyor.cdd.build;

import java.lang.reflect.Method;
import java.util.List;

import com.kodekonveyor.cdd.ContractInfo;
import com.kodekonveyor.cdd.run.dto.ContractRunnerData;

public interface ContractCreationService<ServiceType> {

  ContractInfo<ServiceType> createContract(
      List<ContractInfo<ServiceType>> contracts, Method method,
      ContractRunnerData<ServiceType> data
  ) throws AssertionError, Throwable;

}
