package com.jeizard.quizapp.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.jeizard.domain.entities.Image
import com.jeizard.domain.entities.Question
import com.jeizard.domain.repository.BaseRepository
import com.jeizard.domain.repository.ImageRepository
import com.jeizard.domain.repository.WebRepository
import com.jeizard.domain.usecases.GetNextQuestionFromWebUseCase
import com.jeizard.domain.usecases.UpdateCurrentImageByQuestionUseCase

class QuizViewModel(private val questionRepository: BaseRepository<Question>, private val imageRepository: ImageRepository, val webRepository: WebRepository) : ViewModel() {

    val allQuestions: MutableLiveData<List<Question>> = MutableLiveData()
    var currentQuestionIndex: MutableLiveData<Int> = MutableLiveData()
    var currentImages: MutableLiveData<List<Image>> =  MutableLiveData<List<Image>>()
    private var infinityModeOn = false

    private val allQuestionListener = object : BaseRepository.OnDataChangedListener<Question> {
        override fun onChanged(items: List<Question>) {
            allQuestions.postValue(items)
            currentQuestionIndex.postValue(0)
        }
    }

    fun updateCurrentImage() {
        val currentQuestions = allQuestions.value
        if (!currentQuestions.isNullOrEmpty()) {
            val currentQuestion = currentQuestions.getOrNull(currentQuestionIndex.value ?: 0)
            if (currentQuestion != null) {
                UpdateCurrentImageByQuestionUseCase(currentQuestion.id, imageRepository).execute()
            }
        }
    }

    fun setCurrentQuestionIndex(index: Int){
        currentQuestionIndex.value = index
        updateCurrentImage()
    }

    fun loadAllQuestions() {
        questionRepository.addListener(allQuestionListener)
        imageRepository.addImageByQuestionListener(imagesByQuestionListener)
        updateCurrentImage()
    }


    fun infinityModeOn(){
        if(!infinityModeOn) {
            webRepository.addQuestionListener(allQuestionListener)
            webRepository.addImagesListener(imagesByQuestionListener)
            GetNextQuestionFromWebUseCase(webRepository).execute()
            infinityModeOn = true
        }
    }

    fun infinityModeOff(){
        if(infinityModeOn) {
            webRepository.removeQuestionListener(allQuestionListener)
            webRepository.removeImagesListener(imagesByQuestionListener)
            infinityModeOn = false
        }
    }

    fun GetNextQuestion(navController: NavController) {
        if (currentQuestionIndex.value!! < (allQuestions.value?.size ?: 0) - 1) {
            currentQuestionIndex.value = currentQuestionIndex.value!! + 1
            updateCurrentImage()
        } else {
            navController.popBackStack("home", inclusive = false)
        }
    }

    private val imagesByQuestionListener = object : ImageRepository.OnImagesByQuestionChangedListener {
        override fun onChanged(items: List<Image>) {
            currentImages.postValue(items)
        }
    }
}

