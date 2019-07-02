package com.kodekonveyor.cdd;

import java.lang.annotation.Annotation;

import javassist.NotFoundException;

public interface FieldGetterService {

  <T extends Annotation> Object getFieldValueWithAnnotation(
      Class<T> annotationClass, Object testInstance
  ) throws IllegalArgumentException, IllegalAccessException,
      NoSuchMethodException, SecurityException, NotFoundException;

}
