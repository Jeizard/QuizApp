# Викторина от Копатыча

![Jetpack Compose](https://img.shields.io/badge/-Jetpack%20Compose-6200EE?logo=android&logoColor=white)
![Coroutines](https://img.shields.io/badge/-Coroutines-56a8cb?logo=kotlin&logoColor=white)
![Glide](https://img.shields.io/badge/-Glide-4285F4?style=flat-square&logo=android&logoColor=white)
![Jsoup](https://img.shields.io/badge/-Jsoup-373737?style=flat-square)
![Room](https://img.shields.io/badge/-Room-FF8C00?logo=android&logoColor=white)
![LiveData](https://img.shields.io/badge/-LiveData-FF0000?logo=android&logoColor=white)
![ViewModel](https://img.shields.io/badge/-ViewModel-7019E2?logo=android&logoColor=white)

"Викторина от Копатыча" - мобильная игра-викторина, основанная на мультсериале Смешарики, где игрокам предстоит отгадывать название серии по 4 картинкам.

## Скриншоты
<div class="screenshot-row">
  <img src="screenshots/screenshot_1.jpg" alt="Screenshot 1">
  <img src="screenshots/screenshot_2.jpg" alt="Screenshot 2">
  <img src="screenshots/screenshot_3.jpg" alt="Screenshot 3">
  <img src="screenshots/screenshot_4.jpg" alt="Screenshot 4">
</div>


<style>
    .screenshot-row {
        display: flex;
        justify-content: space-between;
    }

    .screenshot-row img {
        width: 23%;
        height: auto;
        max-width: 100%;
    }
</style>


## Описание

 Приложение представляет собой веселую викторину, в которой игроки могут проверить свои знания о мультсериале "Смешарики". Игрокам предлагается угадывать названия серий по предоставленным картинкам. 
 
 Приложение имеет два режима игры:

- **Уровни  :** В этом режиме игроки могут пройти через заранее созданные уровни с вопросами, чтобы проверить свои знания.
- **Бесконечный режим:** В этом режиме приложение использует парсинг данных из интернета для загрузки новых вопросов и картинок, предоставляя бесконечное количество уровней для игры.

## Технологии

- ![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-Android-brightgreen)
- ![Coroutines](https://img.shields.io/badge/Coroutines-Kotlin-blue)
- ![Glide](https://img.shields.io/badge/Glide-Image%20Loading-orange)
- ![Jsoup](https://img.shields.io/badge/Jsoup-HTML%20Parsing-yellow)
- Android Architecture Components:
  - ![Room](https://img.shields.io/badge/Room-Database%20Library-blueviolet)
  - ![LiveData](https://img.shields.io/badge/LiveData-Data%20Observing-red)
  - ![ViewModel](https://img.shields.io/badge/ViewModel-UI%20Related%20Data%20Handling-green)



## Установка

1. Клонируйте репозиторий: `git clone https://github.com/your/repository.git`
2. Откройте проект в Android Studio.
3. Соберите и запустите приложение.

## Использование

- Запустите приложение на устройстве или эмуляторе.
- Выберите режим игры: "Уровни" или "Бесконечный режим".
- Отгадывайте название серий, основываясь на предоставленных 4 изображениях.
