package com.kodekonveyor.cdd.dto;

import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.kodekonveyor.cdd.ContractInfo;
import com.kodekonveyor.cdd.impl.ContractInfoFactory;
import com.kodekonveyor.cdd.notintegrated.ContextItem;
import com.kodekonveyor.cdd.notintegrated.ContextRule;
import com.kodekonveyor.cdd.testartifacts.ExampleService;
import com.kodekonveyor.cdd.testartifacts.TestContract;

public class ContractJunitTestData {

  private static final String IT_FIELD_NAME = "it";

  public final static String CONTRACT_PASSING_RETURN =
      "contract_passing_return";

  @Autowired
  ContractInfoFactory<ExampleService> contractInfoFactory;

  @ContextItem
  private ExampleService exampleService;// = mock(ExampleService.class);

  @ContextItem
  private TestContract contractInstance;// = mock(TestContract.class);

  @ContextItem
  public ContractInfo<ExampleService> contract;// = mock(ContractInfo.class);
  @ContextItem
  public ContractRunnerData<ExampleService> contractRunnerData;// = mock(ContractRunnerData.class);

  @ContextItem
  public List<ContractInfo<ExampleService>> contracts;// = mock(List.class);

  public Method contractMethod;

  void initialize() {
    try {
      contractMethodIsContractPassingReturn();
      contractRunnerDataContainsDataForExampleService();
      when(contractInfoFactory.getObject())
          .thenReturn(contract);

    } catch (
        NoSuchMethodException | SecurityException | NoSuchFieldException e
    ) {
      throw new IllegalArgumentException("should not happen");
    }
  }

  @ContextRule("contract method is CONTRACT_PASSING_RETURN")
  private void contractMethodIsContractPassingReturn()
      throws NoSuchMethodException {
    contractMethod =
        TestContract.class.getMethod(CONTRACT_PASSING_RETURN);
  }

  @ContextRule("contract runner data is for example service")
  private void contractRunnerDataContainsDataForExampleService()
      throws NoSuchFieldException {
    when(contractRunnerData.getItField())
        .thenReturn(TestContract.class.getField(IT_FIELD_NAME));
    when(contractRunnerData.getTestInstance()).thenReturn(contractInstance);
    when(contractRunnerData.getServiceInstance())
        .thenReturn(exampleService);
  }

}