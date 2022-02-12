package ch.guillen.wordle.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ch.guillen.wordle.data.model.WordModel

@Database(entities = [WordModel::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDatabase(): WordDatabase

    companion object {
        @Synchronized
        fun getInstance(context: Context): AppDatabase = buildDatabase(context)

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context.applicationContext,
                AppDatabase::class.java,
                "dictionary")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}