package com.jeizard.quizapp.data.web.mappers

import com.jeizard.domain.entities.Image
import com.jeizard.quizapp.data.mapper.Mapper
import com.jeizard.quizapp.data.web.entities.ImageWebEntity

class ImageWebEntityMapper : Mapper<ImageWebEntity, Image> {

    override fun mapFromDBEntity(d: ImageWebEntity): Image {
        return Image(d.id, d.url)
    }

    override fun mapToDBEntity(e: Image): ImageWebEntity {
        return ImageWebEntity(e.id, e.url)
    }

    override fun mapFromDBEntity(dCollection: Collection<ImageWebEntity>): List<Image> {
        return dCollection.map { mapFromDBEntity(it) }
    }

    override fun mapToDBEntity(eCollection: Collection<Image>): List<ImageWebEntity> {
        return eCollection.map { mapToDBEntity(it) }
    }
}
