package ch.guillen.wordle.presentation.ui.game

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.guillen.wordle.presentation.ui.keyboard.KeyType
import ch.guillen.wordle.presentation.ui.keyboard.KeyboardState
import ch.guillen.words.domain.usecase.PickRandomWord
import ch.guillen.words.domain.usecase.ValidateWord
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val pickRandomWord: PickRandomWord,
    private val validateWord: ValidateWord
) : ViewModel() {

    private val _solution = MutableStateFlow("")
    val solution = _solution.asStateFlow()

    private val _words = MutableStateFlow(listOf(Word("")))
    val words = _words.asStateFlow()

    private val _wordState = MutableStateFlow(listOf<WordState>(WordState.Idle))
    val wordState = _wordState.asStateFlow()

    private val _keyboardState = MutableStateFlow(KeyboardState())
    val keyboardState = _keyboardState.asStateFlow()

    init {
        flow {
            val randomWord = pickRandomWord.invoke().word
            emit(randomWord)
        }.onEach {
            _solution.tryEmit(it)
        }.launchIn(viewModelScope)
    }

    fun didType(keyType: KeyType) = viewModelScope.launch {

        if(wordState.value.last() is WordState.GameOver) {
            return@launch
        }

        val currentWord = words.value.last().word
        when(keyType) {
            is KeyType.Letter -> {
                if(currentWord.length < 5) {
                    emitNewWordState(WordState.Idle)
                    emitNewWord(Word(currentWord + keyType.char))
                }
            }

            KeyType.Backspace -> {
                emitNewWordState(WordState.Idle)
                emitNewWord(Word(currentWord.dropLast(1)))
            }

            KeyType.Enter -> {
                val doesWordExist = validateWord.invoke(currentWord)
                if(!doesWordExist || currentWord.length < 5) {
                    emitNewWordState(WordState.Idle)
                    emitNewWordState(WordState.Error)
                    return@launch
                }

                val guessStatus = getGuessStatus(words.value.last())
                emitNewWordState(WordState.Validated(guessStatus))

                // Check if game is over
                val wordIsCorrect = guessStatus.filterNot { it == CharStatus.CORRECT }.isEmpty()
                if(wordIsCorrect) {
                    emitNewWordState(WordState.GameOver(true))
                    return@launch
                } else if (_wordState.value.size == 6) {
                    emitNewWordState(WordState.GameOver(false))
                    return@launch
                }

                // Update keyboard
                guessStatus.forEachIndexed { i, charStatus ->
                    keyboardState.value.updateLetter(KeyType.Letter(words.value.last().word[i]), charStatus)
                }
                _keyboardState.tryEmit(keyboardState.value)

                // Prepare next word
                _words.tryEmit(words.value.toMutableList().apply {
                    add(Word(""))
                })
                _wordState.tryEmit(wordState.value.toMutableList().apply {
                    add(WordState.Idle)
                })
            }
            else -> { }
        }

    }

    private fun emitNewWord(newWord: Word) {
        _words.tryEmit(words.value.toMutableList().apply {
            this[lastIndex] = newWord
        })
    }

    private fun emitNewWordState(newState: WordState) {
        _wordState.tryEmit(wordState.value.toMutableList().apply {
            this[lastIndex] = newState
        })
    }

    @VisibleForTesting
    fun getGuessStatus(word: Word): List<CharStatus> {
        val charObj = mutableListOf<CharStatus>()

        word.word.forEachIndexed { index, letter ->
            if (!solution.value.contains(letter)) {
                charObj.add(CharStatus.ABSENT)
                return@forEachIndexed
            }

            if(letter == solution.value[index]) {
                charObj.add(CharStatus.CORRECT)
                return@forEachIndexed
            }

            charObj.add(CharStatus.PRESENT)
        }

        return charObj

    }
}