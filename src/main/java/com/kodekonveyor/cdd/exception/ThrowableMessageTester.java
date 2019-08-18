package com.kodekonveyor.cdd.exception;

public class ThrowableMessageTester extends ThrowableTesterBase {

  public ThrowableTesterInterface assertMessageIs(final String message) {
    return this.throwableMessageTesterService
        .assertMessageIs(message, (ThrowableTesterInterface) this);
  }

  public ThrowableTesterInterface assertMessageMatches(final String string) {
    return this.throwableMessageTesterService
        .assertMessageMatches(string, (ThrowableTesterInterface) this);
  }

  public ThrowableTesterInterface assertMessageContains(final String string) {
    return this.throwableMessageTesterService
        .assertMessageContains(string, (ThrowableTesterInterface) this);
  }

}
