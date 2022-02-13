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
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ValidateWordTest {

    private val repository = mockk<WordsRepository>()

    private lateinit var testee: ValidateWord

    @Before
    fun beforeEveryTest() {
        testee = ValidateWord(repository)
    }

    @Test
    fun `validate word returns true when word is found on list`() = runBlockingTest {
        // Arrange
        val testRandomWord = "ALLOY"
        coEvery { repository.containsWord(any()) } returns true

        // Act
        val result = testee.invoke(testRandomWord)

        // Assert
        assertTrue(result)
    }

    @Test
    fun `validate word returns false when word is not found on list`() = runBlockingTest {
        // Arrange
        val testRandomWord = "ALLOY"
        coEvery { repository.containsWord(any()) } returns false

        // Act
        val result = testee.invoke(testRandomWord)

        // Assert
        assertFalse(result)
    }
}
