package com.kodekonveyor.cdd.impl;

import static org.mockito.Mockito.mock;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.runner.Description;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodekonveyor.cdd.ContractInfo;
import com.kodekonveyor.cdd.RunnerDataCreationService;
import com.kodekonveyor.cdd.annotations.Contract;
import com.kodekonveyor.cdd.annotations.Subject;
import com.kodekonveyor.cdd.annotations.TestData;
import com.kodekonveyor.cdd.dto.ContractRunnerData;

@Service
public class RunnerDataCreationServiceImpl<ServiceClass>
    implements RunnerDataCreationService<ServiceClass> {

  @Autowired
  FieldGetterServiceImpl<ServiceClass> fieldGetterService;

  @Autowired
  ChildDescriptionServiceImpl<ServiceClass> childDescriptionService;

  @Override
  public ContractRunnerData<ServiceClass>
      makeRunnerDataFromTestClass(final Class<? extends Object> testClass)
          throws InstantiationException, IllegalAccessException,
          InvocationTargetException, NoSuchMethodException {
    ContractRunnerData<ServiceClass> data = new ContractRunnerData<>();
    data.setTestClass(testClass);
    data.setTestInstance(testClass.getConstructor().newInstance());
    MockitoAnnotations.initMocks(data.getTestInstance());
    data.setServiceInstance(
        fieldGetterService.getFieldWithAnnotation(
            Subject.class, data.getTestInstance()
        )
    );
    data.setTestData(
        fieldGetterService.getFieldWithAnnotation(
            TestData.class, data.getTestInstance()
        )
    );
    data.setContracts(listContracts(data));
    System.out.println("contracts for" + testClass + ":" + data.getContracts());
    data.setSuiteDescription(Description.createSuiteDescription(testClass));
    for (final ContractInfo<ServiceClass> child : data.getContracts())
      data.getSuiteDescription()
          .addChild(childDescriptionService.describeChild(child, data));
    return data;
  }

  private List<ContractInfo<ServiceClass>>
      listContracts(ContractRunnerData<ServiceClass> data) {
    final List<ContractInfo<ServiceClass>> contracts = new ArrayList<>();
    for (final Method method : data.getTestClass().getMethods()) {
      List<Contract> annotations =
          Arrays.asList(method.getDeclaredAnnotationsByType(Contract.class));
      if (!annotations.isEmpty()) {
        final ContractInfo<ServiceClass> contract = createContractInfo(data);
        contract.setDefiningFunction(method.getName());
        try {
          method.invoke(data.getTestInstance(), data.getTestData(), contract);
        } catch (
            IllegalAccessException | IllegalArgumentException |
            InvocationTargetException e
        ) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        contracts.add(contract);
      }
    }
    return contracts;
  }

  @SuppressWarnings("unchecked")
  private ContractInfo<ServiceClass>
      createContractInfo(ContractRunnerData<ServiceClass> data) {
    return new ContractInfo<ServiceClass>(
        (ServiceClass) mock(data.getServiceInstance().getClass())
    );
  }

}
