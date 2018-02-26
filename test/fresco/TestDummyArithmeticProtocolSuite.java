package fresco;


import dk.alexandra.fresco.framework.sce.evaluator.EvaluationStrategy;
import dk.alexandra.fresco.suite.dummy.arithmetic.AbstractDummyArithmeticTest;
import org.junit.Test;

public class TestDummyArithmeticProtocolSuite extends AbstractDummyArithmeticTest {

  @Test
  public void test_log_reg() {
    runTest(new Tests.TestLogisticRegression<>(), EvaluationStrategy.SEQUENTIAL, 2);
  }
  
}
