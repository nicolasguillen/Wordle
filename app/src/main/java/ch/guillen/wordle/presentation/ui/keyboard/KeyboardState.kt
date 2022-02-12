package ch.guillen.wordle.presentation.ui.keyboard

import androidx.compose.ui.graphics.Color
import ch.guillen.wordle.presentation.theme.HitColors
import ch.guillen.wordle.presentation.ui.CharStatus

data class KeyboardState(
    val rows: List<MutableList<KeyboardModel>> = listOf(
        mutableListOf(
            KeyboardModel(KeyType.Letter('Q')),
            KeyboardModel(KeyType.Letter('W')),
            KeyboardModel(KeyType.Letter('E')),
            KeyboardModel(KeyType.Letter('R')),
            KeyboardModel(KeyType.Letter('T')),
            KeyboardModel(KeyType.Letter('Y')),
            KeyboardModel(KeyType.Letter('U')),
            KeyboardModel(KeyType.Letter('I')),
            KeyboardModel(KeyType.Letter('O')),
            KeyboardModel(KeyType.Letter('P')),
        ),
        mutableListOf(
            KeyboardModel(KeyType.Space),
            KeyboardModel(KeyType.Letter('A')),
            KeyboardModel(KeyType.Letter('S')),
            KeyboardModel(KeyType.Letter('D')),
            KeyboardModel(KeyType.Letter('F')),
            KeyboardModel(KeyType.Letter('G')),
            KeyboardModel(KeyType.Letter('H')),
            KeyboardModel(KeyType.Letter('J')),
            KeyboardModel(KeyType.Letter('K')),
            KeyboardModel(KeyType.Letter('L')),
            KeyboardModel(KeyType.Space),
        ),
        mutableListOf(
            KeyboardModel(KeyType.Enter),
            KeyboardModel(KeyType.Letter('Z')),
            KeyboardModel(KeyType.Letter('X')),
            KeyboardModel(KeyType.Letter('C')),
            KeyboardModel(KeyType.Letter('V')),
            KeyboardModel(KeyType.Letter('B')),
            KeyboardModel(KeyType.Letter('N')),
            KeyboardModel(KeyType.Letter('M')),
            KeyboardModel(KeyType.Backspace),
        )
    )
) {
    fun updateLetter(letter: KeyType.Letter, charStatus: CharStatus) {
        rows.forEach { row ->
            row.find { it.type == letter }?.color = charStatus.color
        }
    }
}

data class KeyboardModel(
    val type: KeyType,
    var color: Color = HitColors.Unknown,
    val onPress: () -> Unit = {  },
)

sealed class KeyType {
    data class Letter(val char: Char): KeyType()
    object Enter: KeyType()
    object Backspace: KeyType()
    object Space: KeyType()

    val label: String
        get() = when(this) {
            is Letter -> "$char"
            Backspace -> "<-"
            Enter -> "Enter"
            Space -> ""
        }

    val weight: Float
        get() = when(this) {
            is Letter -> 1f
            Backspace, Enter -> 1.8f
            Space -> 0.5f
        }
}