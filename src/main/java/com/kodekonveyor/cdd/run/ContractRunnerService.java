package com.kodekonveyor.cdd.run;

import org.junit.runner.notification.RunNotifier;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.kodekonveyor.cdd.ContractInfo;
import com.kodekonveyor.cdd.ContractRunner;
import com.kodekonveyor.cdd.build.RunnerDataCreationService;
import com.kodekonveyor.cdd.build.impl.ChildDescriptionServiceImpl;
import com.kodekonveyor.cdd.run.dto.ContractRunnerData;
import com.kodekonveyor.cdd.run.impl.ContractRunnerServiceImpl;

public interface ContractRunnerService<ServiceType> {

  void runChild(
      final ContractInfo<ServiceType> contract, final RunNotifier notifier,
      ContractRunnerData<ServiceType> data
  );

  @SuppressWarnings({
      "unchecked", "rawtypes"
  })
  static void loadDependencies(final ContractRunner<?> self) {
    try (
        ConfigurableApplicationContext ctx =
            new ClassPathXmlApplicationContext("applicationContext.xml")
    ) {
      self.setRunnerDataCreationService(
          (RunnerDataCreationService) ctx
              .getBean("runnerDataCreationServiceImpl")
      );
      self.setContractRunnerService(
          (ContractRunnerServiceImpl) ctx
              .getBean("contractRunnerServiceImpl")
      );
      self.setChildDescriptionService(
          (ChildDescriptionServiceImpl) ctx
              .getBean("childDescriptionServiceImpl")
      );
    }
  }

}
