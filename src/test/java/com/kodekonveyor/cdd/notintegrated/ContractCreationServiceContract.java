package com.kodekonveyor.cdd.notintegrated;

import static org.mockito.Mockito.verify;

import org.mockito.InjectMocks;

import com.kodekonveyor.cdd.ContractInfo;
import com.kodekonveyor.cdd.annotation.Context;
import com.kodekonveyor.cdd.annotation.ContractFactory;
import com.kodekonveyor.cdd.annotation.ContractRule;
import com.kodekonveyor.cdd.annotation.Subject;
import com.kodekonveyor.cdd.build.impl.ContractCreationServiceImpl;
import com.kodekonveyor.cdd.dto.ContractJunitTestData;
import com.kodekonveyor.cdd.testartifacts.ExampleService;

//@RunWith(ContractRunner.class)
@Contract({
    "contract runner data is for example service",
    "contract method is CONTRACT_PASSING_RETURN"
})
public class ContractCreationServiceContract {

  @Subject
  @InjectMocks
  public ContractCreationServiceImpl<
      ExampleService> contractCreationServiceImpl;

  @ContractFactory
  public ContractInfo<ContractCreationServiceImpl<
      ExampleService>> it; //NOPMD ShortVariable

  @Context
  public ContractJunitTestData testData = new ContractJunitTestData();

  @ContractRule(
    "createContract creates contract based on the method in contract"
  )
  public void createContract() throws NoSuchMethodException,
      AssertionError, Throwable {
    contractCreationServiceImpl
        .createContract(
            testData.contracts,
            testData.contractMethod, testData.contractRunnerData
        );

    verifyReturnValue(testData.contract);
    verify(testData.contracts).add(testData.contract);
  }

  private void verifyReturnValue(
      final ContractInfo<ExampleService> contractMock
  ) {
    verify(contractMock).setSuiteData(testData.contractRunnerData);
    verify(contractMock)
        .setDefiningFunction(testData.contractMethod);
  }
}
