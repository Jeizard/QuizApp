package com.jeizard.quizapp.ViewModels.Factories

import android.app.Activity
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jeizard.domain.entities.Image
import com.jeizard.domain.entities.Question
import com.jeizard.domain.repository.BaseRepository
import com.jeizard.domain.repository.ImageRepository
import com.jeizard.domain.repository.WebRepository
import com.jeizard.quizapp.ViewModels.QuizViewModel
import com.jeizard.quizapp.data.room.models.single_models.images.repository.ImageRepositoryRoomImpl
import com.jeizard.quizapp.data.room.models.single_models.questions.repository.QuestionRepositoryRoomImpl
import com.jeizard.quizapp.data.web.repository.WebRepositoryRoomImpl

class QuizViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val questionRepository: BaseRepository<Question> = QuestionRepositoryRoomImpl(application = application)
        val imageRepository: ImageRepository = ImageRepositoryRoomImpl(application = application)
        val webRepository: WebRepository = WebRepositoryRoomImpl()
        return QuizViewModel(questionRepository, imageRepository, webRepository) as T
    }
}
