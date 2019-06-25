package com.kodekonveyor.cdd.impl;

import static org.mockito.Mockito.mock;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.runner.Description;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Service;

import com.kodekonveyor.cdd.ContractInfo;
import com.kodekonveyor.cdd.RunnerDataCreationService;
import com.kodekonveyor.cdd.annotations.Contract;
import com.kodekonveyor.cdd.annotations.Sample;
import com.kodekonveyor.cdd.annotations.Subject;
import com.kodekonveyor.cdd.annotations.TestData;
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

    Object testInstance = testClass.getConstructor().newInstance();
    beanFactory.autowireBean(testInstance);
    data.setTestInstance(testInstance);
    DataFactory dataFactory =
        (DataFactory) fieldGetterService.getFieldWithAnnotation(
            TestData.class, data.getTestInstance()
        );
    data.setTestData(dataFactory.createTestData());
    setServiceInstanceFrom(data, myAnswer);
    setMockFrom(data);
    data.setContracts(createContracts(data));
    data.setSuiteDescription(Description.createSuiteDescription(testClass));
    for (final ContractInfo<ServiceClass> child : data.getContracts())
      data.getSuiteDescription()
          .addChild(childDescriptionService.describeChild(child, data));
    return data;
  }

  @SuppressWarnings("unchecked")
  private void setMockFrom(ContractRunnerData<ServiceClass> data) {
    data.setMock((ServiceClass) mock(data.getServiceInstance().getClass()));
  }

  @SuppressWarnings("unchecked")
  private void setServiceInstanceFrom(
      ContractRunnerData<ServiceClass> data, Object myAnswer
  )
      throws Throwable {
    Object testInstance = data.getTestInstance();
    ServiceClass serviceInstance =
        (ServiceClass) fieldGetterService.getFieldWithAnnotation(
            Subject.class, testInstance
        );
    if (null == serviceInstance)
      serviceInstance = (ServiceClass) myAnswer;
    if (null == serviceInstance)
      serviceInstance = createServiceInstanceWithGetSample(data);
    data.setServiceInstance(serviceInstance);
  }

  @SuppressWarnings("unchecked")
  private ServiceClass createServiceInstanceWithGetSample(
      ContractRunnerData<ServiceClass> data
  ) throws Throwable {
    Object testInstance = data.getTestInstance();
    ServiceClass instance = null;
    for (Method method : testInstance.getClass().getMethods()) {
      Sample annotation = method.getAnnotation(Sample.class);
      if (null != annotation)
        try {
          instance =
              (ServiceClass) method.invoke(testInstance, data.getTestData());
        } catch (InvocationTargetException e) {
          throw e.getCause();
        }
    }
    return instance;
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
