package org.jetbrains.spek.api

import org.jetbrains.spek.junit.*
import org.junit.runner.*
import kotlin.collections.linkedListOf

@RunWith(JUnitClassRunner::class)
abstract class Spek : Specification {

    private val recordedActions = linkedListOf<TestGivenAction>()

    override fun given(description: String, givenExpression: Given.() -> Unit) {
        recordedActions.add(
                object : TestGivenAction {
                    override fun description() = "given " + description

                    override fun iterateOn(it: (TestOnAction) -> Unit) {
                        val given = GivenImpl()
                        given.givenExpression()
                        given.iterateOn(it)
                    }
                })

    }

    fun iterateGiven(it: (TestGivenAction) -> Unit): Unit = removingIterator(recordedActions, it)

    fun allGiven(): List<TestGivenAction> = recordedActions
}


fun <T> Spek.givenData(data: Iterable<T>, givenExpression: Given.(T) -> Unit) {
    for (entry in data) {
        given(entry.toString()) {
            givenExpression(entry)
        }
    }
}
