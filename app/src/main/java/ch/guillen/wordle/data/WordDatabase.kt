package ch.guillen.wordle.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import ch.guillen.wordle.data.model.WordModel

@Dao
interface WordDatabase {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllWords(words: List<WordModel>)
}