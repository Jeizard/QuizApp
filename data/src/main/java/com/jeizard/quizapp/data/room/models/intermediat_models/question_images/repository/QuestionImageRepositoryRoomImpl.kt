package com.jeizard.quizapp.data.room.models.intermediat_models.question_images.repository

import android.app.Application
import com.jeizard.domain.entities.QuestionImage
import com.jeizard.domain.repository.BaseRepository
import com.jeizard.quizapp.data.room.AppDatabase
import com.jeizard.quizapp.data.room.models.intermediat_models.question_images.dao.QuestionImageDao
import com.jeizard.quizapp.data.room.models.intermediat_models.question_images.entity.QuestionImageDBEntity
import com.jeizard.quizapp.data.room.models.room.single_models.images.mapper.QuestionImageDBEntityMapper
import com.jeizard.quizapp.data.room.repository.BaseRepositoryRoomImpl

class QuestionImageRepositoryRoomImpl(application: Application) :
    BaseRepositoryRoomImpl<QuestionImageDBEntity, QuestionImageDao, QuestionImage>(
        AppDatabase.getInstance(application).questionImageDao(),
        QuestionImageDBEntityMapper()
    ),
    BaseRepository<QuestionImage>