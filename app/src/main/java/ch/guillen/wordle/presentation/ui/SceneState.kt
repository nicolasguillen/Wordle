package ch.guillen.wordle.presentation.ui

import ch.guillen.wordle.presentation.ui.keyboard.KeyType

data class SceneState(
    val solution: String,
    val word1: Word = Word("", WordState.IDLE),
) {

    fun getMissedLetters(): List<KeyType.Letter> {
        return if(word1.state != WordState.IDLE) {
            word1.word
                .filter { !solution.contains(it) }
                .map { KeyType.Letter(it) }
        } else {
            emptyList()
        }
    }
}

enum class WordState {
    IDLE, DONE, ERROR
}

data class Word(val word: String, var state: WordState)
