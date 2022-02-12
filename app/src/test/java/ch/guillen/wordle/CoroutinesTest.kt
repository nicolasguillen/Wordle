@file:Suppress("EXPERIMENTAL_API_USAGE")

package ch.guillen.wordle

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Rule
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * An interface meant to be implemented by a Test Suite that uses Complex Concurrency via Coroutines.
 * Example:
```
class MyClassTest : CoroutinesTest {

    @Test
    fun `some test`() = coroutinesTest {
        // testing structured concurrency here!
    }
}
```
 *
 * It provides a [CoroutinesTestRule] and alternative dispatchers.
 *
 */
interface CoroutinesTest {

    @get:Rule val coroutinesRule: CoroutinesTestRule
        get() = CoroutinesTestRule(dispatchers)

    val dispatchers: DispatcherProvider
        get() = TestDispatcherProvider

}

/**
 * A JUnit Test Rule that set a Main Dispatcher
 * @author Davide Farella
 */
class CoroutinesTestRule internal constructor(
    val dispatchers: DispatcherProvider = TestDispatcherProvider
) : TestWatcher() {

    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(dispatchers.Main)
    }

    override fun finished(description: Description) {
        super.finished(description)
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
    }
}
