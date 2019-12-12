package one.mann.weatherman

/*
TODO:
 Update Database model: Atomise and make it adhere to single responsibility principle (split-up into multiple tables)
 Make Weather data model less repetitive by getting rid of redundant operations (replace with arrays/lists)
 Use relevant data types in Weather model and database instead of using strings everywhere
 Make coroutine context injectable (?) or replace it with kotlin lifecycle extension (?)
 Split-up BaseLocationActivity into separate base activities (?)
 Bug: 1) The same city can be added multiple times 2) Deleting one city also removes any other city having the same name
 Replace OpenWeatherMap API and Google Places API with Dark Sky and TomTom respectively, remove Teleport API if possible
 Get weather for current location (instead of previous saved location) in notifications if GPS is enabled on device
 Implement Coroutine Flow for TomTom
 Add tests for all modules
 Handle all network responses from API calls explicitly
 Write and test Proguard rules
 Migrate ViewPager to ViewPager2
 Add more weather data parameters (detailed forecasts, maps, UV index, etc)
 Handle navBar hidden usecase (screen layouts should resize accordingly to fill the empty space)
 Align and center ForecastGraphView lines (by increasing lengths by the amount they are being clipped at intersections)
 Implement CI/CD (Jenkins or CircleCI)
 Implement Crashlytics (Firebase)
// Implement View Binding and remove Kotlin synthetics -> DONE (except recycler view adapter and viewHolder)
// Follow standardised naming convention for views (refactor) -> DONE
 */