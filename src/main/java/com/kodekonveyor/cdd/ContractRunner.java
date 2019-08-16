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
public class ContractRunner<ServiceType>
    extends ParentRunner<ContractInfo<ServiceType>> {

  private RunnerDataCreationService<ServiceType> runnerDataCreationService;
  private ContractRunnerService<ServiceType> contractRunnerService;
  private ChildDescriptionService<ServiceType> childDescriptionService;

  private ContractRunnerData<ServiceType> data;

  public ContractRunner(final Class<? extends Object> testClass)
      throws Throwable {
    super(testClass);
    ContractRunnerService.loadDependencies(this);
    this.data =
        this.runnerDataCreationService.makeRunnerDataFromTestClass(testClass);
  }

  @Override
  public Description getDescription() {
    return this.data.getSuiteDescription();
  }

  @Override
  protected List<ContractInfo<ServiceType>> getChildren() {
    return this.data.getContracts();
  }

  @Override
  protected Description
      describeChild(final ContractInfo<ServiceType> contract) {
    return this.childDescriptionService.describeChild(contract, this.data);

  }

  @Override
  protected void
      runChild(final ContractInfo<ServiceType> child, final RunNotifier notifier) {
    this.contractRunnerService.runChild(child, notifier, this.data);
  }

}
