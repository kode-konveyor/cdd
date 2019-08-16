package com.kodekonveyor.cdd.exception;

public interface ThrowableTesterInterface {

  ThrowableTesterInterface assertMessageIs(String message);

  ThrowableTesterInterface
      assertException(Class<? extends Throwable> klass);

  ThrowableTesterInterface assertUnimplemented(Thrower thrower);

  ThrowableTesterInterface assertMessageMatches(String string);

  ThrowableTesterInterface assertMessageContains(String string);

  ThrowableTesterInterface
      assertStackFileName(int stackIndex, String string);

  ThrowableTesterInterface
      assertStackClass(int stackIndex, String string);

  ThrowableTesterInterface
      assertStackLineNumber(int stackIndex, int lineNumber);

  ThrowableTesterInterface
      assertStackMethod(int stackIndex, String string);

  ThrowableTesterInterface showStackTrace();

  ThrowableTesterData getData();

}
