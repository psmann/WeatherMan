package one.mann.weatherman.ui.detail

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.ViewModelProvider
import one.mann.domain.models.Direction
import one.mann.domain.models.ErrorType.NoInternet
import one.mann.domain.models.ErrorType.NoResponse
import one.mann.weatherman.R
import one.mann.weatherman.WeatherManApp
import one.mann.weatherman.api.openweathermap.isOvercast
import one.mann.weatherman.databinding.ActivityDetailBinding
import one.mann.weatherman.ui.common.base.BaseLocationActivity
import one.mann.weatherman.ui.common.util.*
import one.mann.weatherman.ui.detail.adapters.DetailRecyclerAdapter
import javax.inject.Inject


/* Created by Psmann. */

internal class DetailActivity : BaseLocationActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val binding by lazy { ActivityDetailBinding.inflate(layoutInflater) }
    private val detailViewModel: DetailViewModel by lazy { getViewModel(viewModelFactory) }
    private val detailRecyclerAdapter by lazy { DetailRecyclerAdapter() }
    private val position: Int by lazy { intent.getIntExtra(PAGER_POSITION, 0) }
    private val backgroundResource: Int by lazy { intent.getIntExtra(ACTIVITY_BACKGROUND, getGradient()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.root.setBackgroundResource(backgroundResource)
        handleLocationPermission { initActivity(it) }
    }

    /** Pass RESULT_OK to be handled in CityFragment */
    override fun onBackPressed() {
        setResult(RESULT_OK)
        super.onBackPressed()
    }

    override fun injectDependencies() = WeatherManApp.appComponent.getSubComponent().injectDetailActivity(this)

    private fun initActivity(permissionGranted: Boolean) {
        if (!permissionGranted) { // If permission denied then exit
            toast(R.string.permission_required)
            finish()
            return
        }
        detailViewModel.uiModel.observe(::getLifecycle, ::observeUiModel)
        binding.apply {
            // Set up the Swipe Refresh Layout
            detailSwipeLayout.apply {
                adjustLayoutHeight()
                setColorSchemeColors(Color.RED, Color.BLUE)
                setOnRefreshListener { handleLocationServiceResult() }
            }
        }
    }

    private fun handleLocationServiceResult() = handleLocationPermission { permissionGranted ->
        if (permissionGranted) checkLocationService { detailViewModel.handleRefreshing(it) }
    }

    private fun observeUiModel(model: DetailUiModel) {
        val weatherData = model.weatherData
        binding.detailSwipeLayout.isRefreshing = model.viewState is DetailUiModel.State.Refreshing
        if (model.viewState is DetailUiModel.State.ShowError) when (model.viewState.errorType) {
            NoInternet -> toast(R.string.no_internet_connection)
            is NoResponse -> toast(R.string.network_error, NoResponse().message)
            else -> run { return@run }
        }
        if (weatherData.size >= position + 1) {
            detailRecyclerAdapter.update(weatherData[position])
            if (binding.detailRecyclerView.adapter != detailRecyclerAdapter) binding.detailRecyclerView.apply {
                setHasFixedSize(true)
                adapter = detailRecyclerAdapter
                visibility = View.VISIBLE
                setSlideAnimation(Direction.UP)
            }
            // Update activity background only if it changes after a data refresh
            val newBackground = getGradient(
                    weatherData[position].currentWeather.sunPosition,
                    isOvercast(weatherData[position].currentWeather.iconId)
            )
            if (newBackground != backgroundResource) binding.root.setBackgroundResource(newBackground)
        }
    }

    /** Set correct StatusBar and NavigationBar heights */
    private fun View.adjustLayoutHeight() {
        val density = resources.displayMetrics.density.toInt()
        val statusBarId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        val navBarId = resources.getIdentifier("status_bar_height", "dimen", "android")

        this.apply {
            val params = layoutParams as CoordinatorLayout.LayoutParams
            params.topMargin = if (statusBarId > 0) resources.getDimensionPixelSize(statusBarId) else 48 * density
            params.bottomMargin = when (resources.configuration.orientation) {
                Configuration.ORIENTATION_PORTRAIT -> if (navBarId > 0) resources.getDimensionPixelSize(navBarId) else 0
                else -> 0
            }
            layoutParams = params
        }
    }
}