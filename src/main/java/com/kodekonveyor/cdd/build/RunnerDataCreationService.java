package com.kodekonveyor.cdd.build;

import org.springframework.stereotype.Service;

import com.kodekonveyor.cdd.run.dto.ContractRunnerData;

@Service
public interface RunnerDataCreationService<ServiceClass> {

  ContractRunnerData<ServiceClass>
      makeRunnerDataFromTestClass(Class<? extends Object> testClass)
          throws Throwable;

}
