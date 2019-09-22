package com.kodekonveyor.cdd;

import java.text.MessageFormat;

import org.springframework.stereotype.Component;

import lombok.EqualsAndHashCode;

@Component
@EqualsAndHashCode
public class TestContractTestData {//NOPMD ClassNamingConventions

  public static final Class<? extends RuntimeException> EXCEPTION_THROWN =
      IllegalArgumentException.class;
  public static final Class<? extends RuntimeException> EXCEPTION_NOT_THROWN =
      UnsupportedOperationException.class;

  public static final Integer GOOD_RETURN_VALUE = 42;
  public static final Integer GOOD_PARAMETER = 42;
  public static final Integer PARAMETER_INDUCING_EXCEPTION = 1;
  public static final Integer ILLEGAL_OUTPUT_VALUE = 1;
  public static final String EXCEPTION_MESSAGE =
      "bad parameter: " + PARAMETER_INDUCING_EXCEPTION;
  public static final String NOT_EXISTING_EXCEPTION_MESSAGE =
      "good parameter";
  public static final String CAN_NOT_SET_FIELD_MESSAGE =
      ".*Can not set .* field .*TestContract.it to .*";
  public static final String WRONG_PARAMETERS_MESSAGE =
      "java.lang.IllegalArgumentException: wrong number of arguments";
  public static final int METHOD_LINENUMBER = 35;
  public static final String METHOD_NAME = "methodWithParameter";
  public static final String CLASS_FILE_NAME = "TestContract.java";
  public static final String FULL_CLASSNAME =
      "com.kodekonveyor.cdd.testartifacts.TestContract";
  public static final int METHOD_LINE_NUMBER = 28;
  public static final String IT_FIELD_NAME = "it";
  public final static String CONTRACT_PASSING_RETURN =
      "contractPassingReturn";
  public final static String RETURN_DEATIL_NAME = "Returns 42";
  public final static String RETURN_DETAIL_FUNCTION_NAME =
      "returnDetailPredicate";
  public static final String SERVICE_FILE_NAME = "ExampleService.java";
  public static final String BAD_PARAMETER = "bad parameter: 1";
  public static final String NO_EXCEPTION_MESSAGE =
      "Expected IllegalArgumentException, but no exception thrown";
  public static final String BAD_EXCEPTION_MESSAGE_FORMAT =
      "Expected ArithmeticException({0}), but see cause";

  public static final String BAD_EXCEPTION_MESSAGE = MessageFormat.format(
      TestContractTestData.BAD_EXCEPTION_MESSAGE_FORMAT,
      TestContractTestData.BAD_PARAMETER
  );
  public static final String BAD_MESSAGE_MESSAGE =
      "Expected IllegalArgumentException(bad parameter: 42), but see cause";

}
