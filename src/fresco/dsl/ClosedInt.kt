package fresco.dsl

import dk.alexandra.fresco.framework.value.SInt
import java.math.BigInteger
import dk.alexandra.fresco.framework.builder.numeric.ProtocolBuilderNumeric
import dk.alexandra.fresco.framework.builder.Computation
import dk.alexandra.fresco.framework.DRes

class ClosedInt(val value: BigInteger, val inputParty: Int) : Cached(), IntExpression {
    constructor(value: Int, inputParty: Int) :
            this(BigInteger.valueOf(value.toLong()), inputParty)

    override fun buildThis(builder: ProtocolBuilderNumeric): DRes<SInt> {
        return builder.numeric().input(BigInteger.valueOf(value.toLong()), inputParty)
    }
}
