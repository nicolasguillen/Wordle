package ch.guillen.wordle.presentation.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import ch.guillen.wordle.presentation.ui.keyboard.KeyType

class SceneViewModel : ViewModel() {

    var solution = "EARLY"

    var gameState by mutableStateOf(SceneState(solution))
        private set

    fun didType(keyType: KeyType) {
        when(keyType) {
            is KeyType.Letter ->
                if(gameState.word1.word.length < 5) {
                    gameState = gameState.copy(word1 = Word(gameState.word1.word + keyType.char, WordState.IDLE))
                }

            KeyType.Backspace ->
                gameState =
                    gameState.copy(word1 = Word(gameState.word1.word.dropLast(1), WordState.IDLE))

            KeyType.Enter -> {
                gameState = if(gameState.word1.word.length == 5) {
                    gameState.copy(word1 = Word(gameState.word1.word, WordState.DONE))
                } else {
                    gameState.copy(word1 = Word(gameState.word1.word, WordState.ERROR))
                }
            }
            else -> {  }
        }
    }
}