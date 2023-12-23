package com.jeizard.quizapp.data.room.models.single_models.images.mapper

import com.jeizard.domain.entities.Image
import com.jeizard.quizapp.data.mapper.Mapper
import com.jeizard.quizapp.data.room.models.single_models.images.entity.ImageDBEntity

class ImageDBEntityMapper : Mapper<ImageDBEntity, Image> {

    override fun mapFromDBEntity(d: ImageDBEntity): Image {
        return Image(d.id.toInt(), d.url)
    }

    override fun mapToDBEntity(e: Image): ImageDBEntity {
        return ImageDBEntity(e.id.toLong(), e.url)
    }

    override fun mapFromDBEntity(dCollection: Collection<ImageDBEntity>): List<Image> {
        return dCollection.map { mapFromDBEntity(it) }
    }

    override fun mapToDBEntity(eCollection: Collection<Image>): List<ImageDBEntity> {
        return eCollection.map { mapToDBEntity(it) }
    }
}
