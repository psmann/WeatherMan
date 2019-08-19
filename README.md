# WeatherMan

A weather app that shows present and forecast weather data for current location and other cities.
Uses Open Weather Map API for weather data, Teleport API to get timezones and Google Places API to get city locations.


<img src="https://user-images.githubusercontent.com/42505064/63302701-d9283d80-c2ab-11e9-94e7-c19b06ddecb2.jpg" width="30%"></img> <img src="https://user-images.githubusercontent.com/42505064/63302702-d9283d80-c2ab-11e9-9ab2-ddb93cf2bc58.jpg" width="30%"></img> <img src="https://user-images.githubusercontent.com/42505064/63302703-d9c0d400-c2ab-11e9-9e28-47d7ad8bcb9e.jpg" width="30%"></img> <img src="https://user-images.githubusercontent.com/42505064/63302704-d9c0d400-c2ab-11e9-900c-5e464d627b35.jpg" width="30%"></img> <img src="https://user-images.githubusercontent.com/42505064/63302705-d9c0d400-c2ab-11e9-8aaa-d8b8a0c7bcd6.jpg" width="30%"></img> <img src="https://user-images.githubusercontent.com/42505064/63302706-d9c0d400-c2ab-11e9-88ed-fcdb1ee6042b.jpg" width="30%"></img> 


The application uses an implementation of Clean Architecture. 
MVVM pattern (Android Architecture Components) is used in the presentation layer.
Code is written in Kotlin and uses Coroutines for asynchronous work.

There are three modules: 
  1) Domain (Kotlin): Contains all the domain level business logic such as entities and data algorithms.
  2) Interactors (Kotlin): Contains usecases and repository patterns
  3) App (Android): This is the presentation module. Contains all UI and framework code (including API services).

The following libraries and APIs are used:
  1) Dagger 2 - Dependency Injection
  2) Retrofit 2 - API calls
  3) OkHttp 3 - API calls
  4) Room Persistence Library - Database
  5) WorkManager - Background tasks (data sync, notifications)
  6) Google Play Services - GPS location
  7) Google Places Autocomplete - Remote city locations
  8) Open Weather Map - Weather data
  9) Teleport - Timezones