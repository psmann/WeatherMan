# WeatherMan

An Android weather app that shows current and forecast weather for user location and other cities.
Uses OpenWeatherMap API for weather information and TomTom Search API to get cities.

## Screenshots

<img src="https://user-images.githubusercontent.com/42505064/64204739-66e75980-ce64-11e9-958f-9e88ca867895.jpg" width="32%"></img> 
<img src="https://user-images.githubusercontent.com/42505064/64204740-66e75980-ce64-11e9-900a-796ce04e3d8f.jpg" width="32%"></img> 
<img src="https://user-images.githubusercontent.com/42505064/64204742-66e75980-ce64-11e9-8b98-8a9d94895d97.jpg" width="32%"></img> 
<img src="https://user-images.githubusercontent.com/42505064/63302704-d9c0d400-c2ab-11e9-900c-5e464d627b35.jpg" width="32%"></img> 
<img src="https://user-images.githubusercontent.com/42505064/63302705-d9c0d400-c2ab-11e9-8aaa-d8b8a0c7bcd6.jpg" width="32%"></img> 
<img src="https://user-images.githubusercontent.com/42505064/63302706-d9c0d400-c2ab-11e9-88ed-fcdb1ee6042b.jpg" width="32%"></img> 

## Project Structure

The project has a modular structure and uses an implementation of Clean Architecture. 
MVVM pattern with the help of Android Architecture Components is used in the presentation layer.
Code is written in Kotlin and uses coroutines to handle all asynchronous work.

### Modules

  1) domain (Kotlin): Contains all the domain level business logic such as data entities and algorithms.
  2) interactors (Kotlin): Contains usecases and repository patterns.
  3) app (Android): This is the presentation module. Contains all UI and framework code (including API services).

### Dependencies

  * [Dagger 2](https://dagger.dev/) - Dependency Injection
  * [Retrofit 2](https://square.github.io/retrofit/) - Network calls
  * [OkHttp 3](https://square.github.io/okhttp/) - Network calls
  * [Room Persistence Library](https://developer.android.com/topic/libraries/architecture/room) - SQLite Database
  * [Kotlin Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - Asynchronous programming
  * [Work Manager](https://developer.android.com/topic/libraries/architecture/workmanager) - Background tasks (data sync, notifications)
  * [Google Play Services](https://developers.google.com/android/reference/com/google/android/gms/location/package-summary) - Device GPS location
  * [TomTom Search API](https://developer.tomtom.com/search-api) - City names and locations
  * [OpenWeatherMap API](https://openweathermap.org/api) - Weather information
  * [Teleport Timezone API](https://developers.teleport.org/api/resources/Timezone/) - Time zones

## Getting Started

1) Clone the project repository.
2) Generate keys for OpenWeatherMap API and TomTom Search API.
3) Add your keys to [app/../api/common/Keys.kt](app/src/main/java/one/mann/weatherman/api/common/Keys.kt) and rebuild project.