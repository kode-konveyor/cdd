package com.kodekonveyor.cdd.build.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.runner.Description;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Service;

import com.kodekonveyor.cdd.ContractInfo;
import com.kodekonveyor.cdd.annotation.ContractFactory;
import com.kodekonveyor.cdd.annotation.ContractRule;
import com.kodekonveyor.cdd.annotation.Subject;
import com.kodekonveyor.cdd.build.ChildDescriptionService;
import com.kodekonveyor.cdd.build.ContractCreationService;
import com.kodekonveyor.cdd.build.RunnerDataCreationService;
import com.kodekonveyor.cdd.exception.StackTraceSetterService;
import com.kodekonveyor.cdd.fields.FieldGetterService;
import com.kodekonveyor.cdd.run.dto.ContractRunnerData;

import javassist.NotFoundException;
import lombok.Getter;
import lombok.Setter;

@Service
public class RunnerDataCreationServiceImpl<ServiceType>
    implements RunnerDataCreationService<ServiceType> {

  public static final String NO_TEST_INSTANCE = "NO TEST INSTANCE of ";

  public static final String NO_IT_FIELD =
      "no field marked @ContractFactory in ";

  @Autowired
  private FieldGetterService fieldGetterService;

  @Autowired
  private ChildDescriptionService<ServiceType> childDescriptionService;

  @Autowired
  private ContractCreationService<ServiceType> contractCreationService;

  @Autowired
  private AutowireCapableBeanFactory beanFactory;

  @Autowired
  @Getter
  @Setter
  private StackTraceSetterService stackTraceSetterService;

  @Override
  public ContractRunnerData<ServiceType>
      makeRunnerDataFromTestClass(final Class<? extends Object> testClass)
          throws Throwable {
    return makeRunnerDataFromTestClass(testClass, null);
  }

  public ContractRunnerData<ServiceType> makeRunnerDataFromTestClass(
      final Class<? extends Object> testClass, final Object myAnswer
  ) throws Throwable {
    final ContractRunnerData<ServiceType> data = new ContractRunnerData<>();
    data.setTestClass(testClass);
    final Object testInstance = this.beanFactory.createBean(testClass);
    checktestInstance(testClass, testInstance);

    setServiceInstance(data, testInstance);
    data.setTestInstance(testInstance);
    final Field itField = this.fieldGetterService
        .getFieldWithAnnotation(ContractFactory.class, testInstance);
    checkItField(testInstance, itField);
    data.setItField(itField);
    makeSubjectField(testInstance);
    data.setContracts(createContracts(data));
    data.setSuiteDescription(Description.createSuiteDescription(testClass));
    registerTestCases(data);
    return data;
  }

  private void checkItField(final Object testInstance, final Field itField)
      throws Throwable {
    if (null == itField)
      throw this.getStackTraceSetterService()
          .changeStackWithClass(

              new AssertionError(
                  NO_IT_FIELD + testInstance.getClass().getSimpleName()
              ), testInstance.getClass()
          );
  }

  private void checktestInstance(
      final Class<? extends Object> testClass, final Object testInstance
  ) throws AssertionError {
    if (null == testInstance)
      throw (AssertionError) this.getStackTraceSetterService()
          .changeStackWithClass(
              new AssertionError(NO_TEST_INSTANCE + testClass),
              testClass
          );
  }

  private void registerTestCases(final ContractRunnerData<ServiceType> data) {
    for (final ContractInfo<ServiceType> child : data.getContracts())
      data.getSuiteDescription()
          .addChild(this.childDescriptionService.describeChild(child, data));
  }

  private void makeSubjectField(final Object testInstance)
      throws IllegalAccessException {
    final Field subjectField = this.fieldGetterService
        .getFieldWithAnnotation(Subject.class, testInstance);
    final Class<?> subjectClass = subjectField.getType();
    final Object subject = this.beanFactory.createBean(subjectClass);
    subjectField.set(testInstance, subject);
  }

  @SuppressWarnings("unchecked")
  private void setServiceInstance(
      final ContractRunnerData<ServiceType> data, final Object testInstance
  ) throws NoSuchMethodException, NotFoundException, IllegalAccessException {
    data.setServiceInstance(
        (ServiceType) this.fieldGetterService
            .getFieldValueWithAnnotation(Subject.class, testInstance)
    );
  }

  private List<ContractInfo<ServiceType>>
      createContracts(final ContractRunnerData<ServiceType> data) throws Throwable {
    final List<ContractInfo<ServiceType>> contracts = new ArrayList<>();
    for (final Method method : data.getTestClass().getMethods()) {
      final List<ContractRule> annotations =
          Arrays.asList(method.getDeclaredAnnotationsByType(ContractRule.class));
      if (!annotations.isEmpty())
        this.contractCreationService.createContract(
            contracts, method, data
        );
    }
    return contracts;
  }

}
