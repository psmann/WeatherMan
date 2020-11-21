package one.mann.weatherman

/*
TODO:
 Update Database model: Atomise and make it adhere to single responsibility principle (split-up into multiple tables)
 Make Weather data model less repetitive by getting rid of redundant operations (replace with arrays/lists)
 Use relevant data types in Weather model and database instead of using strings everywhere
 Bug: 1) The same city can be added multiple times 2) Deleting one city also removes any other city having the same name
 Get weather for current location (instead of previous saved location) in notifications if GPS is enabled on device
 Implement Coroutine Flow for LiveData
 Add tests for all modules
 Write and test Proguard rules - WIP
 Add more weather data parameters (detailed forecasts, maps, UV index, etc)
 Handle navBar hidden usecase (screen layouts should resize accordingly to fill the empty space)
 Implement CI/CD (Jenkins or CircleCI)
 Implement Crashlytics (Firebase)
 Add comments to domain model classes
*/

/*
DONE:
 Implement View Binding and remove Kotlin synthetics
 Follow standardised naming convention for views (refactor)
 Center ForecastGraphView lines (by increasing lengths by the amount they are being clipped at intersections)
 Replace Google Places API with TomTom Search API
 Handle all network responses from API calls explicitly
 Migrate ViewPager to ViewPager2
*/