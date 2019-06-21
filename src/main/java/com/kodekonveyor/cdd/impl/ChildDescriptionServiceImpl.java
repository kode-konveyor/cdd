package com.kodekonveyor.cdd.impl;

import org.junit.runner.Description;
import org.springframework.stereotype.Service;

import com.kodekonveyor.cdd.ChildDescriptionService;
import com.kodekonveyor.cdd.ContractInfo;
import com.kodekonveyor.cdd.dto.ContractRunnerData;

@Service
public class ChildDescriptionServiceImpl<ServiceClass>
    implements ChildDescriptionService<ServiceClass> {

  @Override
  public Description describeChild(
      final ContractInfo<ServiceClass> contract,
      ContractRunnerData<ServiceClass> data
  ) {
    return Description
        .createTestDescription(
            data.getTestClass(), contract.getDefiningFunction()
        );
  }

}
