package com.kodekonveyor.cdd.exception;

public interface ThrowableTesterService {

  Throwable getException(ThrowableTesterInterface tester);

  ThrowableTesterInterface
      assertException(
          Class<? extends Throwable> klass, ThrowableTesterInterface tester
      );

  ThrowableTesterInterface
      assertUnimplemented(Thrower thrower, ThrowableTesterInterface tester);

}
