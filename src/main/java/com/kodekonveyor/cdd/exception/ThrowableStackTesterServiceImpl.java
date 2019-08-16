package com.kodekonveyor.cdd.exception;

import static org.junit.Assert.assertEquals;

import org.springframework.stereotype.Service;

@Service
public class ThrowableStackTesterServiceImpl
    implements ThrowableStackTesterService {

  @Override
  public ThrowableTesterInterface assertStackFileName(
      final int stackIndex, final String string,
      final ThrowableTesterInterface tester
  ) {
    final StackTraceElement stackElement =
        getStackTraceElement(stackIndex, tester);
    assertEquals(string, stackElement.getFileName());
    return tester;
  }

  @Override
  public ThrowableTesterInterface
      assertStackClass(
          final int stackIndex, final String string,
          final ThrowableTesterInterface tester
      ) {
    final StackTraceElement stackElement =
        getStackTraceElement(stackIndex, tester);
    assertEquals(string, stackElement.getClassName());
    return tester;
  }

  @Override
  public ThrowableTesterInterface assertStackLineNumber(
      final int stackIndex, final int lineNumber,
      final ThrowableTesterInterface tester
  ) {
    final StackTraceElement stackElement =
        getStackTraceElement(stackIndex, tester);
    assertEquals(lineNumber, stackElement.getLineNumber());
    return tester;
  }

  @Override
  public ThrowableTesterInterface
      assertStackMethod(
          final int stackIndex, final String string,
          final ThrowableTesterInterface tester
      ) {
    final StackTraceElement stackElement =
        getStackTraceElement(stackIndex, tester);
    assertEquals(string, stackElement.getMethodName());
    return tester;
  }

  private StackTraceElement
      getStackTraceElement(
          final int stackIndex, final ThrowableTesterInterface tester
      ) {
    final ThrowableTesterData data = tester.getData();
    return data.thrown.getStackTrace()[stackIndex];
  }

  @Override
  public ThrowableTesterInterface
      showStackTrace(final ThrowableTesterInterface tester) {
    final ThrowableTesterData data = tester.getData();
    data.thrown.printStackTrace(); //NOPMD AvoidPrintStackTrace
    return tester;
  }

}
