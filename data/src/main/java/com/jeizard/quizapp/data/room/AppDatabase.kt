package com.jeizard.quizapp.data.room

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jeizard.quizapp.data.room.models.intermediat_models.question_images.dao.QuestionImageDao
import com.jeizard.quizapp.data.room.models.intermediat_models.question_images.entity.QuestionImageDBEntity
import com.jeizard.quizapp.data.room.models.single_models.images.dao.ImageDao
import com.jeizard.quizapp.data.room.models.single_models.images.entity.ImageDBEntity
import com.jeizard.quizapp.data.room.models.single_models.questions.dao.QuestionDao
import com.jeizard.quizapp.data.room.models.single_models.questions.entity.QuestionDBEntity

@Database(
    entities = [QuestionDBEntity::class,
               ImageDBEntity::class,
               QuestionImageDBEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun questionDao(): QuestionDao
    abstract fun imageDao(): ImageDao
    abstract fun questionImageDao(): QuestionImageDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, "quiz_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build()
            }
            return INSTANCE as AppDatabase
        }

        private val roomCallback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                PopulateDbAsyncTask(INSTANCE).execute()
            }

            override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
                super.onDestructiveMigration(db)
                PopulateDbAsyncTask(INSTANCE).execute()
            }
        }
    }

    private class PopulateDbAsyncTask(db: AppDatabase?) : AsyncTask<Void, Void, Void>() {
        private val questionDao : QuestionDao = db!!.questionDao()
        private val imageDao : ImageDao = db!!.imageDao()
        private val questionImageDao: QuestionImageDao = db!!.questionImageDao()
        override fun doInBackground(vararg voids: Void): Void? {
            val questionData = listOf(
                "СМЫСЛ ЖИЗНИ",
                "ЖЕЛЕЗНАЯ НЯНЯ",
                "ПСИХОЛОГ",
                "ЭРУДИТ"
            )

            questionData.forEachIndexed { index, questionText ->
                val questionId = questionDao.insert(QuestionDBEntity(0, questionText))

                (1..4).forEach { imageId ->
                    val image = ImageDBEntity(0, "android.resource://com.jeizard.quizapp/drawable/question_${index + 1}_image_$imageId")
                    val imageId = imageDao.insert(image)

                    val questionImage = QuestionImageDBEntity(questionId, imageId)
                    questionImageDao.insert(questionImage)
                }
            }
            return null
        }
    }
}
