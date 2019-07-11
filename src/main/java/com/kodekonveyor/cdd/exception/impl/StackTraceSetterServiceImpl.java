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
      final Method method, Class<?> declaringClass
  ) throws NotFoundException {
    StackTraceElement[] stack = new StackTraceElement[1];
    String className;
    if (null == declaringClass) {
      className = method.getDeclaringClass().getName();
    } else
      className = declaringClass.getName();
    String fileName = createFilenameFromClassName(className);
    className = className.replaceAll("\\$.*", "");
    String methodName;
    int lineNumber;
    if (null == method) {
      methodName = "";
      lineNumber = 0;
    } else {
      methodName = method.getName();
      ClassPool classPool = ClassPool.getDefault();
      CtClass classCode = classPool.get(className);
      CtMethod methodCode = classCode.getDeclaredMethod(method.getName());
      lineNumber = methodCode.getMethodInfo().getLineNumber(0);
    }
    stack[0] =
        new StackTraceElement(
            className, methodName, fileName, lineNumber
        );
    return stack;
  }

  private String createFilenameFromClassName(String className) {
    String fileName = className;
    fileName = fileName.replaceAll("\\$.*", "");
    fileName = fileName.replaceAll("^.*\\.", "") + JAVA_EXTENSION;
    return fileName;
  }

  @Override
  public Throwable
      changeStackWithClass(Throwable throwable, Class<?> declaringClass) {
    StackTraceElement[] stack;
    try {
      stack = this.createStackTrace(null, declaringClass);
    } catch (NotFoundException e) {
      return e;
    }
    throwable.setStackTrace(stack);
    return throwable;
  }

  @Override
  public Throwable
      changeStackWithMethod(Throwable throwable, Method method) {
    StackTraceElement[] stack;
    try {
      stack = this.createStackTrace(method, null);
    } catch (NotFoundException e) {
      return e;
    }
    throwable.setStackTrace(stack);
    return throwable;
  }

}
