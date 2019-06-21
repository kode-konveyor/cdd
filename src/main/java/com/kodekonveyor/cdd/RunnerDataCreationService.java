package com.kodekonveyor.cdd;

import java.lang.reflect.InvocationTargetException;

import com.kodekonveyor.cdd.dto.ContractRunnerData;

public interface RunnerDataCreationService<ServiceClass> {

  ContractRunnerData<ServiceClass>
      makeRunnerDataFromTestClass(Class<? extends Object> testClass)
          throws InstantiationException, IllegalAccessException,
          InvocationTargetException, NoSuchMethodException;

}
