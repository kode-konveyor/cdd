package com.kodekonveyor.cdd.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class ThrowableTesterServiceImpl implements ThrowableTesterService {

  public static ThrowableTesterInterface assertThrows(final Thrower thrower) {
    ThrowableTester tester;
    try (
        ConfigurableApplicationContext ctx =
            new ClassPathXmlApplicationContext("applicationContext.xml")
    ) {
      tester = (ThrowableTester) ctx.getBean("throwableTester");
    }

    try {
      thrower.throwException();
    } catch (final Throwable exception) { //NOPMD AvoidCatchingThrowable
      tester.data.thrown = exception;
    }
    if (tester.data.thrown == null)
      fail("no exception thrown");
    return tester;
  }

  @Override
  public Throwable getException(final ThrowableTesterInterface tester) {
    return tester.getData().thrown;
  }

  @Override
  public ThrowableTesterInterface
      assertException(
          final Class<? extends Throwable> klass,
          final ThrowableTesterInterface tester
      ) {
    final ThrowableTesterData data = tester.getData();
    final String message =
        String
            .format(
                "expected %s but got %s", klass,
                ExceptionUtils.getStackTrace(data.thrown)
            );
    assertEquals(message, klass, data.thrown.getClass());
    return tester;
  }

  @Override
  public ThrowableTesterInterface
      assertUnimplemented(
          final Thrower thrower, final ThrowableTesterInterface tester
      ) {
    assertThrows(thrower).assertException(UnsupportedOperationException.class);
    return tester;
  }

}
