package com.kodekonveyor.cdd.notintegrated;

import static org.mockito.Mockito.verify;

import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;

import com.kodekonveyor.cdd.ContractInfo;
import com.kodekonveyor.cdd.annotations.Context;
import com.kodekonveyor.cdd.annotations.ContractFactory;
import com.kodekonveyor.cdd.annotations.ContractRule;
import com.kodekonveyor.cdd.annotations.Subject;
import com.kodekonveyor.cdd.dto.ContractJunitTestData;
import com.kodekonveyor.cdd.impl.ContractCreationServiceImpl;
import com.kodekonveyor.cdd.impl.ContractInfoFactory;
import com.kodekonveyor.cdd.testartifacts.ExampleService;

//@RunWith(ContractRunner.class)
@Contract({
    "contract runner data is for example service",
    "contract method is CONTRACT_PASSING_RETURN"
})
public class ContractCreationServiceContract {

  @Autowired
  ContractInfoFactory<ExampleService> contractInfoFactory;

  @Subject
  @InjectMocks
  public ContractCreationServiceImpl<
      ExampleService> contractCreationServiceImpl;

  @ContractFactory
  public ContractInfo<ContractCreationServiceImpl<
      ExampleService>> it;

  @Context
  public ContractJunitTestData testData = new ContractJunitTestData();

  @ContractRule(
    "createContract creates contract based on the method in contract"
  )
  //@Test
  public void createContract() throws NoSuchMethodException, SecurityException,
      AssertionError, Throwable {

    /*
      ContractInfo<ExampleService> contract = mock(ContractInfo.class);
      // return value can be defined
      // * with a mock like above and the where clause below,
      // * with the rules set up in the context, or
      // * as a concrete value
      it.returns(contract)
        .where(
           on(contract).setSuiteData(testData.contractRunnerData),
           on(contract).setDefiningFunction(ContractJunitTestData.CONTRACT_PASSING_RETURN)
         )
        .when(contractCreationServiceImpl)
        .createContract(
           testData.contracts,
            testData.contractMethod, testData.contractRunnerData
        )
        .withSideEffect(on(testData.contracts).add(testData.contract))
     */
    ContractInfo<ExampleService> contract = contractCreationServiceImpl
        .createContract(
            testData.contracts,
            testData.contractMethod, testData.contractRunnerData
        );

    verifyReturnValue(contract, testData.contract);
    verify(testData.contracts).add(testData.contract);
  }

  private void verifyReturnValue(
      ContractInfo<ExampleService> contract,
      ContractInfo<ExampleService> contractMock
  ) {
    verify(contractMock).setSuiteData(testData.contractRunnerData);
    verify(contractMock)
        .setDefiningFunction(testData.contractMethod);
  }
}
