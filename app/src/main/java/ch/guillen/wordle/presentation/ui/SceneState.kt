package ch.guillen.wordle.presentation.ui

import androidx.compose.ui.graphics.Color
import ch.guillen.wordle.presentation.theme.HitColors

enum class CharStatus {
    ABSENT, PRESENT, CORRECT;

    val color: Color
        get() = when(this) {
            ABSENT -> HitColors.Miss
            PRESENT -> HitColors.Partial
            CORRECT -> HitColors.Hit
        }
}

sealed class WordState {
    data class Validated(
        val status: List<CharStatus>,
    ) : WordState()
    object Idle: WordState()
    object Error: WordState()
    object Victory: WordState()
}

data class Word(var word: String)
