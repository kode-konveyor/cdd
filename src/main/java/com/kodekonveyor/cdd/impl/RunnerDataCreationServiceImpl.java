package com.kodekonveyor.cdd.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.runner.Description;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Service;

import com.kodekonveyor.cdd.ContractCreationService;
import com.kodekonveyor.cdd.ContractInfo;
import com.kodekonveyor.cdd.RunnerDataCreationService;
import com.kodekonveyor.cdd.annotations.Contract;
import com.kodekonveyor.cdd.annotations.ContractFactory;
import com.kodekonveyor.cdd.annotations.Subject;
import com.kodekonveyor.cdd.dto.ContractRunnerData;

@Service
public class RunnerDataCreationServiceImpl<ServiceClass>
    implements RunnerDataCreationService<ServiceClass> {

  @Autowired
  FieldGetterServiceImpl fieldGetterService;

  @Autowired
  ChildDescriptionServiceImpl<ServiceClass> childDescriptionService;

  @Autowired
  ContractCreationService<ServiceClass> contractCreationService;

  @Autowired
  private AutowireCapableBeanFactory beanFactory;

  @Override
  public ContractRunnerData<ServiceClass>
      makeRunnerDataFromTestClass(final Class<? extends Object> testClass)
          throws Throwable {
    return makeRunnerDataFromTestClass(testClass, null);
  }

  public ContractRunnerData<ServiceClass> makeRunnerDataFromTestClass(
      Class<? extends Object> testClass, Object myAnswer
  ) throws Throwable {
    ContractRunnerData<ServiceClass> data = new ContractRunnerData<>();
    data.setTestClass(testClass);
    Object testInstance = beanFactory.createBean(testClass);
    if (null == testInstance)
      throw new AssertionError("NO TEST INSTANCE of " + testClass);

    setServiceInstance(data, testInstance);
    data.setTestInstance(testInstance);
    Field itField = fieldGetterService
        .getFieldWithAnnotation(ContractFactory.class, testInstance);
    if (null == itField)
      throw new AssertionError("NO IT FIELD in " + testInstance);
    data.setItField(itField);
    data.setContracts(createContracts(data));
    data.setSuiteDescription(Description.createSuiteDescription(testClass));
    for (final ContractInfo<ServiceClass> child : data.getContracts())
      data.getSuiteDescription()
          .addChild(childDescriptionService.describeChild(child, data));
    return data;
  }

  @SuppressWarnings("unchecked")
  private void setServiceInstance(
      ContractRunnerData<ServiceClass> data, Object testInstance
  ) throws IllegalAccessException {
    data.setServiceInstance(
        (ServiceClass) fieldGetterService
            .getFieldValueWithAnnotation(Subject.class, testInstance)
    );
  }

  private List<ContractInfo<ServiceClass>>
      createContracts(ContractRunnerData<ServiceClass> data) throws Throwable {
    final List<ContractInfo<ServiceClass>> contracts = new ArrayList<>();
    for (final Method method : data.getTestClass().getMethods()) {
      List<Contract> annotations =
          Arrays.asList(method.getDeclaredAnnotationsByType(Contract.class));
      if (!annotations.isEmpty()) {
        contractCreationService.createContract(
            contracts, method, data
        );
      }
    }
    return contracts;
  }

}
