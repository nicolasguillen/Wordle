package ch.guillen.wordle.presentation.ui

import ch.guillen.test.CoroutinesTest
import ch.guillen.wordle.presentation.ui.game.CharStatus
import ch.guillen.wordle.presentation.ui.game.GameViewModel
import ch.guillen.wordle.presentation.ui.game.Word
import ch.guillen.words.domain.usecase.PickRandomWord
import ch.guillen.words.domain.usecase.ValidateWord
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GameViewModelTest : CoroutinesTest {

    private lateinit var testee: GameViewModel
    private val pickRandomWord: PickRandomWord = mockk()
    private val validateWord: ValidateWord = mockk()

    @Before
    fun setup() {
        setupSolution()

        testee = GameViewModel(pickRandomWord, validateWord)
    }

    @Test
    fun `getGuessStatus returns word evaluation`() {
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

    private fun setupSolution() {
        coEvery { pickRandomWord.invoke() } returns
                ch.guillen.words.domain.entity.Word("EARLY")
    }
}