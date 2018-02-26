package fresco.dsl

import dk.alexandra.fresco.framework.value.SInt
import dk.alexandra.fresco.framework.builder.numeric.ProtocolBuilderNumeric
import dk.alexandra.fresco.framework.builder.Computation
import dk.alexandra.fresco.framework.DRes

interface IntExpression : Expression {

	operator fun plus(other: IntExpression): IntExpression {
		return object : Cached(), IntExpression {
			override fun buildThis(builder: ProtocolBuilderNumeric): DRes<SInt> {
				return builder.numeric().add(this.build(builder), other.build(builder))
			}
		}
	}

	operator fun minus(other: IntExpression): IntExpression {
		return object : Cached(), IntExpression {
			override fun buildThis(builder: ProtocolBuilderNumeric): DRes<SInt> {
				return builder.numeric().sub(this.build(builder), other.build(builder))
			}
		}
	}

	operator fun times(other: IntExpression): IntExpression {
		return object : Cached(), IntExpression {
			override fun buildThis(builder: ProtocolBuilderNumeric): DRes<SInt> {
				return builder.numeric().mult(this.build(builder), other.build(builder))
			}
		}
	}

	operator fun div(other: IntExpression): IntExpression {
		return object : Cached(), IntExpression {
			override fun buildThis(builder: ProtocolBuilderNumeric): DRes<SInt> {
				return builder.advancedNumeric().div(this.build(builder), other.build(builder))
			}
		}
	}
}

fun sqrt(expr: IntExpression): IntExpression {
	return object : Cached(), IntExpression {
		override fun buildThis(builder: ProtocolBuilderNumeric): DRes<SInt> {
			return builder.advancedNumeric().sqrt(expr.build(builder),
					builder.basicNumericContext.maxBitLength)
		}
	}
}
