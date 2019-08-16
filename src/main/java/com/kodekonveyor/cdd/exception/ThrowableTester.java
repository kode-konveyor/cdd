package com.kodekonveyor.cdd.exception;

import org.springframework.stereotype.Service;

@Service
public class ThrowableTester extends ThrowableStackTester
    implements ThrowableTesterInterface {

  public static ThrowableTesterInterface assertThrows(final Thrower thrower) {
    return ThrowableTesterServiceImpl.assertThrows(thrower);
  }

  @Override
  public ThrowableTesterInterface
      assertException(final Class<? extends Throwable> klass) {
    return this.throwableTesterService.assertException(klass, this);
  }

  @Override
  public ThrowableTesterInterface assertUnimplemented(final Thrower thrower) {
    return this.throwableTesterService.assertUnimplemented(thrower, this);
  }

}
