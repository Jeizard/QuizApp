package com.jeizard.quizapp.data.room.models.single_models.questions.repository

import android.app.Application
import com.jeizard.domain.entities.Question
import com.jeizard.domain.repository.BaseRepository
import com.jeizard.quizapp.data.room.AppDatabase
import com.jeizard.quizapp.data.room.models.single_models.questions.dao.QuestionDao
import com.jeizard.quizapp.data.room.models.single_models.questions.entity.QuestionDBEntity
import com.jeizard.quizapp.data.room.models.single_models.questions.mapper.QuestionDBEntityMapper
import com.jeizard.quizapp.data.room.repository.BaseRepositoryRoomImpl

class QuestionRepositoryRoomImpl(application: Application) :
    BaseRepositoryRoomImpl<QuestionDBEntity, QuestionDao, Question>(
        AppDatabase.getInstance(application).questionDao(),
        QuestionDBEntityMapper()
    ),
    BaseRepository<Question>