package ch.guillen.wordle.presentation.ui

data class SceneState(
    val solution: String,
    val words: MutableList<Word> = mutableListOf(Word("", WordState.IDLE)),
)

enum class WordState {
    IDLE, DONE, ERROR
}

data class Word(var word: String, var state: WordState)
