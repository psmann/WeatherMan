package one.mann.weatherman

/*
TODO:
 Fix navBar and statusBar heights
 Update Database model: Atomise and make it adhere to single responsibility principle (split-up into multiple tables)
 Make Weather data model less repetitive by getting rid of redundant operations (replace with arrays/lists)
 Use relevant data types in Weather model and database instead of using strings everywhere
 Bugs:
  1) The same city can be added multiple times
  2) Deleting one city also removes any other city having the same name
 Handle navBar hidden usecase (screen layouts should resize accordingly to fill the empty space)
 Add tests for all modules
 Implement CI/CD (Jenkins or CircleCI)
*/

/*
DONE:
 Implement View Binding and remove Kotlin synthetics
 Follow standardised naming convention for views (refactor)
 Center ForecastGraphView lines (by increasing lengths by the amount they are being clipped at intersections)
 Replace Google Places API with TomTom Search API
 Handle all network responses from API calls explicitly
 Migrate ViewPager to ViewPager2
 Get weather for current location (instead of previous saved location) in notifications if GPS is enabled on device
 Write and test Proguard rules
*/