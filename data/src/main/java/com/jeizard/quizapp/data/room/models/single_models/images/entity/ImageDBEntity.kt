package com.jeizard.quizapp.data.room.models.single_models.images.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "images"
)
data class ImageDBEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val url: String)