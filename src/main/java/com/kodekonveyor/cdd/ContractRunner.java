package com.kodekonveyor.cdd;

import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;

import com.kodekonveyor.cdd.build.ChildDescriptionService;
import com.kodekonveyor.cdd.build.RunnerDataCreationService;
import com.kodekonveyor.cdd.run.ContractRunnerService;
import com.kodekonveyor.cdd.run.dto.ContractRunnerData;

import lombok.Setter;

@Setter
public class ContractRunner<ServiceClass>
    extends ParentRunner<ContractInfo<ServiceClass>> {

  RunnerDataCreationService<ServiceClass> runnerDataCreationService;
  ContractRunnerService<ServiceClass> contractRunnerService;
  ChildDescriptionService<ServiceClass> childDescriptionService;

  private ContractRunnerData<ServiceClass> data;

  public ContractRunner(final Class<? extends Object> testClass)
      throws Throwable {
    super(testClass);
    ContractRunnerService.loadDependencies(this);
    data = runnerDataCreationService.makeRunnerDataFromTestClass(testClass);
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
