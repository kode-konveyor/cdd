package com.kodekonveyor.cdd.assemble;

import com.kodekonveyor.cdd.ContractInfo;

public interface ContractInfoService<ServiceType> {

  ServiceType returns(
      Object returnValue, ContractInfo<ServiceType> contractInfo
  );

  ServiceType throwing(
      Class<? extends RuntimeException> exceptionClass,
      String exceptionMessage, ContractInfo<ServiceType> contractInfo
  );

}
