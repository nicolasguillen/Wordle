@file:Suppress("EXPERIMENTAL_API_USAGE")

package ch.guillen.test

import kotlinx.coroutines.test.TestCoroutineDispatcher

/**
 * Implementation of [DispatcherProvider] meant to be used for tests
 */
val TestDispatcherProvider = object : DispatcherProvider {
    override val Main: TestCoroutineDispatcher = TestCoroutineDispatcher()
    override val Io = Main
    override val Comp = Main
}
