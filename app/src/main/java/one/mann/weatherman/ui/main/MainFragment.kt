package one.mann.weatherman.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_main.*
import one.mann.domain.model.Weather
import one.mann.weatherman.R
import one.mann.weatherman.api.openweathermap.isOvercast
import one.mann.weatherman.application.WeatherManApp
import one.mann.weatherman.ui.common.util.PAGER_POSITION
import one.mann.weatherman.ui.common.util.getGradient
import one.mann.weatherman.ui.common.util.getViewModel
import one.mann.weatherman.ui.common.util.loadIcon
import one.mann.weatherman.ui.detail.DetailActivity
import one.mann.weatherman.ui.main.MainViewModel.UiModel
import javax.inject.Inject

internal class MainFragment : Fragment() {

    private var position = 0
    private val mainViewModel: MainViewModel by lazy { activity?.run { getViewModel(viewModelFactory) }!! }
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    companion object {
        private const val POSITION = "POSITION"
        @JvmStatic
        fun newInstance(position: Int) = MainFragment().apply {
            arguments = Bundle().apply { putInt(POSITION, position) }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getInt(POSITION)?.let { position = it }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_main, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        injectDependencies()
        button_detail.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(PAGER_POSITION, position)
            startActivity(intent)
        }
        // Init ViewModel
        mainViewModel.uiModel.observe(this, Observer {
            if (it is UiModel.DisplayUi) fragment_main_const_ly.visibility = if (it.display) View.VISIBLE else View.GONE
        })
        mainViewModel.weatherData.observe(this, Observer { if (it.size >= position + 1) setupViews(it[position]) })
    }

    private fun injectDependencies() = WeatherManApp.appComponent.getSubComponent().injectMainFragment(this)

    private fun setupViews(weather: Weather) {
        weather_icon.loadIcon(weather.iconId, weather.sunPosition)
        current_temp.text = weather.currentTemp
        time_updated.text = weather.lastChecked
        city_name.text = weather.cityName
        fragment_main_const_ly.setBackgroundResource(getGradient(weather.sunPosition, isOvercast(weather.iconId)))
    }
}