package com.kodekonveyor.cdd.exception;

import java.lang.reflect.Method;

import org.springframework.stereotype.Service;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

@Service
public class StackTraceCreatorService {

  private static final String JAVA_EXTENSION = ".java";

  public StackTraceElement[] createStackTrace(final Method method)
      throws NotFoundException {
    Class<?> declaringClass = method.getDeclaringClass();
    StackTraceElement[] stack = createStackTrace(method, declaringClass);
    return stack;
  }

  public StackTraceElement[] createStackTrace(
      final Method method, Class<?> declaringClass
  ) throws NotFoundException {
    StackTraceElement[] stack = new StackTraceElement[1];
    String className = declaringClass.getName();
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

  public StackTraceElement[] createStackTrace(Object testInstance)
      throws NotFoundException {
    Class<? extends Object> klass = testInstance.getClass();
    return createStackTrace(null, klass);
  }

}
