package com.kodekonveyor.cdd;

import org.springframework.beans.factory.annotation.Autowired;

import com.kodekonveyor.cdd.assemble.ContractInfoData;
import com.kodekonveyor.cdd.assemble.ContractInfoServiceImpl;

import lombok.Getter;
import lombok.experimental.Delegate;

public class ContractInfo<ServiceClass> {

  @Delegate
  @Getter
  ContractInfoData<ServiceClass> data;

  @Autowired
  ContractInfoServiceImpl<ServiceClass> contractInfoServiceImpl;

  public ContractInfo() {
    data = new ContractInfoData<>();//FIXME
  }

  public ServiceClass returns(Object returnValue) {
    return contractInfoServiceImpl.returns(returnValue, this);
  }

  public ServiceClass throwing(
      Class<? extends RuntimeException> exceptionClass,
      String exceptionMessage
  ) {
    return contractInfoServiceImpl
        .throwing(exceptionClass, exceptionMessage, this);
  }

}
