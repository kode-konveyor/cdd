package com.kodekonveyor.cdd.exception.impl;

import java.lang.reflect.Method;

import org.springframework.stereotype.Service;

import com.kodekonveyor.cdd.exception.StackTraceSetterService;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

@Service
public class StackTraceSetterServiceImpl implements StackTraceSetterService {

  private static final String JAVA_EXTENSION = ".java";

  private StackTraceElement[] createStackTrace(
      final Method method, final Class<?> declaringClass
  ) throws NotFoundException {
    final StackTraceElement[] stack = new StackTraceElement[1];
    String className;
    if (null == declaringClass)
      className = method.getDeclaringClass().getName();
    else
      className = declaringClass.getName();
    final String fileName = createFilenameFromClassName(className);
    className = className.replaceAll("\\$.*", "");
    String methodName;
    int lineNumber;
    if (null == method) {
      methodName = "";
      lineNumber = 0;
    } else {
      methodName = method.getName();
      final ClassPool classPool = ClassPool.getDefault();
      final CtClass classCode = classPool.get(className);
      final CtMethod methodCode = classCode.getDeclaredMethod(method.getName());
      lineNumber = methodCode.getMethodInfo().getLineNumber(0);
    }
    stack[0] =
        new StackTraceElement(
            className, methodName, fileName, lineNumber
        );
    return stack;
  }

  private String createFilenameFromClassName(final String className) {
    String fileName = className;
    fileName = fileName.replaceAll("\\$.*", "");
    fileName = fileName.replaceAll("^.*\\.", "") + JAVA_EXTENSION;
    return fileName;
  }

  @Override
  public Throwable
      changeStackWithClass(
          final Throwable throwable, final Class<?> declaringClass
      ) {
    StackTraceElement[] stack;
    try {
      stack = createStackTrace(null, declaringClass);
    } catch (final NotFoundException e) {
      return e; //NOPMD UnnecessaryLocalBeforeReturn
    }
    throwable.setStackTrace(stack);
    return throwable;
  }

  @Override
  public Throwable
      changeStackWithMethod(final Throwable throwable, final Method method) {
    StackTraceElement[] stack;
    try {
      stack = createStackTrace(method, null);
    } catch (final NotFoundException e) {
      return e; //NOPMD UnnecessaryLocalBeforeReturn
    }
    throwable.setStackTrace(stack);
    return throwable;
  }

}
