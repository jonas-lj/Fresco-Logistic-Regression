package fresco.dsl

import dk.alexandra.fresco.framework.value.SInt
import java.lang.ref.WeakReference
import dk.alexandra.fresco.framework.builder.numeric.ProtocolBuilderNumeric
import dk.alexandra.fresco.framework.builder.Computation
import dk.alexandra.fresco.framework.DRes

interface Expression {
    fun build(builder: ProtocolBuilderNumeric): DRes<SInt>
}

abstract class Cached : Expression {
    var latestValue : WeakReference<DRes<SInt>>? = null
    var latestBuilder : WeakReference<ProtocolBuilderNumeric>? = null

    override fun build(builder: ProtocolBuilderNumeric): DRes<SInt> {
        val possibleResult = latestValue?.get()
        if (latestBuilder?.get() === builder && possibleResult != null) {
            return possibleResult
        } else {
            val result = buildThis(builder)
            latestValue = WeakReference(result)
            latestBuilder = WeakReference(builder)
            return result
        }
    }

    abstract fun buildThis(builder: ProtocolBuilderNumeric): DRes<SInt>
}
