package com.kodekonveyor.cdd.exception;

@FunctionalInterface
public interface Thrower {

  void throwException() throws Throwable;
}
