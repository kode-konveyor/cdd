package com.kodekonveyor.cdd;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.kodekonveyor.cdd.dto.ContractRunnerData;
import com.kodekonveyor.cdd.impl.ContractCreationServiceImpl;
import com.kodekonveyor.cdd.impl.ContractInfoFactory;
import com.kodekonveyor.cdd.testartifacts.ExampleService;
import com.kodekonveyor.cdd.testartifacts.TestContract;
import com.kodekonveyor.cdd.testdata.ContractTestData;

@RunWith(MockitoJUnitRunner.class)
public class ContractCreationServiceTest {

  private static final String IT_FIELD_NAME = "it";

  @InjectMocks
  public ContractCreationServiceImpl<
      ExampleService> contractCreationServiceImpl;

  @Mock
  private ContractInfoFactory<ExampleService> contractInfoFactory;

  @Mock
  private List<ContractInfo<ExampleService>> contracts;
  @Mock
  private ContractRunnerData<ExampleService> contractRunnerData;
  @Mock
  private ExampleService exampleService;
  @Mock
  private TestContract contractInstance;
  @Mock
  public ContractInfo<ExampleService> contract;

  private Method contractMethod;

  private ContractInfo<ExampleService> returnValue;

  @Before
  public void setUp() throws Throwable {
    contractMethod =
        TestContract.class.getMethod(ContractTestData.CONTRACT_PASSING_RETURN);

    when(contractInfoFactory.getObject())
        .thenReturn(contract);
    when(contractRunnerData.getItField())
        .thenReturn(TestContract.class.getField(IT_FIELD_NAME));
    when(contractRunnerData.getTestInstance()).thenReturn(contractInstance);
    when(contractRunnerData.getServiceInstance())
        .thenReturn(exampleService);
    returnValue = contractCreationServiceImpl
        .createContract(
            contracts,
            contractMethod, contractRunnerData
        );

  }

  @Test
  public void suite_data_is_set_to_the_data_parameter() {
    verify(contract).setSuiteData(contractRunnerData);
  }

  @Test
  public void defining_function_is_set_to_the_name_of_the_method_parameter() {
    verify(contract)
        .setDefiningFunction(contractMethod);
  }

  @Test
  public void service_instance_is_set_to_the_service_instance_from_the_data() {
    verify(contract).setService(exampleService);
  }

  @Test
  public void the_contract_is_added_to_the_list_of_contracts_in_parameter() {
    verify(contracts).add(contract);
  }

  @Test
  public void
      thecontract_is_set_as_the_it_field_of_the_test_instance_of_the_data() {
    assertEquals(returnValue, contractInstance.it);
  }

  @Test
  public void returns_the_created_contract() {
    assertEquals(returnValue, contract);
  }

  @Test
  public void calls_the_method_in_the_method_parameter() {
    verify(contractInstance).contract_passing_return();
  }

}
