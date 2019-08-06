package one.mann.weatherman.ui.detail

import android.graphics.Color
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_detail.*
import one.mann.domain.model.LocationResponse.*
import one.mann.domain.model.LocationType.DB
import one.mann.domain.model.LocationType.DEVICE
import one.mann.weatherman.R
import one.mann.weatherman.application.WeatherManApp
import one.mann.weatherman.ui.common.base.BaseActivity
import one.mann.weatherman.ui.common.util.PAGER_POSITION
import one.mann.weatherman.ui.common.util.getViewModel
import one.mann.weatherman.ui.detail.adapter.DetailRecyclerAdapter
import javax.inject.Inject

internal class DetailActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val detailViewModel: DetailViewModel by lazy { getViewModel(viewModelFactory) }
    private val detailRecyclerAdapter by lazy { DetailRecyclerAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        handleLocationPermission { initActivity(it) }
    }

    override fun injectDependencies() = WeatherManApp.appComponent.getSubComponent().injectDetailActivity(this)

    private fun initActivity(permissionGranted: Boolean) {
        if (!permissionGranted) { // If permission denied then exit
            toast(R.string.permission_required)
            finish()
            return
        }
        detail_recyclerview.setHasFixedSize(true)
        detail_recyclerview.layoutManager = LinearLayoutManager(this)
        detail_recyclerview.adapter = detailRecyclerAdapter
        // Init ViewModel
        detailViewModel.uiModel.observe(this, Observer {
            when (it) {
                is DetailViewModel.UiModel.Refreshing -> detail_swipe_ly.isRefreshing = it.loading
                is DetailViewModel.UiModel.ShowError -> toast(R.string.error_has_occurred_try_again)
            }
        })
        intent.getIntExtra(PAGER_POSITION, 1)
        //        mainViewModel.weatherData.observe(this, Observer { if (it.size >= position + 1) setupViews(it[position]) })
        // detailRecyclerAdapter.update(it[position])
        // .setBackgroundResource(getGradient(weather.sunPosition, isOvercast(weather.iconId)))
        detail_swipe_ly.setColorSchemeColors(Color.RED, Color.BLUE)
        detail_swipe_ly.setOnRefreshListener { handleLocationServiceResult() }
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
}