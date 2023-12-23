package com.jeizard.domain.usecases

import com.jeizard.domain.repository.WebRepository

class GetNextQuestionFromWebUseCase(
    private val webRepository: WebRepository
) {
    fun execute() {
        webRepository.getNext()
    }
}
