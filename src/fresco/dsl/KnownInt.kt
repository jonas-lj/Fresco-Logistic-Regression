package fresco.dsl

import java.math.BigInteger
import dk.alexandra.fresco.framework.builder.numeric.ProtocolBuilderNumeric
import dk.alexandra.fresco.framework.builder.Computation
import dk.alexandra.fresco.framework.value.SInt
import dk.alexandra.fresco.framework.DRes

class KnownInt(val value: BigInteger): Cached(), IntExpression {
    constructor(value: Int) : this(BigInteger.valueOf(value.toLong()))

    override fun buildThis(builder: ProtocolBuilderNumeric): DRes<SInt> {
        return builder.numeric().known(value)
    }
}
