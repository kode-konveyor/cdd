package com.kodekonveyor.cdd.fields.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Service;

import com.kodekonveyor.cdd.exception.StackTraceSetterService;
import com.kodekonveyor.cdd.fields.FieldGetterService;

import javassist.NotFoundException;

@Service
public class FieldGetterServiceImpl
    implements FieldGetterService {

  @Autowired
  public StackTraceSetterService stackTraceSetterService;
  @Autowired
  private AutowireCapableBeanFactory beanFactory;

  @Override
  public <AnnotationClass extends Annotation> Object
      getFieldValueWithAnnotation(
          final Class<AnnotationClass> annotationClass,
          final Object testInstance
      ) throws IllegalAccessException,
          NoSuchMethodException, NotFoundException {
    final Field theField =
        getFieldWithAnnotation(annotationClass, testInstance);
    if (theField == null)
      throw (IllegalArgumentException) this.stackTraceSetterService
          .changeStackWithClass(
              new IllegalArgumentException(
                  "Annotation not found: @" + annotationClass.getSimpleName()
              ), testInstance.getClass()
          );
    return getServiceInstance(theField, testInstance);
  }

  @Override
  public <AnnotationClass extends Annotation> Field
      getFieldWithAnnotation(
          final Class<AnnotationClass> annotationClass,
          final Object testInstance
      ) {
    Field theField = null;
    for (final Field field : testInstance.getClass().getFields()) {

      final List<AnnotationClass> annotations =
          getAnnotationsFor(field, annotationClass);
      if (!annotations.isEmpty())
        theField = field;
    }
    return theField;
  }

  private <AnnotationClass extends Annotation> List<AnnotationClass>
      getAnnotationsFor(
          final Field field, final Class<AnnotationClass> annotationClass
      ) {
    return Arrays
        .asList(field.getDeclaredAnnotationsByType(annotationClass));
  }

  private Object
      getServiceInstance(
          final Field field, final Object testInstance
      ) throws IllegalAccessException {

    final Object instance = field.get(testInstance);
    if (null != instance)
      this.beanFactory.autowireBean(instance);
    return instance;
  }

}
