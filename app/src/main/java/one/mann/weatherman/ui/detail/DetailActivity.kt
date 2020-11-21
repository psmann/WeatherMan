package one.mann.weatherman.ui.detail

import android.graphics.Color
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import one.mann.domain.models.Errors.NoInternet
import one.mann.domain.models.Errors.NoResponse
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
    private val position: Int by lazy { intent.getIntExtra(PAGER_POSITION, 1) }
    private val backgroundResource: Int by lazy { intent.getIntExtra(ACTIVITY_BACKGROUND, getGradient()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.root.setBackgroundResource(backgroundResource)
        handleLocationPermission { initActivity(it) }
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
            // Set up RecyclerView
            detailRecyclerView.apply {
                setHasFixedSize(true)
                adapter = detailRecyclerAdapter
            }
            // Set up the Swipe Refresh Layout
            detailSwipeLayout.apply {
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
        if (model.viewState is DetailUiModel.State.Error) when (model.viewState.errorType) {
            NoInternet -> toast(R.string.no_internet_connection)
            is NoResponse -> toast(R.string.network_error, NoResponse().message)
            else -> run { return@run }
        }
        if (weatherData.size >= position + 1) {
            detailRecyclerAdapter.update(weatherData[position])
            // Update activity background only if it changes after a data refresh
            val newBackground = getGradient(weatherData[position].sunPosition, isOvercast(weatherData[position].iconId))
            if (newBackground != backgroundResource) binding.root.setBackgroundResource(newBackground)
        }
    }
}