package com.kodekonveyor.cdd.build;

import org.junit.runner.Description;

import com.kodekonveyor.cdd.ContractInfo;
import com.kodekonveyor.cdd.run.dto.ContractRunnerData;

public interface ChildDescriptionService<ServiceClass> {

  Description describeChild(
      final ContractInfo<ServiceClass> contract,
      ContractRunnerData<ServiceClass> data
  );
}
