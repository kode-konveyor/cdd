package com.kodekonveyor.cdd;

import java.util.function.BiPredicate;

import org.springframework.beans.factory.annotation.Autowired;

import com.kodekonveyor.cdd.assemble.ContractInfoData;
import com.kodekonveyor.cdd.assemble.ContractInfoServiceImpl;

import lombok.Getter;
import lombok.experimental.Delegate;

public class ContractInfo<ServiceType> {

  @Delegate
  @Getter
  private final ContractInfoData<ServiceType> data;

  @Autowired
  private ContractInfoServiceImpl<ServiceType> contractInfoServiceImpl;

  public ContractInfo() {
    this.data = new ContractInfoData<>();//FIXME
  }

  public ContractInfo<ServiceType> returns(final Object returnValue) {
    return this.contractInfoServiceImpl.returns(returnValue, this);
  }

  public ContractInfo<ServiceType> throwing(
      final Class<? extends RuntimeException> exceptionClass,
      final String exceptionMessage
  ) {
    return this.contractInfoServiceImpl
        .throwing(exceptionClass, exceptionMessage, this);
  }

  public ServiceType when() {
    return this.contractInfoServiceImpl.when(this);
  }

  public ContractInfo<ServiceType>
      withReturnPredicate(final BiPredicate<Object, Object> predicate) {
    return this.contractInfoServiceImpl.withReturnPredicate(predicate, this);
  }

}
