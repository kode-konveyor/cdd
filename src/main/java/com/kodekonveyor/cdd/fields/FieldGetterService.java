package com.kodekonveyor.cdd.fields;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import javassist.NotFoundException;

public interface FieldGetterService {

  <AnnotationClass extends Annotation> Object getFieldValueWithAnnotation(
      Class<AnnotationClass> annotationClass, Object testInstance
  ) throws IllegalAccessException,
      NoSuchMethodException, NotFoundException;

  <AnnotationClass extends Annotation> Field getFieldWithAnnotation(
      Class<AnnotationClass> annotationClass, Object testInstance
  );

}
