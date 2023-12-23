package com.jeizard.quizapp.data.web.mappers

import com.jeizard.domain.entities.Question
import com.jeizard.quizapp.data.mapper.Mapper
import com.jeizard.quizapp.data.web.entities.QuestionWebEntity

class QuestionWebEntityMapper : Mapper<QuestionWebEntity, Question> {

    override fun mapFromDBEntity(d: QuestionWebEntity): Question {
        return Question(d.id, d.answer)
    }

    override fun mapToDBEntity(e: Question): QuestionWebEntity {
        return QuestionWebEntity(e.id, e.answer)
    }

    override fun mapFromDBEntity(dCollection: Collection<QuestionWebEntity>): List<Question> {
        return dCollection.map { mapFromDBEntity(it) }
    }

    override fun mapToDBEntity(eCollection: Collection<Question>): List<QuestionWebEntity> {
        return eCollection.map { mapToDBEntity(it) }
    }
}

