package com.kodekonveyor.cdd.exception;

public class ThrowableStackTester extends ThrowableMessageTester {

  public ThrowableTesterInterface
      assertStackFileName(final int stackIndex, final String string) {
    return this.throwableStackTesterService
        .assertStackFileName(stackIndex, string, (ThrowableTesterInterface) this);
  }

  public ThrowableTesterInterface
      assertStackClass(final int stackIndex, final String string) {
    return this.throwableStackTesterService
        .assertStackClass(stackIndex, string, (ThrowableTesterInterface) this);
  }

  public ThrowableTesterInterface
      assertStackLineNumber(final int stackIndex, final int lineNumber) {
    return this.throwableStackTesterService
        .assertStackLineNumber(
            stackIndex, lineNumber, (ThrowableTesterInterface) this
        );
  }

  public ThrowableTesterInterface
      assertStackMethod(final int stackIndex, final String string) {
    return this.throwableStackTesterService
        .assertStackMethod(stackIndex, string, (ThrowableTesterInterface) this);
  }

  public ThrowableTesterInterface showStackTrace() {
    return this.throwableStackTesterService
        .showStackTrace((ThrowableTesterInterface) this);
  }

}
