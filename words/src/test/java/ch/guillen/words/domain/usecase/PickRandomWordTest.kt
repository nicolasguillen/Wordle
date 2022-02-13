@file:Suppress("EXPERIMENTAL_API_USAGE")
package ch.guillen.words.domain.usecase

import ch.guillen.words.domain.entity.Word
import ch.guillen.words.domain.repository.WordsRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class PickRandomWordTest {

    private val repository = mockk<WordsRepository>()

    private lateinit var testee: PickRandomWord

    @Before
    fun beforeEveryTest() {
        testee = PickRandomWord(repository)
    }

    @Test
    fun `get random word returns success`() = runBlockingTest {
        // Arrange
        val testRandomWord = "ALLOY"
        coEvery { repository.getRandomWord() } returns Word(testRandomWord)

        // Act
        val result = testee.invoke()

        // Assert
        assertEquals(testRandomWord, result.word)
    }
}
