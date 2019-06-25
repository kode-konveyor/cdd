package com.kodekonveyor.cdd;

import org.springframework.stereotype.Service;

import com.kodekonveyor.cdd.dto.ContractRunnerData;

@Service
public interface RunnerDataCreationService<ServiceClass> {

  ContractRunnerData<ServiceClass>
      makeRunnerDataFromTestClass(Class<? extends Object> testClass)
          throws Throwable;

}
