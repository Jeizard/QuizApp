package com.jeizard.domain.usecases

import com.jeizard.domain.repository.ImageRepository

class UpdateCurrentImageByQuestionUseCase(
    private val questionId: Int,
    private val imageRepository: ImageRepository
) {
    fun execute() {
        imageRepository.getImagesByQuestion(questionId)
    }
}
