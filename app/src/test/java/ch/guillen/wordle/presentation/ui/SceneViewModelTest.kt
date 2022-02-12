package ch.guillen.wordle.presentation.ui

import ch.guillen.wordle.CoroutinesTest
import ch.guillen.words.domain.usecase.PickRandomWord
import ch.guillen.words.domain.usecase.ValidateWord
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SceneViewModelTest : CoroutinesTest {

    private lateinit var testee: SceneViewModel
    private val pickRandomWord: PickRandomWord = mockk()
    private val validateWord: ValidateWord = mockk()

    @Before
    fun setup() {
        coEvery { pickRandomWord.invoke() } returns
                ch.guillen.words.domain.entity.Word("EARLY")

        testee = SceneViewModel(pickRandomWord, validateWord)
    }

    @Test
    fun test1() {
        // Arrange
        val word = Word("ALLOY")

        // Act
        val test = testee.getGuessStatus(word)

        // Arrange
        assertEquals(test, listOf(
            CharStatus.PRESENT,
            CharStatus.PRESENT,
            CharStatus.PRESENT,
            CharStatus.ABSENT,
            CharStatus.CORRECT,
        ))
    }
}