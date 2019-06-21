package com.kodekonveyor.cdd.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kodekonveyor.cdd.FieldGetterService;

@Service
public class FieldGetterServiceImpl<ServiceClass>
    implements FieldGetterService<ServiceClass> {

  public FieldGetterServiceImpl() {
    super();
  }

  @Override
  public <T extends Annotation> ServiceClass getFieldWithAnnotation(
      Class<T> annotationClass, Object testInstance
  ) throws IllegalAccessException {
    Field theField = null;
    for (final Field field : testInstance.getClass().getFields()) {
      final List<T> annotations =
          getAnnotationsFor(field, annotationClass);
      if (!annotations.isEmpty())
        theField = field;
    }
    if (theField == null)
      throw new IllegalArgumentException(NO_SUBJECT + annotationClass);
    return getServiceInstance(theField, testInstance);
  }

  private <T extends Annotation> List<T>
      getAnnotationsFor(final Field field, Class<T> annotationClass) {
    return Arrays
        .asList(field.getDeclaredAnnotationsByType(annotationClass));
  }

  @SuppressWarnings("unchecked")
  private ServiceClass
      getServiceInstance(final Field field, Object testInstance)
          throws IllegalAccessException {

    return (ServiceClass) field.get(testInstance);
  }

}
