package com.kodekonveyor.cdd.impl;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Service;

import com.kodekonveyor.cdd.ContractInfo;

@Service
public class ContractInfoFactory<ServiceClass>
    implements FactoryBean<ContractInfo<ServiceClass>> {

  @Override
  public ContractInfo<ServiceClass> getObject() {
    ContractInfo<ServiceClass> newObj = new ContractInfo<ServiceClass>();
    return newObj;
  }

  @Override
  public Class<?> getObjectType() {
    return ContractInfo.class;
  }

}
