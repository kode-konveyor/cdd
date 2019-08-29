package com.kodekonveyor.cdd;

import static org.mockito.Mockito.doReturn;

import org.junit.runner.Description;

import com.kodekonveyor.cdd.build.impl.ChildDescriptionServiceImpl;
import com.kodekonveyor.cdd.run.dto.ContractRunnerData;
import com.kodekonveyor.cdd.testartifacts.ExampleService;

public interface ChildDescriptionServiceStubs {

  ContractRunnerData<ExampleService> DATA = CDDContractTestData.DATA;

  Description DESCRIPTION = CDDContractTestData.DESCRIPTION;

  static void behaviour(
      final ChildDescriptionServiceImpl<ExampleService> childDescriptionService
  ) {

    doReturn(DESCRIPTION).when(childDescriptionService)
        .describeChild(CDDContractTestData.THROWING_CONTRACT, DATA);
    doReturn(DESCRIPTION).when(childDescriptionService)
        .describeChild(CDDContractTestData.NOT_THROWING_THROWING_CONTRACT, DATA);
    doReturn(DESCRIPTION).when(childDescriptionService)
        .describeChild(
            CDDContractTestData.THROWING_CONTRACT_THROWING_ANOTHER_EXCEPTION,
            DATA
        );
    doReturn(DESCRIPTION).when(childDescriptionService)
        .describeChild(CDDContractTestData.OTHER_MESSAGE_THROWING_CONTRACT, DATA);
    doReturn(DESCRIPTION).when(childDescriptionService)
        .describeChild(
            CDDContractTestData.RETURNING_CONTRACT_WITH_THROWING_STUB,
            CDDContractTestData.RETURNING_SERVICE_WITH_THROWING_STUB_DATA
        );
    doReturn(DESCRIPTION).when(childDescriptionService)
        .describeChild(
            CDDContractTestData.RETURNING_CONTRACT_WITH_THROWING_SERVICE, DATA
        );
    doReturn(DESCRIPTION).when(childDescriptionService)
        .describeChild(
            CDDContractTestData.RETURNING_CONTRACT_CALLED_WITH_BAD_PARAMETERS,
            CDDContractTestData.BAD_PARAMETERS_DATA
        );
    doReturn(CDDContractTestData.DESCRIPTION).when(childDescriptionService)
        .describeChild(
            CDDContractTestData.RETURNING_CONTRACT_RETURNING_NULL,
            CDDContractTestData.DATA
        );
    doReturn(CDDContractTestData.DESCRIPTION).when(childDescriptionService)
        .describeChild(
            CDDContractTestData.RETURNING_CONTRACT_WITH_FAILING_PREDICATE,
            CDDContractTestData.DATA
        );
    doReturn(CDDContractTestData.DESCRIPTION).when(childDescriptionService)
        .describeChild(
            CDDContractTestData.CONTRACT_EXPECTING_NULL_RETURN_BUT_SERVICE_RETURNING_OTHER_VALUE,
            CDDContractTestData.DATA
        );

  }

}
