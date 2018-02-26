package fresco;

import static org.junit.Assert.assertTrue;

import dk.alexandra.fresco.framework.Application;
import dk.alexandra.fresco.framework.DRes;
import dk.alexandra.fresco.framework.TestThreadRunner.TestThread;
import dk.alexandra.fresco.framework.TestThreadRunner.TestThreadFactory;
import dk.alexandra.fresco.framework.builder.numeric.ProtocolBuilderNumeric;
import dk.alexandra.fresco.framework.sce.resources.ResourcePool;
import fresco.LogisticRegression;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import plain.MatrixType;
import plain.Vector;

public class Tests {

  public static class TestLogisticRegression<ResourcePoolT extends ResourcePool>
      extends TestThreadFactory<ResourcePoolT, ProtocolBuilderNumeric> {

    @Override
    public TestThread<ResourcePoolT, ProtocolBuilderNumeric> next() {
      return new TestThread<ResourcePoolT, ProtocolBuilderNumeric>() {

        @Override
        public void test() throws Exception {
          
          int n = 16;
          Vector am1 = new Vector(1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
              0.0, 0.0, 0.0);
          Vector am2 = new Vector(0.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0, 1.0,
              1.0, 1.0, 1.0);

          Double[] ones1 = new Double[n];
          Arrays.fill(ones1, Double.valueOf(1.0));
          MatrixType X1 = new plain.Matrix(new Double[] {110.0, 110.0, 93.0, 110.0, 175.0, 105.0, 245.0, 62.0,
              95.0, 123.0, 123.0, 180.0, 180.0, 180.0, 205.0, 215.0}, new Double[] {2.62, 2.875, 2.32, 3.215, 3.44, 3.46, 3.57, 3.19, 3.15, 3.44,
                  3.44, 4.07, 3.73, 3.78, 5.25, 5.424}, ones1).transpose();

          Double[] ones2 = new Double[n];
          Arrays.fill(ones2, Double.valueOf(1.0));
          MatrixType X2 = new plain.Matrix(new Double[] {230.0, 66.0, 52.0, 65.0, 97.0, 150.0, 150.0, 245.0,
              175.0, 66.0, 91.0, 113.0, 264.0, 175.0, 335.0, 109.0}, new Double[] {5.345, 2.2, 1.615, 1.835, 2.465, 3.52, 3.435, 3.84, 3.845, 1.935,
                  2.14, 1.513, 3.17, 2.77, 3.57, 2.78}, ones2).transpose();

          
          Application<List<BigInteger>, ProtocolBuilderNumeric> testApplication = root -> {
            List<DRes<BigInteger>> outputs = new ArrayList<>();
            fresco.dsl.matrices.Vector result = new LogisticRegression().fitLogisticModel(new MatrixType[] {X1, X2},
                new Vector[] {am1, am2}, 1.0, 4);
            
            for (int i = 0; i < result.getSize(); i++) {
              outputs.add(root.numeric().open(result.get(i).getUnderlyingInt().build(root)));
            }
            
            return () -> outputs.stream().map(x -> x.out())
                .collect(Collectors.toList());
          };

          List<BigInteger> output = runApplication(testApplication);
          List<BigDecimal> scaled = output.stream().map(x -> new BigDecimal(x).setScale(10).divide(new BigDecimal(BigInteger.ONE.shiftLeft(16)), RoundingMode.HALF_UP)).collect(Collectors.toList());
          
          double intercept = 1.65707;
          double beta_hp = 0.00968555;
          double beta_wt = -1.17481;
          
          List<BigDecimal> expected = Arrays.asList(BigDecimal.valueOf(beta_hp), BigDecimal.valueOf(beta_wt), BigDecimal.valueOf(intercept));
          System.out.println(scaled);
          
          for (int i = 0; i < scaled.size(); i++) {
            assertTrue(expected.get(i).subtract(scaled.get(i)).abs().compareTo(BigDecimal.valueOf(0.1)) == -1);
          }
          
        }
      };
    }
  }

}
