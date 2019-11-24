package one.mann.weatherman

/*
TODO:
 Update Database model -> Atomize and make it adhere to single responsibility principle (split-up into multiple tables)
 Make Weather data model less repetitive by getting rid of redundant operations (replace with arrays/lists)
 Use relevant data types in Weather model and database instead of using strings everywhere
 Make coroutine context injectable (?) or replace it with kotlin lifecycle extension (?)
 Split-up BaseLocationActivity into separate base activities (?)
 Replace OpenWeatherMap API and Google Places API with Dark Sky and TomTom respectively and remove Teleport is possible
 Get weather for current location (instead of previous saved location) in notifications if GPS is enabled on device
 Implement Coroutine Flow for TomTom
 Add tests for all modules
 Handle all network responses from API calls
 Write and test Proguard rules
 Migrate to ViewPager2
 Add more weather data parameters (detailed forecasts, maps, etc)
 Implement View Binding and remove Kotlin synthetics
 Follow standardised naming convention for views [refactor]
 Handle navBar hidden usecase (views should resize accordingly)
 Align and center ForecastGraphView lines to forecast columns
 Implement CI/CD (Jenkins)
 Implement Crashlytics
 */