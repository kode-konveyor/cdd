package com.kodekonveyor.cdd.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Service;

import com.kodekonveyor.cdd.FieldGetterService;

@Service
public class FieldGetterServiceImpl
    implements FieldGetterService {

  @Autowired
  private AutowireCapableBeanFactory beanFactory;

  public FieldGetterServiceImpl() {
    super();
  }

  @Override
  public <T extends Annotation> Object getFieldValueWithAnnotation(
      Class<T> annotationClass, Object testInstance
  ) throws IllegalArgumentException, IllegalAccessException {
    Field theField = getFieldWithAnnotation(annotationClass, testInstance);
    if (theField == null)
      return null;
    return getServiceInstance(theField, testInstance);
  }

  public <T extends Annotation> Field
      getFieldWithAnnotation(Class<T> annotationClass, Object testInstance) {
    Field theField = null;
    for (final Field field : testInstance.getClass().getFields()) {

      final List<T> annotations =
          getAnnotationsFor(field, annotationClass);
      if (!annotations.isEmpty())
        theField = field;
    }
    return theField;
  }

  private <T extends Annotation> List<T>
      getAnnotationsFor(final Field field, Class<T> annotationClass) {
    return Arrays
        .asList(field.getDeclaredAnnotationsByType(annotationClass));
  }

  private Object
      getServiceInstance(
          final Field field, Object testInstance
      ) throws IllegalArgumentException, IllegalAccessException {

    Object instance = field.get(testInstance);
    if (null != instance)
      beanFactory.autowireBean(instance);
    return instance;
  }

}
