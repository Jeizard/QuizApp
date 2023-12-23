package com.jeizard.quizapp

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.jeizard.domain.entities.Image
import com.jeizard.domain.usecases.GetNextQuestionFromWebUseCase
import com.jeizard.quizapp.ViewModels.Factories.QuizViewModelFactory
import com.jeizard.quizapp.ViewModels.QuizViewModel
import com.jeizard.quizapp.ui.theme.QuizAppTheme

private const val HOME_ROUTE = "home"
private const val QUESTION_SCREEN_INFINITY_MODE_ROUTE = "questionScreenInfinityMode"
private const val QUESTION_SCREEN_DB_MODE_ROUTE = "questionScreenDBMode"
private const val LEVEL_SCREEN_ROUTE = "levelScreen"
private const val BACK_ICON_DESCRIPTION = "Back to Home"
private const val COINS_ICON_DESCRIPTION = "Coins icon"
private const val LOGO_DESCRIPTION = "Back to Home"
private const val TOGGLE_MUSIC_DESCRIPTION = "Toggle Music"
private const val CONTINUE_TEXT = "Продолжить"
private const val CONGRATS_TEXT = "Поздравляем, вы угадали слово!"
private const val GUESSED_WORD_TEXT = "Загаданное слово: "
private const val ADD_COINS_TEXT = "+ 30 монет"
private const val MINUS_FIFTEEN_TEXT = "-15"
private const val MINUS_TEN_TEXT = "-10"
private const val NEXT_TEXT = "->"
private const val NOT_ENOUGH_COINS_MESSAGE = "Недостаточно монет!"
private const val DEFAULT_WORD_TEXT = "СЛОВО"
private const val DEFAULT_IMAGE_URL = "android.resource://com.jeizard.quizapp/drawable/ic_launcher_background"
private const val IMAGE_CONTENT_DESCRIPTION = "image"

class QuizActivity() : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var coinManager: CoinManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        coinManager = CoinManager(this)

        val viewModel = ViewModelProvider(this, QuizViewModelFactory(application))[QuizViewModel::class.java]
        setContent {
            QuizAppTheme {
                val navController = rememberNavController()

                val mediaPlayer = remember {
                    MediaPlayer.create(this@QuizActivity, R.raw.music).apply {
                        isLooping = true
                        start()
                        pause()
                    }
                }

                LaunchedEffect(true) {
                    val callback = object : OnBackPressedCallback(true) {
                        override fun handleOnBackPressed() {
                            handleBackPressed(navController)
                        }
                    }
                    onBackPressedDispatcher.addCallback(this@QuizActivity, callback)
                }

                NavHost(navController = navController, startDestination = "home") {
                    composable(HOME_ROUTE) {
                        HomeScreen(navController = navController, viewModel = viewModel, mediaPlayer = mediaPlayer, coinManager = coinManager)
                    }
                    composable(QUESTION_SCREEN_INFINITY_MODE_ROUTE) {
                        QuestionScreen(navController = navController, viewModel = viewModel, mode = "Infinity", coinManager = coinManager, context = this@QuizActivity )
                    }
                    composable(QUESTION_SCREEN_DB_MODE_ROUTE) {
                        QuestionScreen(navController = navController, viewModel = viewModel, mode = "DataBase", coinManager = coinManager, context = this@QuizActivity)
                    }
                    composable(LEVEL_SCREEN_ROUTE) {
                        LevelScreen(navController = navController, viewModel = viewModel)
                    }
                }
            }
        }
    }

    private fun handleBackPressed(navController: NavController) {
        if (navController.currentBackStackEntry?.destination?.route == QUESTION_SCREEN_INFINITY_MODE_ROUTE ||
            navController.currentBackStackEntry?.destination?.route == QUESTION_SCREEN_DB_MODE_ROUTE
        ) {
            navController.navigate(HOME_ROUTE) {
                popUpTo(HOME_ROUTE) {
                    inclusive = true
                }
            }
        } else {
            navController.popBackStack()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        coinManager.saveCoins()

        mediaPlayer?.release()
        mediaPlayer = null
    }
}

