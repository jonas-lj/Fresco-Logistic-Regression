package fresco.dsl

import dk.alexandra.fresco.framework.value.SInt
import dk.alexandra.fresco.framework.builder.numeric.ProtocolBuilderNumeric
import dk.alexandra.fresco.framework.DRes

open class FixedPoint(override val underlyingInt: IntExpression) : FixedPointExpression {
    override fun build(builder: ProtocolBuilderNumeric): DRes<SInt> {
        return underlyingInt.build(builder)
    }
}

class KnownFixedPoint(val value: Double)
    : FixedPoint(KnownInt(value.toFixedPoint()))

class ClosedFixedPoint(val value: Double, inputParty: Int)
    : FixedPoint(ClosedInt(value.toFixedPoint(), inputParty))
