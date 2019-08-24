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
import one.mann.domain.model.Location
import one.mann.domain.model.LocationResponse.*
import one.mann.domain.model.LocationType.DB
import one.mann.domain.model.LocationType.DEVICE
import one.mann.weatherman.R
import one.mann.weatherman.WeatherManApp
import one.mann.weatherman.api.common.Keys
import one.mann.weatherman.ui.common.base.BaseActivity
import one.mann.weatherman.ui.common.util.getViewModel
import one.mann.weatherman.ui.main.adapter.MainPagerAdapter
import one.mann.weatherman.ui.settings.SettingsActivity
import javax.inject.Inject

internal class MainActivity : BaseActivity() {

    companion object {
        private const val AUTOCOMPLETE_REQUEST_CODE = 1021
    }

    private var isFirstRun = true
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val mainViewModel: MainViewModel by lazy { getViewModel(viewModelFactory) }
    private val mainPagerAdapter by lazy { MainPagerAdapter(supportFragmentManager) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        handleLocationPermission { initActivity(it) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) if (resultCode == Activity.RESULT_OK) {
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
        var countObserved = false // Stop multiple location alerts on first run
        main_viewPager.apply {
            adapter = mainPagerAdapter
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
                override fun onPageSelected(position: Int) {}
                override fun onPageScrollStateChanged(state: Int) { // Fix horizontal scrolling
                    if (!main_swipe_ly.isRefreshing) main_swipe_ly.isEnabled = state == ViewPager.SCROLL_STATE_IDLE
                }
            })
        }
        mainViewModel.uiState.observe(this@MainActivity) {
            val cityCount = it.cityCount
            main_swipe_ly.isRefreshing = it.isLoading
            if (it.showError) toast(R.string.error_has_occurred_try_again)
            if (cityCount == 0) { // If cityCount is 0 then this is the app's first run
                if (!countObserved) {
                    handleLocationServiceResult()
                    countObserved = true // This ensures that handleLocationServiceResult() is only called once here
                }
            } else { // Show Snackbar when user adds a city for the first time
                if (cityCount == 2 && !mainViewModel.navigationGuideShown()) navigationGuideSnack().show()
                if (isFirstRun) main_toolbar.init() // Ensures this function is only called once
                mainPagerAdapter.updatePages(cityCount)
                isFirstRun = false
            }
        }
        main_swipe_ly.apply {
            setColorSchemeColors(Color.RED, Color.BLUE)
            setOnRefreshListener { handleLocationServiceResult() }
        }
    }

    private fun handleLocationServiceResult() = handleLocationPermission { permissionGranted ->
        if (permissionGranted) checkLocationService {
            when (it) {
                NO_NETWORK -> {
                    toast(R.string.no_internet_connection)
                    main_swipe_ly.isRefreshing = false
                }
                ENABLED ->
                    if (isFirstRun) mainViewModel.addCity()
                    else mainViewModel.updateWeather(DEVICE)
                DISABLED ->
                    if (isFirstRun) toast(R.string.gps_needed_for_location)
                    else mainViewModel.updateWeather(DB)
                UNAVAILABLE -> {
                    toast(R.string.location_settings_not_available)
                    finish()
                }
            }
        }
    }

    private fun Toolbar.init() = apply {
        if (!menu.hasVisibleItems()) inflateMenu(R.menu.menu_main) // Inflate menu directly into toolbar
        setOnMenuItemClickListener {
            when (it!!.itemId) {
                R.id.menu_add_city -> if (main_viewPager.adapter!!.count < 10) autocompleteWidget() // Limit cities to 10
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
                val position = main_viewPager.currentItem
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

    /** Widget for Places Autocomplete API that needs to run in activity scope */
    private fun autocompleteWidget() = try {
        if (!Places.isInitialized()) Places.initialize(applicationContext, Keys.PLACES_APP_KEY)
        val filter: List<Place.Field> = listOf(Place.Field.LAT_LNG)
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, filter).build(this)
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
    } catch (ignored: GooglePlayServicesRepairableException) {
    } catch (ignored: GooglePlayServicesNotAvailableException) {
    }
}