@Composable
fun CoinsDisplay(coinManager: CoinManager) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopEnd
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 16.dp,
                end = 24.dp)
        ) {
            Text(
                text = "${coinManager.getCoins()}",
                modifier = Modifier.padding(end = 4.dp)
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_coin),
                contentDescription = COINS_ICON_DESCRIPTION
            )
        }
    }
}

@Composable
fun HomeScreen(navController: NavController, viewModel: QuizViewModel, mediaPlayer: MediaPlayer, coinManager: CoinManager) {
    viewModel.currentQuestionIndex.value = 0

    val icon = remember { mutableStateOf(if (mediaPlayer?.isPlaying == true) {
        R.drawable.ic_music_on
    } else {
        R.drawable.ic_music_off
    })}

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        CoinsDisplay(coinManager = coinManager)
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopStart
        ) {
            IconButton(
                onClick = {
                    if (mediaPlayer?.isPlaying == true) {
                        mediaPlayer.pause()
                        icon.value = R.drawable.ic_music_off
                    } else {
                        mediaPlayer?.start()
                        icon.value = R.drawable.ic_music_on
                    }
                },
                modifier = Modifier
                    .padding(top = 4.dp,
                        start = 16.dp),
            ) {
                Icon(
                    painter = painterResource(icon.value),
                    contentDescription = TOGGLE_MUSIC_DESCRIPTION
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon),
                    contentDescription = LOGO_DESCRIPTION,
                    modifier = Modifier.size(200.dp)
                )
            }

            Column(
                modifier = Modifier.padding(bottom = 8.dp, start = 16.dp, end = 16.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        navController.navigate(QUESTION_SCREEN_INFINITY_MODE_ROUTE) },
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 32.dp,
                            end = 32.dp)
                ) {
                    Text(text = "Бесконечный")
                }
            }

            Column(
                modifier = Modifier.padding(bottom = 42.dp, start = 16.dp, end = 16.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        viewModel.loadAllQuestions()
                        navController.navigate(LEVEL_SCREEN_ROUTE) },
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 32.dp,
                            end = 32.dp)
                ) {
                    Text(text = "Уровни")
                }
            }

            Column(
                modifier = Modifier.padding(bottom = 16.dp, top = 16.dp, end = 16.dp, start = 16.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        viewModel.loadAllQuestions()
                        navController.navigate(QUESTION_SCREEN_DB_MODE_ROUTE) },
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                ) {
                    Text(text = "Играть")
                }
            }
        }
    }
}

@Composable
fun LevelScreen(navController: NavController, viewModel: QuizViewModel) {
    val allQuestions = viewModel.allQuestions.value ?: emptyList()

    LazyVerticalGrid(
        columns = GridCells.Fixed(5), // Number of columns in each row
        modifier = Modifier.fillMaxSize()
    ) {
        items(allQuestions.size) { index ->
            Button(
                onClick = { navigateToQuestionScreen(navController, viewModel, index) },
                modifier = Modifier.padding(8.dp),
                shape = RoundedCornerShape(5.dp),

            ) {
                Text(text = "${index + 1}")
            }
        }
    }
}

private fun navigateToQuestionScreen(navController: NavController, viewModel: QuizViewModel, index: Int) {
    viewModel.setCurrentQuestionIndex(index)
    navController.navigate(QUESTION_SCREEN_DB_MODE_ROUTE)
}


private const val SPACE_INDEX = -1

