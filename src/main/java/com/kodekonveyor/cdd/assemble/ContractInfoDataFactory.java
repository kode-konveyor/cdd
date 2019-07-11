package com.kodekonveyor.cdd.assemble;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Service;

@Service
public class ContractInfoDataFactory<ServiceClass>
    implements FactoryBean<ContractInfoData<ServiceClass>> {

  @Autowired
  private AutowireCapableBeanFactory beanFactory;

  @Override
  public ContractInfoData<ServiceClass> getObject() {
    ContractInfoData<ServiceClass> newObj =
        new ContractInfoData<ServiceClass>();
    beanFactory.autowireBean(newObj);
    return newObj;
  }

  @Override
  public Class<?> getObjectType() {
    return ContractInfoData.class;
  }

}
