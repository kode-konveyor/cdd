package com.kodekonveyor.cdd.assemble;

import com.kodekonveyor.cdd.ContractInfo;

public interface ContractInfoService<ServiceClass> {

  ServiceClass returns(
      Object returnValue, ContractInfo<ServiceClass> contractInfo
  );

  ServiceClass throwing(
      Class<? extends RuntimeException> exceptionClass,
      String exceptionMessage, ContractInfo<ServiceClass> contractInfo
  );

}
