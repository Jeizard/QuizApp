package com.jeizard.quizapp.data.room.models.single_models.questions.mapper

import com.jeizard.domain.entities.Question
import com.jeizard.quizapp.data.mapper.Mapper
import com.jeizard.quizapp.data.room.models.single_models.questions.entity.QuestionDBEntity

class QuestionDBEntityMapper : Mapper<QuestionDBEntity, Question> {

    override fun mapFromDBEntity(d: QuestionDBEntity): Question {
        return Question(d.id.toInt(), d.answer)
    }

    override fun mapToDBEntity(e: Question): QuestionDBEntity {
        return QuestionDBEntity(e.id.toLong(), e.answer)
    }

    override fun mapFromDBEntity(dCollection: Collection<QuestionDBEntity>): List<Question> {
        return dCollection.map { mapFromDBEntity(it) }
    }

    override fun mapToDBEntity(eCollection: Collection<Question>): List<QuestionDBEntity> {
        return eCollection.map { mapToDBEntity(it) }
    }
}

