package ch.guillen.wordle.presentation.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import ch.guillen.wordle.presentation.ui.keyboard.KeyType

class SceneViewModel : ViewModel() {

    var solution = "EARLY"

    private var index by mutableStateOf(0)

    var words by mutableStateOf(listOf<Word>())
        private set

    fun didType(keyType: KeyType) {
        if(words.getOrNull(index) == null) {
            words = words.toMutableList().apply {
                add(Word("", WordState.IDLE))
            }
        }
        when(keyType) {
            is KeyType.Letter -> {
                if(words[index].word.length < 5) {
                    words = words.toMutableList().apply {
                        this[index] = Word(words[index].word + keyType.char, WordState.IDLE)
                    }
                }
            }

            KeyType.Backspace ->
                words = words.toMutableList().apply {
                    this[index] = Word(words[index].word.dropLast(1), WordState.IDLE)
                }

            KeyType.Enter -> {
                if(words[index].word.length == 5) {
                    words = words.toMutableList().apply {
                        this[index] = Word(words[index].word, WordState.DONE)
                    }
                    index++
                } else {
                    words = words.toMutableList().apply {
                        this[index] = Word(words[index].word, WordState.ERROR)
                    }
                }
            }
            else -> {  }
        }
    }
}