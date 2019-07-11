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

public interface ContractRunnerService<ServiceClass> {

  public void runChild(
      final ContractInfo<ServiceClass> contract, final RunNotifier notifier,
      ContractRunnerData<ServiceClass> data
  );

  @SuppressWarnings({
      "unchecked", "rawtypes"
  })
  static void loadDependencies(ContractRunner<?> self) {
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
