package one.mann.weatherman.ui.detail

import android.graphics.Color
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import one.mann.domain.model.Errors
import one.mann.weatherman.R
import one.mann.weatherman.WeatherManApp
import one.mann.weatherman.api.openweathermap.isOvercast
import one.mann.weatherman.databinding.ActivityDetailBinding
import one.mann.weatherman.ui.common.base.BaseLocationActivity
import one.mann.weatherman.ui.common.util.ACTIVITY_BACKGROUND
import one.mann.weatherman.ui.common.util.PAGER_POSITION
import one.mann.weatherman.ui.common.util.getGradient
import one.mann.weatherman.ui.common.util.getViewModel
import one.mann.weatherman.ui.detail.adapter.DetailRecyclerAdapter
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
        binding.detailRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@DetailActivity)
            adapter = detailRecyclerAdapter
        }
        binding.detailSwipeLayout.apply {
            setColorSchemeColors(Color.RED, Color.BLUE)
            setOnRefreshListener { handleLocationServiceResult() }
        }
        detailViewModel.uiState.observe(::getLifecycle, ::observeUiState)
    }

    private fun handleLocationServiceResult() = handleLocationPermission { permissionGranted ->
        if (permissionGranted) checkLocationService { detailViewModel.handleRefreshing(it) }
    }

    private fun observeUiState(state: DetailViewState) {
        val weather = state.weatherData
        binding.detailSwipeLayout.isRefreshing = state.isRefreshing
        when (state.error) {
            Errors.NO_INTERNET -> toast(R.string.no_internet_connection)
            Errors.NO_RESPONSE -> toast(R.string.error_has_occurred_try_again)
            else -> run { return@run } // Workaround for lack of break support inside when statements
        }
        if (weather.size >= position + 1) {
            detailRecyclerAdapter.update(weather[position])
            // Update activity background only if it changes after a data refresh
            val newBackground = getGradient(weather[position].sunPosition, isOvercast(weather[position].iconId))
            if (newBackground != backgroundResource) binding.root.setBackgroundResource(newBackground)
        }
    }
}