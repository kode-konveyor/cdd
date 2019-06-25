package com.kodekonveyor.cdd.testartifacts;

import org.springframework.stereotype.Service;

import com.kodekonveyor.cdd.impl.DataFactory;

@Service
public class TestArtifactDataFactory implements DataFactory {

  @Override
  public Object createTestData() {
    return new Data();
  }

}
