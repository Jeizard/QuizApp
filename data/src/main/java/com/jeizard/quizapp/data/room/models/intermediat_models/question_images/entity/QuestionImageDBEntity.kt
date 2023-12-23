package com.jeizard.quizapp.data.room.models.intermediat_models.question_images.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.jeizard.quizapp.data.room.models.single_models.questions.entity.QuestionDBEntity
import com.jeizard.quizapp.data.room.models.single_models.images.entity.ImageDBEntity

@Entity(
    tableName = "question_images",
    primaryKeys = ["question_id", "image_id"],
    indices = [
        Index("image_id")
    ],
    foreignKeys = [
        ForeignKey(
            entity = QuestionDBEntity::class,
            parentColumns = ["id"],
            childColumns = ["question_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ImageDBEntity::class,
            parentColumns = ["id"],
            childColumns = ["image_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class QuestionImageDBEntity(
    @ColumnInfo(name = "question_id") val questionId: Long,
    @ColumnInfo(name = "image_id") val imageId: Long)