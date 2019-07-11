package com.kodekonveyor.cdd.exception;

public interface ThrowableTesterService {

  ThrowableTester assertMessageIs(String message, ThrowableTester tester);

  Throwable getException(ThrowableTester tester);

  ThrowableTester
      assertException(Class<? extends Throwable> klass, ThrowableTester tester);

  ThrowableTester assertUnimplemented(Thrower thrower, ThrowableTester tester);

  ThrowableTester assertMessageMatches(String string, ThrowableTester tester);

  ThrowableTester assertMessageContains(String string, ThrowableTester tester);

  ThrowableTester assertStackFileName(
      int stackIndex, String string, ThrowableTester tester
  );

  ThrowableTester
      assertStackClass(int stackIndex, String string, ThrowableTester tester);

  ThrowableTester assertStackLineNumber(
      int stackIndex, int lineNumber, ThrowableTester tester
  );

  ThrowableTester
      assertStackMethod(int stackIndex, String string, ThrowableTester tester);

  ThrowableTester showStackTrace(ThrowableTester tester);

}
