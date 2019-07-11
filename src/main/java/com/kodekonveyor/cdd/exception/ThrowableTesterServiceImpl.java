package com.kodekonveyor.cdd.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class ThrowableTesterServiceImpl implements ThrowableTesterService {

  public static ThrowableTester assertThrows(final Thrower thrower) {
    ThrowableTester tester;
    try (
        ConfigurableApplicationContext ctx =
            new ClassPathXmlApplicationContext("applicationContext.xml")
    ) {
      tester = (ThrowableTester) ctx.getBean("throwableTester");
    }

    try {
      thrower.throwException();
    } catch (final Throwable exception) {// NOPMD
      tester.data.thrown = exception;
    }
    if (tester.data.thrown == null)
      fail("no exception thrown");
    return tester;
  }

  public ThrowableTesterServiceImpl() {
    super();
  }

  @Override
  public ThrowableTester
      assertMessageIs(final String message, ThrowableTester tester) {
    ThrowableTesterData data = tester.data;
    assertEquals(message, data.thrown.getMessage());
    return tester;
  }

  @Override
  public Throwable getException(ThrowableTester tester) {
    ThrowableTesterData data = tester.data;
    return data.thrown;
  }

  @Override
  public ThrowableTester
      assertException(
          final Class<? extends Throwable> klass, ThrowableTester tester
      ) {
    ThrowableTesterData data = tester.data;
    final String message =
        String
            .format(
                "expected %s but got %s", klass,
                ExceptionUtils.getStackTrace(data.thrown)
            );
    assertEquals(message, klass, data.thrown.getClass());
    return tester;
  }

  @Override
  public ThrowableTester
      assertUnimplemented(final Thrower thrower, ThrowableTester tester) {
    assertThrows(thrower).assertException(UnsupportedOperationException.class);
    return tester;
  }

  @Override
  public ThrowableTester
      assertMessageMatches(String string, ThrowableTester tester) {
    ThrowableTesterData data = tester.data;
    assertNotNull("no message of the exception", data.thrown.getMessage());
    assertTrue(
        "message does not match. \nexpected: " + string + "\n got:" +
            data.thrown.getMessage(),
        data.thrown.getMessage().matches(string)
    );
    return tester;
  }

  @Override
  public ThrowableTester
      assertMessageContains(String string, ThrowableTester tester) {
    ThrowableTesterData data = tester.data;
    assertTrue(
        "message does not contain: " + string + "\n got:" +
            data.thrown.getMessage(),
        data.thrown.getMessage().contains(string)
    );
    return tester;
  }

  @Override
  public ThrowableTester assertStackFileName(
      int stackIndex, String string, ThrowableTester tester
  ) {
    StackTraceElement stackElement = getStackTraceElement(stackIndex, tester);
    assertEquals(string, stackElement.getFileName());
    return tester;
  }

  @Override
  public ThrowableTester
      assertStackClass(int stackIndex, String string, ThrowableTester tester) {
    StackTraceElement stackElement = getStackTraceElement(stackIndex, tester);
    assertEquals(string, stackElement.getClassName());
    return tester;
  }

  @Override
  public ThrowableTester assertStackLineNumber(
      int stackIndex, int lineNumber, ThrowableTester tester
  ) {
    StackTraceElement stackElement = getStackTraceElement(stackIndex, tester);
    assertEquals(lineNumber, stackElement.getLineNumber());
    return tester;
  }

  @Override
  public ThrowableTester
      assertStackMethod(int stackIndex, String string, ThrowableTester tester) {
    StackTraceElement stackElement = getStackTraceElement(stackIndex, tester);
    assertEquals(string, stackElement.getMethodName());
    return tester;
  }

  private StackTraceElement
      getStackTraceElement(int stackIndex, ThrowableTester tester) {
    ThrowableTesterData data = tester.data;
    return data.thrown.getStackTrace()[stackIndex];
  }

  @Override
  public ThrowableTester showStackTrace(ThrowableTester tester) {
    ThrowableTesterData data = tester.data;
    data.thrown.printStackTrace();
    return tester;
  }

}
