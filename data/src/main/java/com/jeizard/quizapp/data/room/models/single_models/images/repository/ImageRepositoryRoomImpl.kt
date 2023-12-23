package com.jeizard.quizapp.data.room.models.single_models.images.repository

import android.app.Application
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.jeizard.domain.entities.Image
import com.jeizard.domain.repository.ImageRepository
import com.jeizard.quizapp.data.room.AppDatabase
import com.jeizard.quizapp.data.room.models.single_models.images.dao.ImageDao
import com.jeizard.quizapp.data.room.models.single_models.images.entity.ImageDBEntity
import com.jeizard.quizapp.data.room.models.single_models.images.mapper.ImageDBEntityMapper
import com.jeizard.quizapp.data.room.repository.BaseRepositoryRoomImpl

class ImageRepositoryRoomImpl(application: Application) :
    BaseRepositoryRoomImpl<ImageDBEntity, ImageDao, Image>(
        AppDatabase.getInstance(application).imageDao(),
        ImageDBEntityMapper()
    ),
    ImageRepository {

    private var imagesByGroup: List<ImageDBEntity> = emptyList()
    private val imagesByQuestionListeners: MutableSet<ImageRepository.OnImagesByQuestionChangedListener> = HashSet()

    override fun getImagesByQuestion(imageId: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            val result = dao.getImagesByQuestion(imageId)
            imagesByGroup = result
            notifyImagesByQuestionChanges()
        }
    }

    override fun addImageByQuestionListener(imagesByQuestionListener: ImageRepository.OnImagesByQuestionChangedListener) {
        imagesByQuestionListeners.add(imagesByQuestionListener)
        imagesByQuestionListener.onChanged(mapper.mapFromDBEntity(imagesByGroup))
    }

    override fun removeImageByQuestionListener(imagesByQuestionListener: ImageRepository.OnImagesByQuestionChangedListener) {
        imagesByQuestionListeners.remove(imagesByQuestionListener)
    }

    private fun notifyImagesByQuestionChanges() {
        for (imagesByQuestionListener in imagesByQuestionListeners) {
            imagesByQuestionListener.onChanged(mapper.mapFromDBEntity(imagesByGroup))
        }
    }
}
