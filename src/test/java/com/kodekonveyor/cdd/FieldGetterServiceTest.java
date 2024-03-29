package com.kodekonveyor.cdd;

import static com.kodekonveyor.cdd.exception.ThrowableTester.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.kodekonveyor.cdd.annotation.Context;
import com.kodekonveyor.cdd.exception.ThrowableTesterInterface;
import com.kodekonveyor.cdd.exception.impl.StackTraceSetterServiceImpl;
import com.kodekonveyor.cdd.fields.impl.FieldGetterServiceImpl;
import com.kodekonveyor.cdd.testartifacts.TestContract;

import javassist.NotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class FieldGetterServiceTest {

  @InjectMocks
  public FieldGetterServiceImpl fieldGetterServiceImpl;

  @Mock
  private StackTraceSetterServiceImpl stackTraceSetterService;

  @Mock
  private TestContract contractInstance;

  @Test
  public void throws_exception_when_annotation_not_found()
      throws NotFoundException {

    doReturn(new IllegalArgumentException("Annotation not found: @Context"))
        .when(stackTraceSetterService)
        .changeStackWithClass(any(), any());
    assertThrows(
        () -> fieldGetterServiceImpl
            .getFieldValueWithAnnotation(Context.class, contractInstance)
    )
        .assertException(IllegalArgumentException.class)
        .assertMessageIs("Annotation not found: @Context")
        .assertStackClass(
            ThrowableTesterInterface.FIRST_FRAME, this.getClass().getName()
        );
  }

}
