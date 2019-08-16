package com.kodekonveyor.cdd.exception;

public interface ThrowableMessageTesterService {

  ThrowableTesterInterface
      assertMessageIs(String message, ThrowableTesterInterface tester);

  ThrowableTesterInterface
      assertMessageMatches(String string, ThrowableTesterInterface tester);

  ThrowableTesterInterface
      assertMessageContains(String string, ThrowableTesterInterface tester);

}
