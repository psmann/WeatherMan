package one.mann.weatherman.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import one.mann.domain.model.weather.Weather
import one.mann.weatherman.WeatherManApp
import one.mann.weatherman.api.openweathermap.isOvercast
import one.mann.weatherman.databinding.FragmentCityBinding
import one.mann.weatherman.ui.common.util.*
import one.mann.weatherman.ui.detail.DetailActivity
import javax.inject.Inject

internal class CityFragment : Fragment() {

    private var position = 0
    private var backgroundResources = 0
    private lateinit var binding: FragmentCityBinding
    private val detailIntent: Intent by lazy { Intent(context, DetailActivity::class.java) }
    private val mainViewModel: MainViewModel by lazy { activity!!.getViewModel(viewModelFactory) } // No nice way to handle !!
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    companion object {
        @JvmStatic
        fun newInstance(position: Int) = CityFragment().apply {
            arguments = Bundle().apply { putInt(PAGER_POSITION, position) }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getInt(PAGER_POSITION)?.let { position = it }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCityBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        injectDependencies()
        binding.cityDetailButton.setOnClickListener { startActivity(detailIntent) }
        mainViewModel.uiState.observe(viewLifecycleOwner, ::observeUiState)
    }

    private fun injectDependencies() = WeatherManApp.appComponent.getSubComponent().injectMainFragment(this)

    private fun observeUiState(state: MainViewState) {
        val weatherData = state.weatherData
        binding.root.visibility = if (state.isLoading) View.GONE else View.VISIBLE // Layout is hidden until data is loaded
        if (weatherData.size >= position + 1) setupViews(weatherData[position])
    }

    private fun setupViews(weather: Weather) {
        val newBackground = getGradient(weather.sunPosition, isOvercast(weather.iconId))
        binding.cityWeatherIconImageView.loadIcon(weather.iconId, weather.sunPosition)
        binding.cityCurrentTempTextView.text = weather.currentTemp
        binding.cityTimeUpdatedTextView.text = weather.lastChecked
        binding.cityNameTextView.text = weather.cityName
        if (backgroundResources != newBackground) { // Update layout background only if it changes after a data refresh
            binding.root.setBackgroundResource(newBackground)
            backgroundResources = newBackground
        }
        setupIntent(newBackground)
    }

    private fun setupIntent(backgroundId: Int) {
        detailIntent.putExtra(PAGER_POSITION, position) // City to show details for
        detailIntent.putExtra(ACTIVITY_BACKGROUND, backgroundId) // Background to be used in detail activity
    }
}