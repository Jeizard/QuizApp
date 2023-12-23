package com.jeizard.domain.repository

import com.jeizard.domain.entities.Image
import com.jeizard.domain.entities.Question

interface WebRepository {
    fun getNext();

    fun addQuestionListener(listener: BaseRepository.OnDataChangedListener<Question>)
    fun removeQuestionListener(listener: BaseRepository.OnDataChangedListener<Question>)

    fun addImagesListener(listener: ImageRepository.OnImagesByQuestionChangedListener)
    fun removeImagesListener(listener: ImageRepository.OnImagesByQuestionChangedListener)
}
