package com.kodekonveyor.cdd.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ThrowableTester {

  @Autowired
  ThrowableTesterService throwableTesterService;

  ThrowableTesterData data;

  protected ThrowableTester() {
    data = new ThrowableTesterData();
  }

  public static ThrowableTester assertThrows(final Thrower thrower) {
    return ThrowableTesterServiceImpl.assertThrows(thrower);
  }

  public ThrowableTester assertMessageIs(String message) {
    return throwableTesterService.assertMessageIs(message, this);
  }

  public Throwable getException() {
    return throwableTesterService.getException(this);
  }

  public ThrowableTester assertException(Class<? extends Throwable> klass) {
    return throwableTesterService.assertException(klass, this);
  }

  public ThrowableTester assertUnimplemented(Thrower thrower) {
    return throwableTesterService.assertUnimplemented(thrower, this);
  }

  public ThrowableTester assertMessageMatches(String string) {
    return throwableTesterService.assertMessageMatches(string, this);
  }

  public ThrowableTester assertMessageContains(String string) {
    return throwableTesterService.assertMessageContains(string, this);
  }

  public ThrowableTester assertStackFileName(int stackIndex, String string) {
    return throwableTesterService.assertStackFileName(stackIndex, string, this);
  }

  public ThrowableTester assertStackClass(int stackIndex, String string) {
    return throwableTesterService.assertStackClass(stackIndex, string, this);
  }

  public ThrowableTester assertStackLineNumber(int stackIndex, int lineNumber) {
    return throwableTesterService
        .assertStackLineNumber(stackIndex, lineNumber, this);
  }

  public ThrowableTester assertStackMethod(int stackIndex, String string) {
    return throwableTesterService.assertStackMethod(stackIndex, string, this);
  }

  public ThrowableTester showStackTrace() {
    return throwableTesterService.showStackTrace(this);
  }

}
