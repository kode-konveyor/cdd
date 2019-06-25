package com.kodekonveyor.cdd;

import java.lang.annotation.Annotation;

public interface FieldGetterService {

  String NO_SUBJECT =
      "Test Class should have a public test data field annotated as @TestData and @InjectMocks " +
          "and a public service field annotated as @Subject and @InjectMocks while looking for ";

  <T extends Annotation> Object getFieldWithAnnotation(
      Class<T> annotationClass, Object testInstance
  ) throws IllegalArgumentException, IllegalAccessException;

}
