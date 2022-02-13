package ch.guillen.words.data.repository

import android.content.Context
import ch.guillen.words.data.entity.WordsDataModel
import ch.guillen.words.data.repository.asset.readFromAssets
import ch.guillen.words.domain.entity.Word
import ch.guillen.words.domain.repository.WordsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class WordsRepositoryImpl(
    private val context: Context,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : WordsRepository {

    override suspend fun getAllWords(): List<Word> = readWordsFile()
        .filter { it.isNotEmpty() }
        .map { it.uppercase() }
        .map { Word(word = it) }

    override suspend fun containsWord(word: Word): Boolean =
        getAllWords().contains(word)

    override suspend fun getRandomWord(): Word =
        getAllWords().shuffled()[1]

    private suspend fun readWordsFile() = withContext(dispatcher) {
        val fileContent = context.readFromAssets(FILE_NAME_ALL_WORDS)
        val wordDataModel: WordsDataModel = Json.decodeFromString(fileContent)
        wordDataModel.words
    }

    companion object {
        const val FILE_NAME_ALL_WORDS = "words.json"
    }

}