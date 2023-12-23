package com.jeizard.quizapp.data.web.repository

import android.os.AsyncTask
import com.jeizard.domain.entities.Image
import com.jeizard.domain.entities.Question
import com.jeizard.domain.repository.BaseRepository
import com.jeizard.domain.repository.ImageRepository
import com.jeizard.domain.repository.WebRepository
import com.jeizard.quizapp.data.mapper.Mapper
import com.jeizard.quizapp.data.room.dao.BaseDao
import com.jeizard.quizapp.data.web.entities.ImageWebEntity
import com.jeizard.quizapp.data.web.entities.QuestionWebEntity
import com.jeizard.quizapp.data.web.mappers.ImageWebEntityMapper
import com.jeizard.quizapp.data.web.mappers.QuestionWebEntityMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class WebRepositoryRoomImpl() : WebRepository {
    private var question: QuestionWebEntity = QuestionWebEntity(0, "СЛОВО")
    private var imagesForQuestion: MutableList<ImageWebEntity> = ArrayList()
    private val questionListeners: MutableSet<BaseRepository.OnDataChangedListener<Question>> = HashSet()
    private val imagesListeners: MutableSet<ImageRepository.OnImagesByQuestionChangedListener> = HashSet()
    private val questionWebEntityMapper = QuestionWebEntityMapper()
    private val imageWebEntityMapper = ImageWebEntityMapper()
    private val url = "https://losyash-library.fandom.com/ru/wiki/%D0%A1%D0%BC%D0%B5%D1%88%D0%B0%D1%80%D0%B8%D0%BA%D0%B8_(%D0%BC%D1%83%D0%BB%D1%8C%D1%82%D1%81%D0%B5%D1%80%D0%B8%D0%B0%D0%BB)"
    private val defaultImageUrl = "android.resource://com.jeizard.quizapp/drawable/ic_launcher_background"
    private val defaultImage = ImageWebEntity(0, defaultImageUrl)

    override fun getNext() {
        CoroutineScope(Dispatchers.IO).launch {
            var randomTitle: String? = null
            var imageUrls: List<String>? = null

            while (randomTitle.isNullOrEmpty() || imageUrls.isNullOrEmpty()) {
                try {
                    val doc: Document = Jsoup.connect(url).get()
                    val titleElements = doc.select("table.wikitable.sortable tr td:nth-child(2) a")
                    val randomIndex = (0 until titleElements.size).random()

                    randomTitle = titleElements[randomIndex].text()
                    println(randomTitle)

                    if (randomTitle.isNullOrEmpty()) {
                        println("randomTitle is Null Or Empty")
                        continue
                    }

                    question = QuestionWebEntity(0, randomTitle.toUpperCase())

                    val randomTitleUrl = titleElements[randomIndex].attr("abs:href")

                    val titleDoc: Document = Jsoup.connect(randomTitleUrl).get()
                    val galleryDiv = titleDoc.select("div[id=gallery-0]")
                    val imageElements = galleryDiv.select(".wikia-gallery-item img.thumbimage:not(.lazyload)")

                    imageUrls = imageElements.map { it.attr("src") }
                    println(imageUrls)

                    if (imageUrls.isNullOrEmpty()) {
                        println("imageUrls is Null Or Empty")
                        continue
                    }

                    val randomImagesUrl: List<String> = imageUrls.shuffled().take(4)
                    println(randomImagesUrl)

                    val images: MutableList<ImageWebEntity> = mutableListOf()

                    randomImagesUrl.forEach { url ->
                        images.add(ImageWebEntity(0, url))
                        println(url)
                    }

                    imagesForQuestion.clear()
                    imagesForQuestion.addAll(images)
                    if (imagesForQuestion.size < 4) {
                        imagesForQuestion.addAll(List(4 - imagesForQuestion.size) { defaultImage })
                    }
                } catch (e: Exception) {
                    println("Exception")
                    continue
                }
                notifyChanges()
            }
        }
    }

    override fun addQuestionListener(listener: BaseRepository.OnDataChangedListener<Question>) {
        questionListeners.add(listener)
        listener.onChanged(questionWebEntityMapper.mapFromDBEntity(listOf(question)))
    }

    override fun removeQuestionListener(listener: BaseRepository.OnDataChangedListener<Question>) {
        questionListeners.remove(listener)
    }

    override fun addImagesListener(listener: ImageRepository.OnImagesByQuestionChangedListener) {
        imagesListeners.add(listener)
        listener.onChanged(imageWebEntityMapper.mapFromDBEntity(imagesForQuestion))
    }

    override fun removeImagesListener(listener: ImageRepository.OnImagesByQuestionChangedListener) {
        imagesListeners.remove(listener)
    }

    private fun notifyChanges() {
        for (listener in questionListeners) {
            listener.onChanged(questionWebEntityMapper.mapFromDBEntity(listOf(question)))
        }
        for (listener in imagesListeners) {
            listener.onChanged(imageWebEntityMapper.mapFromDBEntity(imagesForQuestion))
        }
    }

}
