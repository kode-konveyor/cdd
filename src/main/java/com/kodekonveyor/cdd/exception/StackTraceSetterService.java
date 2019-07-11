package com.kodekonveyor.cdd.exception;

import java.lang.reflect.Method;

public interface StackTraceSetterService {

  Throwable
      changeStackWithClass(Throwable throwable, Class<?> declaringClass);

  Throwable
      changeStackWithMethod(Throwable throwable, Method method);

}
