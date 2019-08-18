package com.kodekonveyor.cdd.assemble;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Service;

@Service
public class ContractInfoDataFactory<ServiceType>
    implements FactoryBean<ContractInfoData<ServiceType>> {

  @Autowired
  private AutowireCapableBeanFactory beanFactory;

  @Override
  public ContractInfoData<ServiceType> getObject() {
    ContractInfoData<ServiceType> newObj =
        new ContractInfoData<>();
    beanFactory.autowireBean(newObj);
    return newObj;
  }

  @Override
  public Class<?> getObjectType() {
    return ContractInfoData.class;
  }

}
