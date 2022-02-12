package ch.guillen.wordle.presentation.ui

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
class SceneViewModel @Inject constructor(
    private val pickRandomWord: PickRandomWord,
    private val validateWord: ValidateWord
) : ViewModel() {

    private val _solution = MutableStateFlow("")
    val solution = _solution.asStateFlow()

    private val _currentIndex = MutableStateFlow(0)
    private val currentIndex = _currentIndex.asStateFlow()

    private val _words = MutableStateFlow(listOf<Word>())
    val words = _words.asStateFlow()

    private val _wordState = MutableStateFlow(listOf<WordState>())
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
        val index = currentIndex.value

        if(words.value.getOrNull(index) == null) {
            _words.tryEmit(words.value.toMutableList().apply {
                add(Word(""))
            })
            _wordState.tryEmit(wordState.value.toMutableList().apply {
                add(WordState.Idle)
            })
        } else {
            _wordState.tryEmit(wordState.value.toMutableList().apply {
                this[index] = WordState.Idle
            })
        }

        val currentWord = words.value[index].word
        when(keyType) {
            is KeyType.Letter -> {
                if(currentWord.length < 5) {
                    _words.tryEmit(words.value.toMutableList().apply {
                        this[index] = Word(currentWord + keyType.char)
                    })
                }
            }

            KeyType.Backspace ->
                _words.tryEmit(words.value.toMutableList().apply {
                    this[index] = Word(currentWord.dropLast(1))
                })

            KeyType.Enter -> {
                val doesWordExist = validateWord.invoke(currentWord)
                if(!doesWordExist || currentWord.length < 5) {
                    _wordState.tryEmit(wordState.value.toMutableList().apply {
                        this[index] = WordState.Error
                    })
                    return@launch
                }

                val guessStatus = getGuessStatus(words.value[index])
                val status = if(guessStatus.filterNot { it == CharStatus.CORRECT }.isEmpty()) {
                    // GG
                    WordState.Victory
                } else {
                    WordState.Validated(guessStatus)
                }
                _wordState.tryEmit(wordState.value.toMutableList().apply {
                    if(words.value.lastIndex == wordState.value.lastIndex) {
                        this[index] = status
                    } else {
                        add(index, status)
                    }
                })


                guessStatus.forEachIndexed { i, charStatus ->
                    keyboardState.value.updateLetter(KeyType.Letter(words.value[index].word[i]), charStatus)
                }
                _keyboardState.tryEmit(keyboardState.value)

                _currentIndex.tryEmit(index+1)
            }
            else -> { }
        }

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