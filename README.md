# WeatherMan

A weather app that shows present and forecast weather data for current location and other cities.
Uses Open Weather Map API for weather data, Teleport API to get timezones and Google Places API to get city locations.

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
