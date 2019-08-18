package com.kodekonveyor.cdd.build;

import org.springframework.stereotype.Service;

import com.kodekonveyor.cdd.run.dto.ContractRunnerData;

@Service
public interface RunnerDataCreationService<ServiceType> {

  ContractRunnerData<ServiceType>
      makeRunnerDataFromTestClass(Class<? extends Object> testClass)
          throws Throwable;

}
