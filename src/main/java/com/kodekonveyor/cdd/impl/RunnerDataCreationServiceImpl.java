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
import com.kodekonveyor.cdd.annotations.ContractFactory;
import com.kodekonveyor.cdd.annotations.ContractRule;
import com.kodekonveyor.cdd.annotations.Subject;
import com.kodekonveyor.cdd.dto.ContractRunnerData;
import com.kodekonveyor.cdd.exception.StackTraceCreatorService;

import javassist.NotFoundException;

@Service
public class RunnerDataCreationServiceImpl<ServiceClass>
    implements RunnerDataCreationService<ServiceClass> {

  public static final String NO_TEST_INSTANCE = "NO TEST INSTANCE of ";

  public static final String NO_IT_FIELD =
      "no field marked @ContractFactory in ";

  @Autowired
  FieldGetterServiceImpl fieldGetterService;

  @Autowired
  ChildDescriptionServiceImpl<ServiceClass> childDescriptionService;

  @Autowired
  ContractCreationService<ServiceClass> contractCreationService;

  @Autowired
  private AutowireCapableBeanFactory beanFactory;

  @Autowired
  public StackTraceCreatorService stackTraceCreatorService;

  @Override
  public ContractRunnerData<ServiceClass>
      makeRunnerDataFromTestClass(final Class<? extends Object> testClass)
          throws Throwable {
    ContractRunnerData<ServiceClass> runnerData =
        makeRunnerDataFromTestClass(testClass, null);
    return runnerData;
  }

  public ContractRunnerData<ServiceClass> makeRunnerDataFromTestClass(
      Class<? extends Object> testClass, Object myAnswer
  ) throws Throwable {
    ContractRunnerData<ServiceClass> data = new ContractRunnerData<>();
    data.setTestClass(testClass);
    Object testInstance = beanFactory.createBean(testClass);
    if (null == testInstance) {
      AssertionError assertionError =
          new AssertionError(NO_TEST_INSTANCE + testClass);
      StackTraceElement[] stack =
          stackTraceCreatorService.createStackTrace(null, testClass);
      assertionError.setStackTrace(stack);

      throw assertionError;
    }

    setServiceInstance(data, testInstance);
    data.setTestInstance(testInstance);
    Field itField = fieldGetterService
        .getFieldWithAnnotation(ContractFactory.class, testInstance);
    if (null == itField) {
      AssertionError assertionError =
          new AssertionError(
              NO_IT_FIELD + testInstance.getClass().getSimpleName()
          );
      StackTraceElement[] stack =
          stackTraceCreatorService.createStackTrace(testInstance);
      assertionError.setStackTrace(stack);
      throw assertionError;
    }
    data.setItField(itField);
    Field subjectField = fieldGetterService
        .getFieldWithAnnotation(Subject.class, testInstance);
    Class<?> subjectClass = subjectField.getType();
    System.out.println("creating subject");
    Object subject = beanFactory.createBean(subjectClass);
    System.out.println("subject:" + subject);
    subjectField.set(testInstance, subject);
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
  ) throws IllegalAccessException, IllegalArgumentException,
      NoSuchMethodException, SecurityException, NotFoundException {
    data.setServiceInstance(
        (ServiceClass) fieldGetterService
            .getFieldValueWithAnnotation(Subject.class, testInstance)
    );
  }

  private List<ContractInfo<ServiceClass>>
      createContracts(ContractRunnerData<ServiceClass> data) throws Throwable {
    final List<ContractInfo<ServiceClass>> contracts = new ArrayList<>();
    for (final Method method : data.getTestClass().getMethods()) {
      List<ContractRule> annotations =
          Arrays.asList(method.getDeclaredAnnotationsByType(ContractRule.class));
      if (!annotations.isEmpty()) {
        contractCreationService.createContract(
            contracts, method, data
        );
      }
    }
    return contracts;
  }

}
