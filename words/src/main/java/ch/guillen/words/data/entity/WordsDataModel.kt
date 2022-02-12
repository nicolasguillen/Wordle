package ch.guillen.words.data.entity

import kotlinx.serialization.Serializable

@Serializable
data class WordsDataModel(
    val words: List<String>
)