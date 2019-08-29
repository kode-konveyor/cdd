package com.kodekonveyor.cdd;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;

public class TestHelper {

  public static void assertStartAndEndSignaledForTheDescription(
      final RunNotifier notifier, final Description description
  ) {
    verify(notifier, times(1)).fireTestStarted(description);
    verify(notifier, times(1)).fireTestFinished(description);
  }

}
