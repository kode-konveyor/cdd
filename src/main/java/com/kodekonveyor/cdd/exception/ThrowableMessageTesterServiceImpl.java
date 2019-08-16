package com.kodekonveyor.cdd.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.springframework.stereotype.Service;

@Service
public class ThrowableMessageTesterServiceImpl
    implements ThrowableMessageTesterService {

  @Override
  public ThrowableTesterInterface
      assertMessageIs(
          final String message, final ThrowableTesterInterface tester
      ) {
    final ThrowableTesterData data = tester.getData();
    assertEquals(message, data.thrown.getMessage());
    return tester;
  }

  @Override
  public ThrowableTesterInterface
      assertMessageMatches(
          final String string, final ThrowableTesterInterface tester
      ) {
    final ThrowableTesterData data = tester.getData();
    assertNotNull("no message of the exception", data.thrown.getMessage());
    assertTrue(
        "message does not match. \nexpected: " + string + "\n got:" +
            data.thrown.getMessage(),
        data.thrown.getMessage().matches(string)
    );
    return tester;
  }

  @Override
  public ThrowableTesterInterface
      assertMessageContains(
          final String string, final ThrowableTesterInterface tester
      ) {
    final ThrowableTesterData data = tester.getData();
    assertTrue(
        "message does not contain: " + string + "\n got:" +
            data.thrown.getMessage(),
        data.thrown.getMessage().contains(string)
    );
    return tester;
  }

}
