package com.kodekonveyor.cdd;

import org.springframework.beans.factory.annotation.Autowired;

import com.kodekonveyor.cdd.assemble.ContractInfoData;
import com.kodekonveyor.cdd.assemble.ContractInfoService;

import lombok.Getter;
import lombok.experimental.Delegate;

public class ContractInfo<ServiceType> {

  @Delegate
  @Getter
  private final ContractInfoData<ServiceType> data;

  @Autowired
  private ContractInfoService<ServiceType> contractInfoService;

  public ContractInfo() {
    this.data = new ContractInfoData<>();//FIXME
  }

  public ContractInfo<ServiceType> returns(final Object returnValue) {
    return this.contractInfoService.returns(returnValue, this);
  }

  public ContractInfo<ServiceType> throwing(
      final Class<? extends RuntimeException> exceptionClass,
      final String exceptionMessage
  ) {
    return this.contractInfoService
        .throwing(exceptionClass, exceptionMessage, this);
  }

  public ServiceType when() {
    return this.contractInfoService.when(this);
  }

  public ContractInfo<ServiceType>
      suchThat(final String... returnCheckDetails) {
    return this.contractInfoService.suchThat(returnCheckDetails, this);
  }

}