@Composable
fun QuestionScreen(navController: NavController, viewModel: QuizViewModel, mode: String, coinManager: CoinManager, context: Context) {
    val showVictoryScreen = remember { mutableStateOf(false) }
    var wordToGuess = remember { mutableStateOf("") }

    val minusFifteenButtonEnabled = remember { mutableStateOf(true) }
    val minusTenButtonEnabled = remember { mutableStateOf(true) }

    val isLoading = remember { mutableStateOf(true) }

    val lifecycleOwner = LocalLifecycleOwner.current

    val defaultImageUrl = DEFAULT_IMAGE_URL
    val defaultImage = Image(0, defaultImageUrl)
    var currentImages: MutableList<Image> = remember { mutableStateListOf() }

    val enteredLetters = remember { mutableStateMapOf<Int, Int>() }
    val alphabet = ('А'..'Я').toList()
    val letters = mutableListOf<Char>()
    val enables = remember {mutableStateListOf<Boolean>()}

    var length = 0
    var randomLettersCount = 0

    fun addRandomLetters(howMany: Int) {
        repeat(howMany) {
            val randomIndex = (alphabet.indices).random()
            val randomLetter = alphabet[randomIndex]
            letters.add(randomLetter)
            enables.add(true)
        }
        letters.shuffle()
    }

    fun setNewWord(newWord: String) {
        wordToGuess.value = newWord
        enteredLetters.clear()
        letters.clear()
        enables.clear()

        length = wordToGuess.value.length

        wordToGuess.value.forEachIndexed { index, letter ->
            if (letter == ' ') {
                enteredLetters[index] = SPACE_INDEX
                length--
            }
            else{
                letters.add(letter)
                enables.add(true)
            }
        }

        randomLettersCount = when {
            length > 27 -> 5 * 7
            length > 21 -> 4 * 7
            length > 14 -> 3 * 7
            else -> 2 * 7
        }
        addRandomLetters(randomLettersCount - length)
    }

    viewModel.currentImages.observe(lifecycleOwner) { images ->
        isLoading.value = images.isNullOrEmpty()
        currentImages.clear()
        currentImages.addAll(images ?: emptyList())
    }

    if(mode == "Infinity"){
        viewModel.infinityModeOn()
        viewModel.currentQuestionIndex.removeObservers(lifecycleOwner)
        viewModel.allQuestions.observe(lifecycleOwner) {allQuestions ->
            if (allQuestions != null) {
                setNewWord(allQuestions[0]?.answer?.toUpperCase() ?: DEFAULT_WORD_TEXT)
            }
            else{
                setNewWord(DEFAULT_WORD_TEXT)
            }
        }
    }
    if(mode == "DataBase"){
        viewModel.infinityModeOff()
        viewModel.allQuestions.removeObservers(lifecycleOwner)
        viewModel.currentQuestionIndex.observe(lifecycleOwner) { index ->
            val questions = viewModel.allQuestions.value
            if (questions != null && index < questions.size) {
                setNewWord(questions[index]?.answer?.toUpperCase() ?: DEFAULT_WORD_TEXT)
            }
            else{
                setNewWord(DEFAULT_WORD_TEXT)
            }
        }
        viewModel.updateCurrentImage()
    }

    fun moveToNextQuestion(mode: String, navController: NavController) {
        viewModel.currentImages.value = emptyList()

        letters.forEachIndexed { index, letter ->
            enables[index] = true
        }
        enteredLetters.clear()
        minusFifteenButtonEnabled.value = true
        minusTenButtonEnabled.value = true

        when (mode) {
            "DataBase" -> {
                viewModel.GetNextQuestion(navController)
            }
            "Infinity" -> {
                GetNextQuestionFromWebUseCase(viewModel.webRepository).execute()
            }
        }
    }

    if (isLoading.value) {
        LoadingScreen()
    }
    else {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, top = 2.dp)
            ) {
                IconButton(
                    onClick = {
                        navController.navigate(HOME_ROUTE) {
                            popUpTo(HOME_ROUTE) {
                                inclusive = true
                            }
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = BACK_ICON_DESCRIPTION
                    )
                }
            }
            CoinsDisplay(coinManager = coinManager)
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    currentImages.take(2).forEach { ImageItem(it.url) }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    currentImages.drop(2).take(2).forEach { ImageItem(it.url) }
                }

                Row(
                    modifier = Modifier.padding(
                        start = 8.dp,
                        end = 8.dp,
                        top = 8.dp
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    wordToGuess.value.forEachIndexed { index, letter ->
                        if (enteredLetters.containsKey(index)) {
                            if (enteredLetters[index] == SPACE_INDEX) {
                                Button(
                                    onClick = { },
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.background),
                                    shape = RoundedCornerShape(0.dp),
                                    contentPadding = PaddingValues(0.dp),
                                    modifier = Modifier
                                        .height(getButtonSize(wordToGuess.value.length).dp)
                                        .width(getButtonSize(wordToGuess.value.length).dp)
                                ) {
                                    Text(
                                        text = "",
                                        textAlign = TextAlign.Center,
                                        fontSize = (getButtonSize(wordToGuess.value.length) - 14).sp
                                    )
                                }
                            } else {
                                val color: Color =
                                    if (letter != letters[enteredLetters[index]!!]) {
                                        Color.Red
                                    } else {
                                        Color.Gray
                                    }
                                Button(
                                    onClick = {
                                        enables[enteredLetters[index]!!] = true
                                        enteredLetters.remove(index)
                                    },
                                    shape = RoundedCornerShape(0.dp),
                                    border = BorderStroke(1.dp, color),
                                    contentPadding = PaddingValues(0.dp),
                                    modifier = Modifier
                                        .height(getButtonSize(wordToGuess.value.length).dp)
                                        .width(getButtonSize(wordToGuess.value.length).dp)
                                ) {
                                    Text(
                                        text = "${letters[enteredLetters[index]!!]}",
                                        textAlign = TextAlign.Center,
                                        fontSize = (getButtonSize(wordToGuess.value.length) - 14).sp
                                    )
                                }
                            }
                        } else {
                            Button(
                                onClick = { },
                                enabled = false,
                                shape = RoundedCornerShape(0.dp),
                                border = BorderStroke(1.dp, Color.Gray),
                                contentPadding = PaddingValues(0.dp),
                                modifier = Modifier
                                    .height(getButtonSize(wordToGuess.value.length).dp)
                                    .width(getButtonSize(wordToGuess.value.length).dp)
                            ) {
                                Text(
                                    text = "",
                                    textAlign = TextAlign.Center,
                                    fontSize = (getButtonSize(wordToGuess.value.length) - 14).sp
                                )
                            }

                        }
                    }
                }
                Row(
                    modifier = Modifier.padding(0.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            if (coinManager.removeCoins(15)) {
                                minusFifteenButtonEnabled.value = false
                                minusTenButtonEnabled.value = false
                                enteredLetters.entries.removeIf { it.value != SPACE_INDEX }
                                letters.forEachIndexed { index, letter ->
                                    enables[index] = true
                                }
                                wordToGuess.value.substring(0, wordToGuess.value.length / 3)
                                    .forEachIndexed { index, letter ->
                                        var startIndex = 0
                                        var foundIndex = letters.indexOfFirst { it == letter }
                                        while (foundIndex != -1 && !enables[foundIndex]) {
                                            val sublist =
                                                letters.subList(startIndex + 1, letters.size)
                                            foundIndex = sublist.indexOfFirst { it == letter }
                                            if (foundIndex != -1) {
                                                foundIndex += startIndex + 1
                                            }
                                            startIndex = foundIndex
                                        }
                                        if (foundIndex != -1) {
                                            enables[foundIndex] = false
                                            enteredLetters[index] = foundIndex
                                        }
                                    }
                            } else {
                                Toast.makeText(
                                    context,
                                    NOT_ENOUGH_COINS_MESSAGE,
                                    Toast.LENGTH_SHORT
                                )
                            }
                        },
                        enabled = minusFifteenButtonEnabled.value,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text(text = MINUS_FIFTEEN_TEXT)
                    }
                    Button(
                        onClick = {
                            if (coinManager.removeCoins(10)) {
                                minusTenButtonEnabled.value = false
                                minusFifteenButtonEnabled.value = false
                                enteredLetters.entries.removeIf { it.value != SPACE_INDEX }
                                letters.forEachIndexed { index, letter ->
                                    enables[index] = false
                                }
                                wordToGuess.value.forEach { letter ->
                                    var startIndex = 0
                                    var index = letters.indexOfFirst { it == letter }
                                    while (index != -1 && enables[index]) {
                                        val sublist = letters.subList(startIndex + 1, letters.size)
                                        index = sublist.indexOfFirst { it == letter }
                                        if (index != -1) {
                                            index += startIndex + 1
                                        }
                                        startIndex = index
                                    }
                                    if (index != -1) {
                                        enables[index] = true
                                    }
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    NOT_ENOUGH_COINS_MESSAGE,
                                    Toast.LENGTH_SHORT
                                )
                            }
                        },
                        enabled = minusTenButtonEnabled.value,
                    ) {
                        Text(text = MINUS_TEN_TEXT)
                    }
                    Button(
                        onClick = {
                            moveToNextQuestion(mode, navController)
                        },
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text(text = NEXT_TEXT)
                    }
                }

                Column(
                    modifier = Modifier.padding(
                        start = 8.dp,
                        end = 8.dp,
                        top = 8.dp
                    ),
                ) {
                    repeat(randomLettersCount / 7) { startIndex ->
                        val endIndex = startIndex * 7 + 7
                        Row {
                            RowOfButtons(
                                startIndex * 7,
                                endIndex,
                                letters,
                                enables,
                                enteredLetters,
                                wordToGuess.value,
                                showVictoryScreen,
                                coinManager
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }
        if (showVictoryScreen.value) {
            VictoryScreen(
                wordToGuess = wordToGuess.value,
                onContinueClick = {
                    showVictoryScreen.value = false
                    moveToNextQuestion(mode, navController)
                },
                coinManager = coinManager
            )
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}


@Composable
fun RowOfButtons(
    startIndex: Int,
    endIndex: Int,
    letters: List<Char>,
    enables: MutableList<Boolean>,
    enteredLetters: MutableMap<Int, Int>,
    wordToGuess: String,
    showVictoryScreen: MutableState<Boolean>,
    coinManager: CoinManager
) {
    letters.subList(startIndex, endIndex).forEachIndexed { index, enable ->
        Button(
            onClick = {
                if (wordToGuess.length > enteredLetters.size) {
                    for (i in wordToGuess.indices) {
                        if (!enteredLetters.containsKey(i)) {
                            enteredLetters[i] = index + startIndex
                            enables[index + startIndex] = false
                            if (enteredLetters.size == wordToGuess.length) {
                                val correctGuesses =
                                    enteredLetters.entries.count { (index, letter) ->
                                        if (letter == SPACE_INDEX) {
                                            wordToGuess[index] == ' '
                                        } else {
                                            letters[letter] == wordToGuess[index]
                                        }
                                    }
                                if (correctGuesses == wordToGuess.length) {
                                    showVictoryScreen.value = true
                                    coinManager.addCoins(30)
                                }
                            }
                            break
                        }
                    }
                }
            },
            modifier = Modifier
                .size(50.dp, 50.dp)
                .padding(0.dp),
            shape = RoundedCornerShape(15.dp),
            enabled = enables[index + startIndex]
        ) {
            Text(
                text = letters[index + startIndex].toString(),
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
    }
}

fun getButtonSize(wordLength: Int): Int {
    return if(380 / wordLength > 50){
        50
    }
    else {
        (380 / wordLength)
    }
}

@Composable
fun VictoryScreen(wordToGuess: String, onContinueClick: () -> Unit, coinManager: CoinManager) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        CoinsDisplay(coinManager = coinManager)
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = CONGRATS_TEXT,
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )

            Text(
                text = GUESSED_WORD_TEXT + wordToGuess,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )

            Text(
                text = ADD_COINS_TEXT,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )

            Button(
                onClick = { onContinueClick() },
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth()
            ) {
                Text(text = CONTINUE_TEXT)
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImageItem(imageUrl: String) {
    GlideImage(
        model = imageUrl,
        contentDescription = IMAGE_CONTENT_DESCRIPTION,
        modifier = Modifier
            .size(200.dp, 200.dp)
            .padding(8.dp),
        contentScale = ContentScale.Crop
    )
}
