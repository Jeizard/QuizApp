package com.jeizard.domain.repository

import com.jeizard.domain.entities.Image

interface ImageRepository: BaseRepository<Image>{
    fun getImagesByQuestion(imageId: Int)

    interface OnImagesByQuestionChangedListener {
        fun onChanged(items: List<Image>)
    }

    fun addImageByQuestionListener(listener: OnImagesByQuestionChangedListener)
    fun removeImageByQuestionListener(listener: OnImagesByQuestionChangedListener)
}
