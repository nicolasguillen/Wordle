@file:Suppress("EXPERIMENTAL_API_USAGE")
package ch.guillen.words.data.repository

import android.content.Context
import ch.guillen.words.data.repository.WordsRepositoryImpl.Companion.FILE_NAME_ALL_WORDS
import ch.guillen.words.data.repository.asset.readFromAssets
import ch.guillen.words.domain.entity.Word
import io.mockk.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.serialization.SerializationException
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.*

const val testWordFile = """
    {
      "words": [
        "alloy",
        "rally",
        "hello"
      ]
    }
"""

class WordsRepositoryImplTest {
    private val context = mockk<Context>(relaxed = true)
    private val testDispatcher = TestCoroutineDispatcher()

    private lateinit var testee: WordsRepositoryImpl

    @Before
    fun beforeEveryTest() {
        mockkStatic("ch.guillen.words.data.repository.asset.AssetReaderKt")
        testee = WordsRepositoryImpl(context, testDispatcher)
    }

    @After
    fun afterEveryTest() {
        unmockkStatic("ch.guillen.words.data.repository.asset.AssetReaderKt")
    }

    @Test
    fun `all words returns full from file`() = runBlockingTest {
        // Arrange
        every { context.readFromAssets(FILE_NAME_ALL_WORDS) } returns testWordFile

        // Act
        val result = testee.getAllWords()

        // Assert
        assertEquals("ALLOY", result[0].word)
        assertEquals("HELLO", result[2].word)
    }

    @Test
    fun `all words return error for invalid empty file`() = runBlockingTest {
        // Arrange
        val errorWordsFile = """
            
        """
        every { context.readFromAssets(FILE_NAME_ALL_WORDS) } returns errorWordsFile

        // THEN
        assertFailsWith(SerializationException::class) {
            testee.getAllWords()
        }
    }

    @Test
    fun `contains word returns true when word is found on list`() = runBlockingTest {
        // Arrange
        every { context.readFromAssets(FILE_NAME_ALL_WORDS) } returns testWordFile

        // Act
        val result = testee.containsWord(Word("ALLOY"))

        // Assert
        assertTrue(result)
    }

    @Test
    fun `contains word returns false when word is not found on list`() = runBlockingTest {
        // Arrange
        every { context.readFromAssets(FILE_NAME_ALL_WORDS) } returns testWordFile

        // Act
        val result = testee.containsWord(Word("CHEAP"))

        // Assert
        assertFalse(result)
    }

    @Test
    fun `find random word returns word that exists on the list`() = runBlockingTest {
        // Arrange
        every { context.readFromAssets(FILE_NAME_ALL_WORDS) } returns testWordFile

        // Act
        val result = testee.getRandomWord()
        val list = testee.getAllWords()

        // Assert
        assertContains(list, result)
    }
}