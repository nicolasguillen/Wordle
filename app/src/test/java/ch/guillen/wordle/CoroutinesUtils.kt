package ch.guillen.wordle

import kotlinx.coroutines.CoroutineDispatcher

@Suppress("PropertyName", "VariableNaming") // Non conventional naming starting with uppercase letter
interface DispatcherProvider {

    /** [CoroutineDispatcher] meant to run IO operations */
    val Io: CoroutineDispatcher

    /** [CoroutineDispatcher] meant to run computational operations */
    val Comp: CoroutineDispatcher

    /** [CoroutineDispatcher] meant to run on main thread */
    val Main: CoroutineDispatcher
}
