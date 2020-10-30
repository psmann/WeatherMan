# WeatherMan

An Android weather app that shows current and forecast weather for user location and other cities.
Uses OpenWeatherMap API for weather data and TomTom API to get cities.

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
  2) interactors (Kotlin): Contains usecases and repository patterns
  3) app (Android): This is the presentation module. Contains all UI and framework code (including API services).

### Dependencies

  * <a href="https://dagger.dev/">Dagger 2</a> - Dependency Injection
  * <a href="https://square.github.io/retrofit/">Retrofit 2</a> - Network calls
  * <a href="https://square.github.io/okhttp/">OkHttp 3</a> - Network calls
  * <a href="https://developer.android.com/topic/libraries/architecture/room">Room Persistence Library</a> - SQLite Database
  * <a href="https://kotlinlang.org/docs/reference/coroutines-overview.html">Kotlin Coroutines</a> - Asynchronous tasks
  * <a href="https://developer.android.com/topic/libraries/architecture/workmanager">WorkManager</a> - Background tasks (data sync, notifications)
  * <a href="https://developers.google.com/android/reference/com/google/android/gms/location/package-summary">Google Play Services</a> - Device GPS location
  * <a href="https://developer.tomtom.com/">TomTom API</a> - City names and locations
  * <a href="https://openweathermap.org/api">OpenWeatherMap API</a> - Weather data

## Getting Started

1) Clone the project repository
2) Generate keys for OpenWeatherMap API and TomTom API
3) Add your keys to api/common/Keys.kt located inside the app module and rebuild project