package ch.guillen.words.domain.usecase

import ch.guillen.words.domain.entity.Word
import ch.guillen.words.domain.repository.WordsRepository
import javax.inject.Inject

class PickRandomWord @Inject constructor(
    private val wordsRepository: WordsRepository
) {
    suspend operator fun invoke(): Word {
        return wordsRepository.getRandomWord()
    }
}