package fresco.dsl

import dk.alexandra.fresco.framework.Application
import dk.alexandra.fresco.framework.BuilderFactory
import dk.alexandra.fresco.framework.configuration.NetworkConfiguration
import dk.alexandra.fresco.framework.network.Network
import dk.alexandra.fresco.framework.sce.resources.ResourcePool
import dk.alexandra.fresco.suite.ProtocolSuite
import dk.alexandra.fresco.suite.dummy.arithmetic.DummyArithmeticBuilderFactory
import dk.alexandra.fresco.suite.dummy.arithmetic.DummyArithmeticResourcePool
import fresco.dsl.matrices.MatrixType
import java.math.BigInteger
import java.nio.ByteBuffer
import java.security.SecureRandom
import java.util.*
import java.util.concurrent.LinkedTransferQueue
import java.util.concurrent.TransferQueue
import dk.alexandra.fresco.framework.builder.numeric.ProtocolBuilderNumeric
import dk.alexandra.fresco.framework.DRes
import dk.alexandra.fresco.framework.sce.SecureComputationEngineImpl
import dk.alexandra.fresco.framework.sce.evaluator.BatchedProtocolEvaluator
import dk.alexandra.fresco.framework.sce.evaluator.SequentialStrategy
import dk.alexandra.fresco.lib.field.integer.BasicNumericContext
import dk.alexandra.fresco.framework.ProtocolCollection
import dk.alexandra.fresco.framework.network.serializers.ByteSerializer
import dk.alexandra.fresco.suite.ProtocolSuite.DummyRoundSynchronization
import com.esotericsoftware.kryo.serializers.DefaultSerializers.BigIntegerSerializer
import dk.alexandra.fresco.framework.sce.resources.ResourcePoolImpl
import dk.alexandra.fresco.framework.util.Drbg
import dk.alexandra.fresco.framework.value.SInt
import java.math.BigDecimal

private val mod = BigInteger("6703903964971298549787012499123814115273848577471136527425966013026501536706464354255445443244279389455058889493431223951165286470575994074291745908195329")
private val maxBitLength = 200
private val theNetwork = DummyNetwork()

fun evaluate(expression: FixedPointExpression): Double {
	return evaluate(expression as Expression).asFixedPoint()
}

fun evaluate(expression: IntExpression): Int {
	return evaluate(expression as Expression).toInt()
}

fun evaluate(expression: fresco.dsl.matrices.Vector): plain.Vector {
	println("${Date().time} --------- START EVALUATE ---------")
	val size = expression.size
	var elements = DoubleArray(size, { 0.0 })
	for (index in 0 until size) {
		val result = evaluate(expression[index])
		println("${Date().time} ---------------------- result: ${result}")
		elements[index] = result
	}
	println("${Date().time} --------- EVALUATE DONE ---------")
	return plain.Vector(*elements)
}

fun evaluate(expression: MatrixType): plain.MatrixType {
	println("${Date().time} --------- START EVALUATE ---------")
	val rows = expression.numberOfRows
	val columns = expression.numberOfColumns
	var elements = Array(rows, { Array(columns, { 0.0 }) })
	for (row in 0 until rows) {
		for (col in 0 until columns) {
			println("${Date().time} --------- evaluate row ${row} col ${col}")
			val result = evaluate(expression[row, col])
			elements[row][col] = result
			println("${Date().time} ---------------------- result: ${result}")
		}
	}
	println("${Date().time} --------- EVALUATE DONE ---------")
	return plain.Matrix(*elements)
}

private fun evaluate(expression: Expression): BigInteger {
	val suite = DummyProtocolSuite()
	val evaluator = BatchedProtocolEvaluator<ResourcePool?>(SequentialStrategy<ResourcePool?>(), suite)
	val engine = SecureComputationEngineImpl(suite, evaluator)
	val result = engine.runApplication(DummyApplication(expression), ResourcePoolImpl(1, 2, object : Drbg {
		override fun nextBytes(bytes: ByteArray?) {
		}
	}), DummyNetwork())
	return result.toSigned()
}

private fun BigInteger.toSigned(): BigInteger {
	var actual = this.mod(mod)
	if (actual > mod.div(BigInteger.valueOf(2))) {
		actual = actual.subtract(mod)
	}
	return actual
}

private class DummyApplication(val expression: Expression) : Application<BigInteger, ProtocolBuilderNumeric> {
	override fun buildComputation(builder: ProtocolBuilderNumeric): DRes<BigInteger> {
		val computation = expression.build(builder)
		val open = builder.numeric().open(computation)
		return open
	}
}

private class DummyProtocolSuite : ProtocolSuite<ResourcePool?, ProtocolBuilderNumeric> {
	override fun init(resourcePool: ResourcePool?, network: Network?): BuilderFactory<ProtocolBuilderNumeric>? {
		val arithmeticFactory = BasicNumericContext(maxBitLength, mod, 2, 1)
		return DummyArithmeticBuilderFactory(arithmeticFactory)
	}

	override fun createRoundSynchronization(): ProtocolSuite.RoundSynchronization<ResourcePool?>? {
		return DummyRoundSynchronization()
	}
}

private class DummyNetwork : dk.alexandra.fresco.framework.network.Network {
	private val queue = mutableMapOf<Int, TransferQueue<ByteArray>>()

	override fun send(partyId: Int, data: ByteArray?) {
		if (!queue.contains(partyId)) {
			queue[partyId] = LinkedTransferQueue()
		}
		queue[partyId]!!.put(data)
	}

	override fun receive(partyId: Int): ByteArray? {
		if (!queue.contains(partyId)) {
			queue[partyId] = LinkedTransferQueue()
		}
		val result = queue[partyId]!!.take()
		return result
	}

	override fun getNoOfParties(): Int {
		return 2;
	}
}