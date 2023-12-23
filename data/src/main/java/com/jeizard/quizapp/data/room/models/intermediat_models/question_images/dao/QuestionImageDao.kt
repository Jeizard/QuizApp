package com.jeizard.quizapp.data.room.models.intermediat_models.question_images.dao

import androidx.room.Dao
import androidx.room.Query
import com.jeizard.quizapp.data.room.dao.BaseDao
import com.jeizard.quizapp.data.room.models.intermediat_models.question_images.entity.QuestionImageDBEntity

@Dao
abstract class QuestionImageDao : BaseDao<QuestionImageDBEntity> {

    @Query("DELETE FROM question_images")
    abstract fun deleteAllQuestionImages()

    @Query("SELECT * FROM question_images")
    abstract fun getAllQuestionImages(): List<QuestionImageDBEntity>

    override fun deleteAll() {
        deleteAllQuestionImages()
    }

    override fun getAll(): List<QuestionImageDBEntity> {
        return getAllQuestionImages()
    }
}
