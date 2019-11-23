package one.mann.weatherman.ui.main

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import one.mann.domain.logic.truncate
import one.mann.domain.model.Errors.*
import one.mann.domain.model.location.Location
import one.mann.weatherman.R
import one.mann.weatherman.WeatherManApp
import one.mann.weatherman.api.common.Keys
import one.mann.weatherman.ui.common.base.BaseLocationActivity
import one.mann.weatherman.ui.common.util.getViewModel
import one.mann.weatherman.ui.main.adapter.MainPagerAdapter
import one.mann.weatherman.ui.settings.SettingsActivity
import javax.inject.Inject

internal class MainActivity : BaseLocationActivity() {

    /*
    TODO:
     Update Database model, make it adhere to single responsibility principle (split into multiple tables) and Atomicity
     ^ Make Weather data model less repetitive by getting rid of redundant operations
     Update all dependencies
     Make coroutine context injectable (?) or replace it with kotlin lifecycle extension (?)
     Change data APIs to Dark Sky and TomTom
     Implement Coroutine Flow for TomTom
     Add tests for all modules
     Handle all network responses from API calls
     Write and test Proguard rules
     Migrate to ViewPager2
     Add more weather data parameters (detailed forecasts, maps, etc)
     Implement View Binding and remove Kotlin synthetics
     Handle navBar hidden usecase (views should resize accordingly)
     Align and center ForecastGraphView lines to forecast columns
     Implement CI/CD (Jenkins)
     */

    companion object {
        private const val AUTOCOMPLETE_REQUEST_CODE = 1021
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val mainViewModel: MainViewModel by lazy { getViewModel(viewModelFactory) }
    private val mainPagerAdapter by lazy { MainPagerAdapter(supportFragmentManager) }
    private var countObserved = false // Stop multiple location alerts on first run
    private var isFirstRun = true // Check if this is the first time app is running

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        handleLocationPermission { initActivity(it) }
    }

    /** Check result of Autocomplete API widget's request */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val placeLoc = Autocomplete.getPlaceFromIntent(data!!).latLng // Get coordinates from intent
            mainViewModel.addCity(Location(listOf(placeLoc!!.latitude.toFloat(), placeLoc.longitude.toFloat())).truncate())
        }
    }

    override fun injectDependencies() = WeatherManApp.appComponent.getSubComponent().injectMainActivity(this)

    private fun initActivity(permissionGranted: Boolean) {
        if (!permissionGranted) { // If permission denied then exit
            toast(R.string.permission_required)
            finish()
            return
        }
        main_view_pager.apply {
            adapter = mainPagerAdapter
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
                override fun onPageSelected(position: Int) {}
                override fun onPageScrollStateChanged(state: Int) { // Fix horizontal scrolling
                    if (!main_swipe_ly.isRefreshing) main_swipe_ly.isEnabled = state == ViewPager.SCROLL_STATE_IDLE
                }
            })
        }
        main_swipe_ly.apply {
            setColorSchemeColors(Color.RED, Color.BLUE)
            setOnRefreshListener { handleLocationServiceResult() } // Prompt for location update if it is first run
        }
        mainViewModel.uiState.observe(::getLifecycle, ::observeUiState)
    }

    private fun handleLocationServiceResult() = handleLocationPermission { permissionGranted ->
        if (permissionGranted) checkLocationService(isFirstRun) { mainViewModel.handleRefreshing(it, isFirstRun) }
    }

    private fun observeUiState(state: MainViewState) {
        main_swipe_ly.isRefreshing = state.isRefreshing
        when (state.error) {
            NO_INTERNET -> toast(R.string.no_internet_connection)
            NO_GPS -> toast(R.string.gps_needed_for_location)
            NO_LOCATION -> toast(R.string.location_settings_not_available)
            NO_RESPONSE -> toast(R.string.error_has_occurred_try_again)
            NO_ERROR -> run { return@run } // A workaround for no break support inside when statements
        }
        when (val count = state.cityCount) {
            -1 -> run { return@run }
            0 -> if (!countObserved) { // If cityCount is 0 then this is the app's first run
                handleLocationServiceResult() // Add current user location, prompt for location update
                countObserved = true // This ensures that handleLocationServiceResult() is only called once here
            }
            else -> { // Show Snackbar when user adds a city for the first time
                if (count == 2 && !mainViewModel.navigationGuideShown()) navigationGuideSnack().show()
                if (isFirstRun) main_toolbar.init() // Ensures this function is only called once
                mainPagerAdapter.updatePages(count)
                isFirstRun = false
            }
        }
    }

    private fun Toolbar.init() = apply {
        if (!menu.hasVisibleItems()) inflateMenu(R.menu.menu_main) // Inflate menu directly into toolbar
        setOnMenuItemClickListener { menuItem ->
            when (menuItem!!.itemId) {
                R.id.menu_add_city -> if (main_view_pager.adapter!!.count < 10) autocompleteWidget() // Limit cities to 10
                else toast(R.string.remove_a_city_before_adding)
                R.id.menu_remove_city -> removeCityAlert().show()
                R.id.menu_settings -> startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
            }
            false
        }
    }

    private fun removeCityAlert() = AlertDialog.Builder(this, R.style.AlertDialogTheme)
            .setTitle(getString(R.string.remove_city_location))
            .setMessage(getString(R.string.do_you_want_to_remove_location))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                val position = main_view_pager.currentItem
                if (position == 0) toast(R.string.cant_remove_first_location)
                else {
                    mainViewModel.removeCity(position)
                    toast(R.string.location_removed)
                }
            }
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .create()

    private fun navigationGuideSnack() = Snackbar
            .make(activity_main_coord_ly, getString(R.string.swipe_left_or_right), Snackbar.LENGTH_INDEFINITE)
            .setAction(getString(R.string.got_it)) { mainViewModel.setNavigationGuideShown() }
            .setBackgroundTint(ContextCompat.getColor(this, R.color.dayClearStart))
            .setActionTextColor(ContextCompat.getColor(this, R.color.sunriseClearCenter))
            .apply {
                val params = view.layoutParams as CoordinatorLayout.LayoutParams
                params.anchorId = R.id.snackbar_anchor // Add bottom padding for navigation bar
                params.anchorGravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
                params.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
                view.elevation = 0f // Remove shadow
                view.layoutParams = params
            }

    /** Widget for Places Autocomplete API which needs to run in activity scope */
    private fun autocompleteWidget() = try {
        if (!Places.isInitialized()) Places.initialize(applicationContext, Keys.PLACES_APP_KEY)
        val filter: List<Place.Field> = listOf(Place.Field.LAT_LNG)
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, filter).build(this)
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
    } catch (ignored: GooglePlayServicesRepairableException) {
    } catch (ignored: GooglePlayServicesNotAvailableException) {
    }
}