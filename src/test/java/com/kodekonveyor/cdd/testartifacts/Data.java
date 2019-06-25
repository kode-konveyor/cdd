package com.kodekonveyor.cdd.testartifacts;

import org.springframework.stereotype.Component;

import com.kodekonveyor.cdd.Specimen;

import lombok.EqualsAndHashCode;

@Component
@EqualsAndHashCode
public class Data implements Specimen {

  public static final Class<? extends RuntimeException> exceptionThrown =
      IllegalArgumentException.class;
  public static final Class<? extends RuntimeException> exceptionNotThrown =
      java.lang.UnsupportedOperationException.class;

  public final Integer goodReturnValue = 42;
  public final Integer goodParameter = 42;
  public final Integer parameterInducingException = 1;
  public final Integer illegalOutputValue = 1;
  public final String exceptionMessage =
      "bad parameter: " + parameterInducingException;
  public final String noteExistingExceptionMessage =
      "good parameter";

  @Override
  public void init() {
  }

}
