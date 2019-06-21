package com.kodekonveyor.cdd;

import org.junit.runner.Description;

import com.kodekonveyor.cdd.dto.ContractRunnerData;

public interface ChildDescriptionService<ServiceClass> {

  Description describeChild(
      final ContractInfo<ServiceClass> contract,
      ContractRunnerData<ServiceClass> data
  );
}
