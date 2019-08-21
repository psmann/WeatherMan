# WeatherMan

An android weather app that shows current and forecast weather for user location and other cities.
Uses OpenWeatherMap API for weather data, Teleport API to get timezones and Google Places API to get city locations.

## Screenshots

<img src="https://user-images.githubusercontent.com/42505064/63302701-d9283d80-c2ab-11e9-94e7-c19b06ddecb2.jpg" width="32%"></img> 
<img src="https://user-images.githubusercontent.com/42505064/63302702-d9283d80-c2ab-11e9-9ab2-ddb93cf2bc58.jpg" width="32%"></img> 
<img src="https://user-images.githubusercontent.com/42505064/63302703-d9c0d400-c2ab-11e9-9e28-47d7ad8bcb9e.jpg" width="32%"></img> 
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

  * Dagger 2 - Dependency Injection
  * Retrofit 2 - API calls
  * OkHttp 3 - API calls
  * Room Persistence Library - Database
  * Kotlin Coroutines - Asynchronous tasks
  * WorkManager - Background tasks (data sync, notifications)
  * Google Play Services - Device GPS location
  * Google Places Autocomplete - Remote city locations
  * OpenWeatherMap - Weather data
  * Teleport - Timezones
  
## Getting Started

1) Clone the project repository
2) Generate new keys for OpenWeatherMap API and Google Places API
3) Add your keys to api/common/Keys.kt located inside the app module and rebuild project