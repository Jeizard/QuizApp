package com.jeizard.quizapp.data.room.models.single_models.questions.dao

import androidx.room.Dao
import androidx.room.Query
import com.jeizard.quizapp.data.room.dao.BaseDao
import com.jeizard.quizapp.data.room.models.single_models.questions.entity.QuestionDBEntity

@Dao
abstract class QuestionDao : BaseDao<QuestionDBEntity> {

    @Query("DELETE FROM questions")
    abstract fun deleteAllQuestions()

    @Query("SELECT * FROM questions")
    abstract fun getAllQuestions(): List<QuestionDBEntity>

    override fun deleteAll() {
        deleteAllQuestions()
    }

    override fun getAll(): List<QuestionDBEntity> {
        return getAllQuestions()
    }
}
