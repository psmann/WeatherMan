package one.mann.weatherman.ui.main

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import one.mann.domain.logic.truncate
import one.mann.domain.models.Errors.*
import one.mann.domain.models.ViewPagerUpdateType
import one.mann.domain.models.ViewPagerUpdateType.*
import one.mann.domain.models.location.Location
import one.mann.weatherman.R
import one.mann.weatherman.WeatherManApp
import one.mann.weatherman.databinding.ActivityMainBinding
import one.mann.weatherman.ui.common.base.BaseLocationActivity
import one.mann.weatherman.ui.common.util.getViewModel
import one.mann.weatherman.ui.main.MainUiModel.State.*
import one.mann.weatherman.ui.main.adapters.MainViewPagerAdapter
import one.mann.weatherman.ui.main.adapters.SearchCityRecyclerAdapter
import one.mann.weatherman.ui.settings.SettingsActivity
import javax.inject.Inject

/* Created by Psmann. */

internal class MainActivity : BaseLocationActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var countObserved = false // Stop multiple location alerts on first run
    private var isFirstRun = true // Check if this is the first time app is running
    private var removeViewPagerItem = false // Check if viewPager item is being removed
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val mainViewModel: MainViewModel by lazy { getViewModel(viewModelFactory) }
    private val mainViewPagerAdapter by lazy { MainViewPagerAdapter(supportFragmentManager, lifecycle) }
    private val inputMethodManager by lazy { getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager }
    private val searchCityRecyclerAdapter by lazy {
        SearchCityRecyclerAdapter { mainViewModel.addCity(Location(listOf(it.latitude, it.longitude)).truncate()) } // Add new city
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        handleLocationPermission { initActivity(it) }
    }

    override fun injectDependencies() = WeatherManApp.appComponent.getSubComponent().injectMainActivity(this)

    /** Hide the searchView if it is visible instead of exiting activity */
    override fun onBackPressed() {
        if (binding.itemSearchCityConstraintLayout.root.visibility == View.VISIBLE) hideSearchView() else super.onBackPressed()
    }

    private fun initActivity(permissionGranted: Boolean) {
        if (!permissionGranted) { // If permission is denied then exit
            toast(R.string.permission_required)
            finish()
            return
        }
        mainViewModel.uiModel.observe(::getLifecycle, ::observeUiModel)
        binding.apply {
            // Set up the ViewPager
            viewPager.apply {
                adapter = mainViewPagerAdapter
                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
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
                        if (!mainSwipeLayout.isRefreshing) mainSwipeLayout.isEnabled = state == ViewPager2.SCROLL_STATE_IDLE
                    }
                })
            }
            // Set up the Swipe Refresh Layout
            mainSwipeLayout.apply {
                setColorSchemeColors(Color.RED, Color.BLUE)
                setOnRefreshListener { handleLocationServiceResult() } // Prompt for location update if it is first run
            }
            // Set up the City Search Layout
            itemSearchCityConstraintLayout.let {
                it.root.setOnClickListener { hideSearchView() } // Hide searchView when clicked anywhere outside it
                it.searchResultRecyclerView.apply {
                    adapter = searchCityRecyclerAdapter
                    setHasFixedSize(true)
                }
                it.citySearchView.setOnQueryTextListener(object : OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean = false
                    override fun onQueryTextChange(newText: String?): Boolean {
                        newText?.let { searchQuery ->
                            if (searchQuery == "" || searchQuery.length < 3) searchCityRecyclerAdapter.update()
                            else mainViewModel.searchCity(searchQuery)
                        }
                        return true
                    }
                })
                val searchViewCloseButton: View? = it.citySearchView.findViewById(androidx.appcompat.R.id.search_close_btn)
                searchViewCloseButton?.setOnClickListener { hideSearchView() }
            }
        }
    }

    private fun handleLocationServiceResult() = handleLocationPermission { permissionGranted ->
        if (permissionGranted) checkLocationService(isFirstRun) { mainViewModel.handleRefreshing(it, isFirstRun) }
    }

    private fun observeUiModel(model: MainUiModel) {
        binding.mainSwipeLayout.isRefreshing = model.viewState is Refreshing
        if (model.citySearchResult.isEmpty()) hideSearchView() else searchCityRecyclerAdapter.update(model.citySearchResult)
        when (val state = model.viewState) {
            is ShowError -> when (state.errorType) {
                NoInternet -> toast(R.string.no_internet_connection)
                NoGps -> toast(R.string.gps_needed_for_location)
                NoLocation -> toast(R.string.location_settings_not_available)
                is NoResponse -> toast(R.string.network_error, NoResponse().message)
            }
            is UpdateViewPager -> updateViewPager(model.weatherData.size, state.updateType)
            else -> run { return@run }
        }
        when (val cities = model.cityCount) {
            -1 -> run { return@run } // Before data has been initialised
            0 -> if (!countObserved) { // If cityCount is 0 then this is the app's first run
                handleLocationServiceResult() // Add current user location, prompt for location update
                countObserved = true // This ensures that handleLocationServiceResult() is only called once here
            }
            else -> { // Show Snackbar when user adds a city for the first time
                if (cities == 2 && !mainViewModel.navigationGuideShown()) appNavigationGuideSnack().show()
                if (isFirstRun) binding.toolbar.init() // Ensures this function is only called once
                isFirstRun = false
            }
        }
    }

    /** Handle ViewPager update scenarios */
    private fun updateViewPager(pageCount: Int, updateType: ViewPagerUpdateType) = when (updateType) {
        SET_SIZE -> mainViewPagerAdapter.updatePages(pageCount) // Just update pager data set
        ADD_ITEM -> {
            mainViewPagerAdapter.updatePages(pageCount) // Update pager data set
            binding.viewPager.setCurrentItem(pageCount - 1, true) // Move to the new item that was added
            toast(R.string.location_added)
        }
        REMOVE_ITEM -> {
            binding.viewPager.setCurrentItem(binding.viewPager.currentItem - 1, true) // Move to previous item
            removeViewPagerItem = true // Setting this to true updates the viewPager inside onPageScrolled() callback
            toast(R.string.location_removed)
        }
        NO_CHANGE -> run { return@run } // Do nothing
    }

    private fun Toolbar.init() = apply {
        if (!menu.hasVisibleItems()) inflateMenu(R.menu.menu_main) // Inflate menu directly into toolbar
        setOnMenuItemClickListener { menuItem ->
            when (menuItem!!.itemId) {
                R.id.menu_add_city -> if (binding.viewPager.adapter?.itemCount!! < 10) { // Limit cities to 10
                    binding.itemSearchCityConstraintLayout.let {
                        it.root.visibility = View.VISIBLE
                        it.citySearchView.requestFocus()
                        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0) // Force show keyboard
                    }
                } else toast(R.string.remove_a_city_before_adding)
                R.id.menu_remove_city -> removeCityAlert().show()
                R.id.menu_settings -> startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
            }
            false
        }
    }

    /** Hide searchView and clear query */
    private fun hideSearchView() = binding.itemSearchCityConstraintLayout.let {
        it.root.visibility = View.GONE
        it.citySearchView.clearFocus()
        it.citySearchView.setQuery("", false)
        searchCityRecyclerAdapter.update() // Remove previous list
    }

    private fun removeCityAlert() = AlertDialog.Builder(this, R.style.AlertDialogTheme)
            .setTitle(getString(R.string.remove_city_location))
            .setMessage(getString(R.string.do_you_want_to_remove_location))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                val position = binding.viewPager.currentItem
                if (position == 0) toast(R.string.cant_remove_first_location)
                else mainViewModel.removeCity(position)
            }
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .create()

    private fun appNavigationGuideSnack() = Snackbar
            .make(binding.root, getString(R.string.swipe_left_or_right), Snackbar.LENGTH_INDEFINITE)
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