package one.mann.weatherman.ui.main

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_IDLE
import com.google.android.material.snackbar.Snackbar
import one.mann.domain.logic.truncate
import one.mann.domain.models.Direction
import one.mann.domain.models.ErrorType.*
import one.mann.domain.models.ViewPagerUpdateType
import one.mann.domain.models.ViewPagerUpdateType.*
import one.mann.domain.models.location.Location
import one.mann.weatherman.R
import one.mann.weatherman.WeatherManApp
import one.mann.weatherman.common.MAXIMUM_CITIES_ALLOWED
import one.mann.weatherman.common.PAGER_COUNT
import one.mann.weatherman.common.PAGER_POSITION
import one.mann.weatherman.databinding.ActivityMainBinding
import one.mann.weatherman.ui.common.base.BaseLocationActivity
import one.mann.weatherman.ui.common.util.getViewModel
import one.mann.weatherman.ui.common.util.setSlideAnimation
import one.mann.weatherman.ui.main.MainUiModel.State.*
import one.mann.weatherman.ui.main.adapters.MainViewPagerAdapter
import one.mann.weatherman.ui.main.adapters.SearchCityRecyclerAdapter
import one.mann.weatherman.ui.settings.SettingsActivity
import javax.inject.Inject

/* Created by Psmann. */

