package com.kodekonveyor.cdd.assemble;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Service;

import com.kodekonveyor.cdd.ContractInfo;

@Service
public class ContractInfoFactory<ServiceType>
    implements FactoryBean<ContractInfo<ServiceType>> {

  @Autowired
  private AutowireCapableBeanFactory beanFactory;

  @Override
  public ContractInfo<ServiceType> getObject() {
    ContractInfo<ServiceType> newObj = new ContractInfo<>();
    beanFactory.autowireBean(newObj);
    return newObj;
  }

  @Override
  public Class<?> getObjectType() {
    return ContractInfo.class;
  }

}
