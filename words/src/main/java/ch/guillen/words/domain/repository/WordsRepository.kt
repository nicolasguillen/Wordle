package ch.guillen.words.domain.repository

import ch.guillen.words.domain.entity.Word

interface WordsRepository {

    suspend fun getAllWords(): List<Word>

    suspend fun containsWord(word: Word): Boolean

    suspend fun getRandomWord(): Word
}