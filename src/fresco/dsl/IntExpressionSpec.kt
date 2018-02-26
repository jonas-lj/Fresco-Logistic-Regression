package fresco.dsl

import com.winterbe.expekt.expect
import dk.alexandra.fresco.framework.value.SInt
import dk.alexandra.fresco.suite.dummy.arithmetic.DummyArithmeticBuilderFactory
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.it
import java.math.BigInteger.TEN
import dk.alexandra.fresco.framework.DRes
import dk.alexandra.fresco.lib.field.integer.BasicNumericContext
import dk.alexandra.fresco.framework.builder.numeric.ProtocolBuilderNumeric

class IntExpressionSpec : Spek({

    val k1 = KnownInt(1)
    val k2 = KnownInt(2)
    val k3 = KnownInt(3)
    val c4 = ClosedInt(4, 1)

    it("supports addition") {
        expect(evaluate(k2 + k3)).to.equal(5)
    }

    it("supports subtraction") {
        expect(evaluate(k3 - k2)).to.equal(1)
    }

    it("can chain expressions") {
        expect(evaluate(k3 + k2 - k1)).to.equal(4)
        expect(evaluate(k3 - k2 + k1)).to.equal(2)
    }

    it("supports multiplication") {
        expect(evaluate(k2 * k3)).to.equal(6)
    }

    it("supports divisions") {
        expect(evaluate(c4 / k2)).to.equal(2)
    }

    it("supports adding a known and a closed integer") {
        expect(evaluate(k3 + c4)).to.equal(7)
    }

    it("support taking square root") {
        expect(evaluate(sqrt(c4))).to.equal(2)
    }

//    context("when building twice") {
//        val expression = sqrt(k1 + k2)
//
//        var build1: DRes<SInt>? = null
//        var build2: DRes<SInt>? = null
//
//	    val factory = BasicNumericContext(2, TEN, 2, 1)
//        val builderFactory = DummyArithmeticBuilderFactory(factory)
//
//        context("with the same builder") {
//            beforeEachTest {
//                val builder = ProtocolBuilderNumeric(builderFactory, false)
//                build1 = expression.build(builder)
//                build2 = expression.build(builder)
//            }
//
//            it("returns the same computation") {
//                expect(build1).to.be.of.identity(build2)
//            }
//        }
//
//        context("with different builders") {
//            beforeEachTest {
//                val builder1 = ProtocolBuilderNumeric(builderFactory, false)
//                val builder2 = ProtocolBuilderNumeric(builderFactory, false)
//                build1 = expression.build(builder1)
//                build2 = expression.build(builder2)
//            }
//
//            it("returns a different computation") {
//                expect(build1).to.not.be.of.identity(build2)
//            }
//        }
//    }
})
