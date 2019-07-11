package com.kodekonveyor.cdd.assemble;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import java.lang.reflect.InvocationTargetException;

import org.springframework.stereotype.Service;

import com.kodekonveyor.cdd.ContractInfo;

@Service
public class ContractInfoServiceImpl<ServiceClass>
    implements ContractInfoService<ServiceClass> {

  @Override
  @SuppressWarnings("unchecked")
  public ServiceClass returns(
      final Object returnValue, ContractInfo<ServiceClass> contractInfo
  ) {
    ContractInfoData<ServiceClass> data = contractInfo.getData();
    data.setReturnValue(returnValue);
    data.setStub(
        doReturn(returnValue).when(mock(data.getService().getClass()))
    );
    return (ServiceClass) data.getStub();
  }

  @Override
  @SuppressWarnings("unchecked")
  public ServiceClass throwing(
      final Class<? extends RuntimeException> exceptionClass,
      final String exceptionMessage, ContractInfo<ServiceClass> contractInfo
  ) {
    ContractInfoData<ServiceClass> data = contractInfo.getData();
    data.setExceptionClass(exceptionClass);
    data.setExceptionMessage(exceptionMessage);
    try {
      Class<? extends Object> serviceClass = data.getService().getClass();
      Object serviceMock = mock(serviceClass);
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
    return (ServiceClass) data.getStub();
  }

}
