package com.kodekonveyor.cdd.assemble;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import org.springframework.stereotype.Service;

import com.kodekonveyor.cdd.ContractInfo;

@Service
public class ContractInfoServiceImpl<ServiceType>
    implements ContractInfoService<ServiceType> {

  @Override
  public ContractInfo<ServiceType> returns(
      final Object returnValue, final ContractInfo<ServiceType> contractInfo
  ) {
    final ContractInfoData<ServiceType> data = contractInfo.getData();
    data.setReturnValue(returnValue);
    data.setStub(
        doReturn(returnValue).when(mock(data.getService().getClass()))
    );
    return contractInfo;
  }

  @SuppressWarnings("unchecked")
  @Override
  public ServiceType when(
      final ContractInfo<ServiceType> contractInfo
  ) {
    final ContractInfoData<ServiceType> data = contractInfo.getData();
    return (ServiceType) data.getStub();
  }

  @Override
  public ContractInfo<ServiceType> throwing(
      final Class<? extends RuntimeException> exceptionClass,
      final String exceptionMessage,
      final ContractInfo<ServiceType> contractInfo
  ) {
    final ContractInfoData<ServiceType> data = contractInfo.getData();
    data.setExceptionClass(exceptionClass);
    data.setExceptionMessage(exceptionMessage);
    try {
      final Class<? extends Object> serviceClass = data.getService().getClass();
      final Object serviceMock = mock(serviceClass);
      data.setStub(
          doThrow(exceptionClass.getConstructor(String.class)
              .newInstance(exceptionMessage)
          )
              .when(serviceMock)
      );
    } catch (
        InstantiationException | IllegalAccessException |
        IllegalArgumentException | InvocationTargetException |
        NoSuchMethodException | SecurityException e
    ) {
      throw new AssertionError("cannot stub", e);
    }
    return contractInfo;
  }

  @Override
  public ContractInfo<ServiceType> suchThat(
      final String[] returnCheckDetails,
      final ContractInfo<ServiceType> contractInfo
  ) {
    final ContractInfoData<ServiceType> data = contractInfo.getData();
    data.getCheckedReturnDetails().addAll(Arrays.asList(returnCheckDetails));
    return contractInfo;
  }

}
