package com.kodekonveyor.cdd.exception;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Service;

@Service
public class ThrowableTesterFactory implements FactoryBean<ThrowableTester> {

  @Override
  public ThrowableTester getObject() {
    return new ThrowableTester();
  }

  @Override
  public Class<?> getObjectType() {
    return ThrowableTester.class;
  }

}
