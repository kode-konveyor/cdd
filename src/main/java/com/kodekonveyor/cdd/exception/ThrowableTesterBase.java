package com.kodekonveyor.cdd.exception;

import org.springframework.beans.factory.annotation.Autowired;

class ThrowableTesterBase {

  @Autowired
  protected ThrowableTesterService throwableTesterService;
  @Autowired
  protected ThrowableMessageTesterService throwableMessageTesterService;
  @Autowired
  protected ThrowableStackTesterService throwableStackTesterService;

  public ThrowableTesterData data;

  protected ThrowableTesterBase() {
    this.data = new ThrowableTesterData();
  }

  public Throwable getException() {
    return this.throwableTesterService
        .getException((ThrowableTesterInterface) this);
  }

  public ThrowableTesterData getData() {
    return this.data;
  }

}
