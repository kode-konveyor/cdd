package com.kodekonveyor.cdd.assemble;

import com.kodekonveyor.cdd.ContractInfo;

public interface ContractInfoService<ServiceType> {

  ContractInfo<ServiceType> returns(
      Object returnValue, ContractInfo<ServiceType> contractInfo
  );

  ContractInfo<ServiceType> throwing(
      Class<? extends RuntimeException> exceptionClass,
      String exceptionMessage, ContractInfo<ServiceType> contractInfo
  );

  ServiceType when(ContractInfo<ServiceType> contractInfo);

}
