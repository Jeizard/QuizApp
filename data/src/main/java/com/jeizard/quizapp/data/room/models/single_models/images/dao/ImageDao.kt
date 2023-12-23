package com.jeizard.quizapp.data.room.models.single_models.images.dao

import androidx.room.Dao
import androidx.room.Query
import com.jeizard.quizapp.data.room.dao.BaseDao
import com.jeizard.quizapp.data.room.models.single_models.images.entity.ImageDBEntity

@Dao
abstract class ImageDao : BaseDao<ImageDBEntity> {

    @Query("DELETE FROM images")
    abstract fun deleteAllImages()

    @Query("SELECT * FROM images")
    abstract fun getAllImages(): List<ImageDBEntity>

    override fun deleteAll() {
        deleteAllImages()
    }

    override fun getAll(): List<ImageDBEntity> {
        return getAllImages()
    }

    @Query(
        "SELECT images.* " +
        "FROM images " +
        "JOIN question_images " +
        "ON images.id = question_images.image_id " +
        "WHERE question_images.question_id = :questionId")
    abstract fun getImagesByQuestion(questionId: Int?): List<ImageDBEntity>
}