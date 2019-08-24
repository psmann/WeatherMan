package one.mann.weatherman.ui.detail

import android.graphics.Color
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_detail.*
import one.mann.domain.model.LocationResponse.*
import one.mann.domain.model.LocationType.DB
import one.mann.domain.model.LocationType.DEVICE
import one.mann.weatherman.R
import one.mann.weatherman.WeatherManApp
import one.mann.weatherman.api.openweathermap.isOvercast
import one.mann.weatherman.ui.common.base.BaseActivity
import one.mann.weatherman.ui.common.util.ACTIVITY_BACKGROUND
import one.mann.weatherman.ui.common.util.PAGER_POSITION
import one.mann.weatherman.ui.common.util.getGradient
import one.mann.weatherman.ui.common.util.getViewModel
import one.mann.weatherman.ui.detail.adapter.DetailRecyclerAdapter
import javax.inject.Inject

internal class DetailActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val detailViewModel: DetailViewModel by lazy { getViewModel(viewModelFactory) }
    private val detailRecyclerAdapter by lazy { DetailRecyclerAdapter() }
    private val position: Int by lazy { intent.getIntExtra(PAGER_POSITION, 1) }
    private val backgroundResource: Int by lazy { intent.getIntExtra(ACTIVITY_BACKGROUND, getGradient()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        activity_detail_coord_ly.setBackgroundResource(backgroundResource)
        handleLocationPermission { initActivity(it) }
    }

    override fun injectDependencies() = WeatherManApp.appComponent.getSubComponent().injectDetailActivity(this)

    private fun initActivity(permissionGranted: Boolean) {
        if (!permissionGranted) { // If permission denied then exit
            toast(R.string.permission_required)
            finish()
            return
        }
        detail_recyclerview.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@DetailActivity)
            adapter = detailRecyclerAdapter
        }
        detail_swipe_ly.apply {
            setColorSchemeColors(Color.RED, Color.BLUE)
            setOnRefreshListener { handleLocationServiceResult() }
        }
        detailViewModel.uiState.observe(this@DetailActivity) { observeUiState(it) }
    }

    private fun handleLocationServiceResult() = handleLocationPermission { permissionGranted ->
        if (permissionGranted) checkLocationService {
            when (it) {
                NO_NETWORK -> {
                    toast(R.string.no_internet_connection)
                    detail_swipe_ly.isRefreshing = false
                }
                ENABLED -> detailViewModel.updateWeather(DEVICE)
                DISABLED -> detailViewModel.updateWeather(DB)
                UNAVAILABLE -> {
                    toast(R.string.location_settings_not_available)
                    finish()
                }
            }
        }
    }

    private fun observeUiState(state: DetailViewModel.ViewState) {
        val weather = state.weatherData
        detail_swipe_ly.isRefreshing = state.isLoading
        if (state.showError) toast(R.string.error_has_occurred_try_again)
        if (weather.size >= position + 1) {
            detailRecyclerAdapter.update(weather[position])
            // Update activity background only if it changes after a data refresh
            val newBackground = getGradient(weather[position].sunPosition, isOvercast(weather[position].iconId))
            if (newBackground != backgroundResource) activity_detail_coord_ly.setBackgroundResource(newBackground)
        }
    }
}