package com.kodekonveyor.cdd;

import org.junit.runner.notification.RunNotifier;

import com.kodekonveyor.cdd.dto.ContractRunnerData;

public interface ContractRunnerService<ServiceClass> {

  public void runChild(
      final ContractInfo<ServiceClass> contract, final RunNotifier notifier,
      ContractRunnerData<ServiceClass> data
  );

}
