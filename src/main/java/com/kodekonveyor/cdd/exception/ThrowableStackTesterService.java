package com.kodekonveyor.cdd.exception;

public interface ThrowableStackTesterService {

  ThrowableTesterInterface assertStackFileName(
      int stackIndex, String string, ThrowableTesterInterface tester
  );

  ThrowableTesterInterface assertStackClass(
      int stackIndex, String string, ThrowableTesterInterface tester
  );

  ThrowableTesterInterface assertStackLineNumber(
      int stackIndex, int lineNumber, ThrowableTesterInterface tester
  );

  ThrowableTesterInterface assertStackMethod(
      int stackIndex, String string, ThrowableTesterInterface tester
  );

  ThrowableTesterInterface showStackTrace(ThrowableTesterInterface tester);

}
