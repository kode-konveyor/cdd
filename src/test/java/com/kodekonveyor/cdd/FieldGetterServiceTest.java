package com.kodekonveyor.cdd;

import static com.kodekonveyor.cdd.exception.ThrowableTester.assertThrows;
import static org.mockito.Mockito.doReturn;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.kodekonveyor.cdd.annotations.Context;
import com.kodekonveyor.cdd.exception.StackTraceCreatorService;
import com.kodekonveyor.cdd.impl.FieldGetterServiceImpl;
import com.kodekonveyor.cdd.testartifacts.TestContract;

import javassist.NotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class FieldGetterServiceTest {

  @InjectMocks
  public FieldGetterServiceImpl fieldGetterServiceImpl;

  @Mock
  StackTraceCreatorService stackTraceCreatorService =
      new StackTraceCreatorService();

  @Mock
  private TestContract contractInstance;

  @Test
  public void throws_exception_when_annotation_not_found()
      throws NotFoundException {

    Object stackTrace = new StackTraceCreatorService().createStackTrace(this);
    doReturn(stackTrace)
        .when(stackTraceCreatorService).createStackTrace(contractInstance);
    assertThrows(
        () -> fieldGetterServiceImpl
            .getFieldValueWithAnnotation(Context.class, contractInstance)
    )
        .assertException(IllegalArgumentException.class)
        .assertMessageIs("Annotation not found: @Context")
        .assertStackClass(0, this.getClass().getName());
  }

}
