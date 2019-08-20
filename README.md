# WeatherMan

A weather app that shows present and forecast weather for current user location and other cities.
Uses Open Weather Map API for weather data, Teleport API to get timezones and Google Places API to get city locations.

## Screenshots

<img src="https://user-images.githubusercontent.com/42505064/63302701-d9283d80-c2ab-11e9-94e7-c19b06ddecb2.jpg" width="33%"></img> 
<img src="https://user-images.githubusercontent.com/42505064/63302702-d9283d80-c2ab-11e9-9ab2-ddb93cf2bc58.jpg" width="33%"></img> 
<img src="https://user-images.githubusercontent.com/42505064/63302703-d9c0d400-c2ab-11e9-9e28-47d7ad8bcb9e.jpg" width="33%"></img> 
<img src="https://user-images.githubusercontent.com/42505064/63302704-d9c0d400-c2ab-11e9-900c-5e464d627b35.jpg" width="33%"></img> 
<img src="https://user-images.githubusercontent.com/42505064/63302705-d9c0d400-c2ab-11e9-8aaa-d8b8a0c7bcd6.jpg" width="33%"></img> 
<img src="https://user-images.githubusercontent.com/42505064/63302706-d9c0d400-c2ab-11e9-88ed-fcdb1ee6042b.jpg" width="33%"></img> 

## Project Structure

The project uses an implementation of Clean Architecture. 
MVVM pattern (Android Architecture Components) is used in the presentation layer.
Code is written in Kotlin and uses Coroutines for asynchronous work.

### Modules

  1) domain (Kotlin): Contains all the domain level business logic such as entities and data algorithms.
  2) interactors (Kotlin): Contains usecases and repository patterns
  3) app (Android): This is the presentation module. Contains all UI and framework code (including API services).

### Dependencies

  * Dagger 2 - Dependency Injection
  * Retrofit 2 - API calls
  * OkHttp 3 - API calls
  * Room Persistence Library - Database
  * WorkManager - Background tasks (data sync, notifications)
  * Google Play Services - GPS location
  * Google Places Autocomplete - Remote city locations
  * Open Weather Map - Weather data
  * Teleport - Timezones
  
## Getting Started

1) Clone the repository.
2) Create new keys for OpenWeatherMap API and Google Places API.
3) Add your keys in api/common/Keys.kt file located in the app module and rebuild project.