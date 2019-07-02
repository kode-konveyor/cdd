package com.kodekonveyor.cdd.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.lang3.exception.ExceptionUtils;

public class ThrowableTester {

  static ThrowableTesterFactory throwableTesterFactory =
      new ThrowableTesterFactory();
  private Throwable thrown;

  public static ThrowableTester assertThrows(final Thrower thrower) {
    ThrowableTester tester = throwableTesterFactory.getObject();
    try {
      thrower.throwException();
    } catch (final Throwable exception) {// NOPMD
      tester.thrown = exception;
    }
    if (tester.thrown == null)
      fail("no exception thrown");
    return tester;
  }

  public ThrowableTester assertMessageIs(final String message) {
    assertEquals(message, thrown.getMessage());
    return this;
  }

  public Throwable getException() {
    return thrown;
  }

  public ThrowableTester
      assertException(final Class<? extends Throwable> klass) {
    final String message =
        String
            .format(
                "expected %s but got %s", klass,
                ExceptionUtils.getStackTrace(thrown)
            );
    assertEquals(message, klass, thrown.getClass());
    return this;
  }

  public ThrowableTester assertUnimplemented(final Thrower thrower) {
    assertThrows(thrower).assertException(UnsupportedOperationException.class);
    return this;
  }

  public ThrowableTester assertMessageMatches(String string) {
    assertNotNull("no message of the exception", thrown.getMessage());
    assertTrue(
        "message does not match. \nexpected: " + string + "\n got:" +
            thrown.getMessage(),
        thrown.getMessage().matches(string)
    );
    return this;
  }

  public ThrowableTester assertMessageContains(String string) {
    assertTrue(
        "message does not contain: " + string + "\n got:" +
            thrown.getMessage(),
        thrown.getMessage().contains(string)
    );
    return this;
  }

  public ThrowableTester assertStackFileName(int stackIndex, String string) {
    StackTraceElement stackElement = getStackTraceElement(stackIndex);
    assertEquals(string, stackElement.getFileName());
    return this;
  }

  public ThrowableTester assertStackClass(int stackIndex, String string) {
    StackTraceElement stackElement = getStackTraceElement(stackIndex);
    assertEquals(string, stackElement.getClassName());
    return this;
  }

  public ThrowableTester assertStackLineNumber(int stackIndex, int lineNumber) {
    StackTraceElement stackElement = getStackTraceElement(stackIndex);
    assertEquals(lineNumber, stackElement.getLineNumber());
    return this;
  }

  public ThrowableTester assertStackMethod(int stackIndex, String string) {
    StackTraceElement stackElement = getStackTraceElement(stackIndex);
    assertEquals(string, stackElement.getMethodName());
    return this;
  }

  private StackTraceElement getStackTraceElement(int stackIndex) {
    return thrown.getStackTrace()[stackIndex];
  }

  public ThrowableTester showStackTrace() {
    thrown.printStackTrace();
    return this;
  }

}
