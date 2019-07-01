# WeatherMan

A weather app that shows present and forecast weather data for current location and other cities.
Uses Open Weather Map API for weather data, Teleport API to get timezones and Google Places API to get city locations.

The application architecture is inspired by Clean Architecture. 
MVVM pattern (Android Architecture Components) is used in the presentation layer.
Code is written in Kotlin and uses Kotlin coroutines to handle asynchronous tasks.

There are three modules: 
  1) Domain (Kotlin): Contains all the domain level business logic such as entities and data algorithms.
  2) Interactors (Kotlin): Contains usecases and repository patterns
  3) App (Android): This is the presentation module. Contains all the UI and framework code (including Api services).

The following libraries and APIs are used:
  1) Dagger 2 - Dependency Injection
  2) Retrofit 2 - Api calls
  3) OkHttp 3 - Api calls
  4) Glide - Image loading and caching
  5) Room Persistence Library - Database
  6) Google Play Services - GPS location services
  7) Google Places Autocomplete - locations
  8) Open Weather Map - Weather data
  9) Teleport - Timezones
