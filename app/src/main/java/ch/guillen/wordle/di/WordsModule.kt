package ch.guillen.wordle.di

import android.content.Context
import ch.guillen.words.data.repository.WordsRepositoryImpl
import ch.guillen.words.domain.repository.WordsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class WordsModule {

    @Provides
    fun provideWordsRepository(@ApplicationContext context: Context): WordsRepository =
        WordsRepositoryImpl(context)
}
