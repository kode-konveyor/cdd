package com.kodekonveyor.cdd;

import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.kodekonveyor.cdd.dto.ContractRunnerData;
import com.kodekonveyor.cdd.impl.ChildDescriptionServiceImpl;
import com.kodekonveyor.cdd.impl.ContractRunnerServiceImpl;
import com.kodekonveyor.cdd.impl.RunnerDataCreationServiceImpl;

public class ContractRunner<ServiceClass>
    extends ParentRunner<ContractInfo<ServiceClass>> {

  RunnerDataCreationService<ServiceClass> runnerDataCreationService;
  ContractRunnerService<ServiceClass> contractRunnerService;
  ChildDescriptionService<ServiceClass> childDescriptionService;

  private ContractRunnerData<ServiceClass> data;

  public ContractRunner(final Class<? extends Object> testClass)
      throws Throwable {
    super(testClass);
    loadDependencies();
    data = runnerDataCreationService.makeRunnerDataFromTestClass(testClass);
  }

  @SuppressWarnings("unchecked")
  private void loadDependencies() {
    try (
        ConfigurableApplicationContext ctx =
            new ClassPathXmlApplicationContext("applicationContext.xml")
    ) {
      runnerDataCreationService =
          (RunnerDataCreationServiceImpl<ServiceClass>) ctx
              .getBean("runnerDataCreationServiceImpl");
      contractRunnerService = (ContractRunnerServiceImpl<ServiceClass>) ctx
          .getBean("contractRunnerServiceImpl");
      childDescriptionService = (ChildDescriptionServiceImpl<ServiceClass>) ctx
          .getBean("childDescriptionServiceImpl");
    }
  }

  @Override
  public Description getDescription() {
    return data.getSuiteDescription();
  }

  @Override
  protected List<ContractInfo<ServiceClass>> getChildren() {
    return data.getContracts();
  }

  @Override
  protected Description
      describeChild(final ContractInfo<ServiceClass> contract) {
    return childDescriptionService.describeChild(contract, data);

  }

  @Override
  protected void
      runChild(ContractInfo<ServiceClass> child, RunNotifier notifier) {
    contractRunnerService.runChild(child, notifier, data);
  }

}