internal class MainActivity : BaseLocationActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var countObserved = false // To stop multiple location alerts on first run
    private var isFirstRun = true // To check if this is the first time app is running
    private var removeViewPagerItem = false // To check if viewPager item is being removed
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val mainViewModel: MainViewModel by lazy { getViewModel(viewModelFactory) }
    private val mainViewPagerAdapter by lazy { MainViewPagerAdapter(supportFragmentManager, lifecycle) }
    private val inputMethodManager by lazy { getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager }
    private val searchCityRecyclerAdapter by lazy {
        SearchCityRecyclerAdapter { mainViewModel.addCity(Location(listOf(it.latitude, it.longitude)).truncate()) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        handleLocationPermission { initActivity(it) }
    }

    /** Saves ViewPager position and page count on configuration change */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(PAGER_POSITION, binding.viewPager.currentItem)
        outState.putInt(PAGER_COUNT, binding.viewPager.adapter?.itemCount!!)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        mainViewPagerAdapter.updatePages(savedInstanceState.getInt(PAGER_COUNT))
        binding.viewPager.currentItem = savedInstanceState.getInt(PAGER_POSITION)
    }

    override fun onBackButtonPressed() = when {
        binding.itemSearchCityConstraintLayout.root.visibility == View.VISIBLE -> hideSearchView()
        binding.viewPager.currentItem != 0 -> binding.viewPager.currentItem = 0
        else -> super.onBackButtonPressed()
    }

    override fun injectDependencies() = WeatherManApp.appComponent.getSubComponent().injectMainActivity(this)

    private fun initActivity(permissionGranted: Boolean) {
        // If permission is denied then exit
        if (!permissionGranted) {
            toast(R.string.permission_required)
            finish()
            return
        }
        mainViewModel.uiModel.observe(::getLifecycle, ::observeUiModel)
        binding.apply {
            // Set up the ViewPager
            viewPager.apply {
                adapter = mainViewPagerAdapter
                registerOnPageChangeCallback(object : OnPageChangeCallback() {
                    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                        super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                        // Complete scrolling to previous item before removing item
                        if (removeViewPagerItem && positionOffset == 0f) {
                            mainViewPagerAdapter.updatePages(binding.viewPager.adapter?.itemCount!! - 1)
                            removeViewPagerItem = false
                        }
                    }

                    override fun onPageScrollStateChanged(state: Int) {
                        super.onPageScrollStateChanged(state)
                        if (!mainSwipeLayout.isRefreshing) mainSwipeLayout.isEnabled = state == SCROLL_STATE_IDLE
                    }
                })
            }
            // Set up the Swipe Refresh Layout
            mainSwipeLayout.apply {
                setColorSchemeColors(Color.RED, Color.BLUE)
                // Prompt for location update if it is first run
                setOnRefreshListener { handleLocationServiceResult() }
            }
            // Set up the City Search Layout
            itemSearchCityConstraintLayout.let {
                // Hide searchView when clicked anywhere outside it
                it.root.setOnClickListener { hideSearchView() }
                it.searchResultRecyclerView.apply {
                    adapter = searchCityRecyclerAdapter
                    setHasFixedSize(true)
                }
                it.citySearchView.setOnQueryTextListener(object : OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean = false
                    override fun onQueryTextChange(newText: String?): Boolean {
                        newText?.let { searchQuery -> mainViewModel.searchCity(searchQuery) }
                        return true
                    }
                })
                val searchCloseButton: View? = it.citySearchView.findViewById(androidx.appcompat.R.id.search_close_btn)
                searchCloseButton?.setOnClickListener { hideSearchView() }
            }
        }
    }

    private fun handleLocationServiceResult() = handleLocationPermission { permissionGranted ->
        if (permissionGranted) checkLocationService(isFirstRun) { mainViewModel.handleRefreshing(it, isFirstRun) }
    }

    private fun observeUiModel(model: MainUiModel) {
        binding.mainSwipeLayout.isRefreshing = model.viewState is Refreshing
        searchCityRecyclerAdapter.update(model.citySearchResult)
        when (val state = model.viewState) {
            is Refreshing -> hideSearchView()
            is ShowError -> when (val errorType = state.errorType) {
                NoInternet -> toast(R.string.no_internet_connection)
                NoGps -> toast(R.string.gps_needed_for_location)
                NoLocation -> toast(R.string.location_settings_not_available)
                CityAlreadyExists -> toast(R.string.city_already_exists, Toast.LENGTH_LONG)
                is NoResponse -> toast(R.string.network_error, errorMessage = errorType.message)
            }
            is UpdateViewPager -> updateViewPager(model.weatherData.size, state.updateType)
            else -> run { return@run }
        }
        when (val cities = model.cityCount) {
            // cityCount is -1 before data has been initialised, in which case do nothing
            -1 -> run { return@run }
            // If cityCount is 0 then this is the app's first run
            0 -> if (!countObserved) {
                // Add current user location, prompt for location update
                handleLocationServiceResult()
                // Ensures handleLocationServiceResult() is only called once here
                countObserved = true
            }
            // Show Snackbar when user adds a city for the first time
            else -> {
                if (cities == 2 && !mainViewModel.navigationGuideShown()) appNavigationGuideSnack().show()
                if (isFirstRun) binding.toolbar.init()
                // Ensures this function is only called once
                isFirstRun = false
            }
        }
    }

    /** Handles ViewPager update scenarios */
    private fun updateViewPager(pageCount: Int, updateType: ViewPagerUpdateType) = when (updateType) {
        // Just update pager data set
        SET_SIZE -> mainViewPagerAdapter.updatePages(pageCount)
        // Update pager data set and move to the new item that was added
        ADD_ITEM -> {
            mainViewPagerAdapter.updatePages(pageCount)
            binding.viewPager.setCurrentItem(pageCount - 1, true)
            toast(R.string.location_added)
        }
        // Move to previous item and update the viewPager inside onPageScrolled() callback
        REMOVE_ITEM -> {
            binding.viewPager.setCurrentItem(binding.viewPager.currentItem - 1, true)
            removeViewPagerItem = true
            toast(R.string.location_removed)
        }
        // Do nothing
        NO_CHANGE -> run { return@run }
    }

    private fun Toolbar.init() = apply {
        // Inflate menu directly into toolbar
        if (!menu.hasVisibleItems()) inflateMenu(R.menu.menu_main)
        setOnMenuItemClickListener { menuItem ->
            when (menuItem!!.itemId) {
                // Add new city, limit is set to 10
                R.id.menu_add_city -> if (binding.viewPager.adapter?.itemCount!! < MAXIMUM_CITIES_ALLOWED) {
                    binding.itemSearchCityConstraintLayout.let {
                        it.root.visibility = View.VISIBLE
                        it.root.setSlideAnimation(Direction.LEFT)
                        it.citySearchView.requestFocus()
                        // Force show keyboard
                        @Suppress("DEPRECATION") // Warning suppressed until suitable replacement is found
                        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
                    }
                } else toast(R.string.remove_a_city_before_adding)
                R.id.menu_remove_city -> removeCityAlert().show()
                R.id.menu_settings -> startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
            }
            false
        }
    }

    /** Hides searchView and clears query */
    private fun hideSearchView() = binding.itemSearchCityConstraintLayout.let {
        it.root.visibility = View.GONE
        it.citySearchView.clearFocus()
        it.citySearchView.setQuery("", false)
        // Remove previous list
        searchCityRecyclerAdapter.update()
    }

    private fun removeCityAlert(): AlertDialog {
        return AlertDialog.Builder(this, R.style.AlertDialogTheme)
            .setTitle(getString(R.string.remove_city_location))
            .setMessage(getString(R.string.do_you_want_to_remove_location))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                val position = binding.viewPager.currentItem
                if (position == 0) toast(R.string.cant_remove_first_location) else mainViewModel.removeCity(position)
            }
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .create()
    }

    private fun appNavigationGuideSnack(): Snackbar {
        return Snackbar.make(binding.root, getString(R.string.swipe_left_or_right), Snackbar.LENGTH_INDEFINITE)
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
    }
}