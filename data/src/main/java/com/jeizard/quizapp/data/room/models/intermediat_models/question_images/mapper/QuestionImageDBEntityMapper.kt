package com.jeizard.quizapp.data.room.models.room.single_models.images.mapper

import com.jeizard.domain.entities.QuestionImage
import com.jeizard.quizapp.data.mapper.Mapper
import com.jeizard.quizapp.data.room.models.intermediat_models.question_images.entity.QuestionImageDBEntity

class QuestionImageDBEntityMapper : Mapper<QuestionImageDBEntity, QuestionImage> {

    override fun mapFromDBEntity(d: QuestionImageDBEntity): QuestionImage {
        return QuestionImage(d.questionId.toInt(), d.imageId.toString())
    }

    override fun mapToDBEntity(e: QuestionImage): QuestionImageDBEntity {
        return QuestionImageDBEntity(e.questionId.toLong(), e.imageId.toLong())
    }

    override fun mapFromDBEntity(dCollection: Collection<QuestionImageDBEntity>): List<QuestionImage> {
        return dCollection.map { mapFromDBEntity(it) }
    }

    override fun mapToDBEntity(eCollection: Collection<QuestionImage>): List<QuestionImageDBEntity> {
        return eCollection.map { mapToDBEntity(it) }
    }
}
