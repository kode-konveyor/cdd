package com.kodekonveyor.cdd.build.impl;

import org.junit.runner.Description;
import org.springframework.stereotype.Service;

import com.kodekonveyor.cdd.ContractInfo;
import com.kodekonveyor.cdd.build.ChildDescriptionService;
import com.kodekonveyor.cdd.run.dto.ContractRunnerData;

@Service
public class ChildDescriptionServiceImpl<ServiceType>
    implements ChildDescriptionService<ServiceType> {

  @Override
  public Description describeChild(
      final ContractInfo<ServiceType> contract,
      final ContractRunnerData<ServiceType> data
  ) {
    return Description
        .createTestDescription(
            data.getTestClass(), contract.getDefiningFunction().getName()
        );
  }

}
