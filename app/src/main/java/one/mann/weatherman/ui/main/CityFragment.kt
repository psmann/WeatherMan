package one.mann.weatherman.ui.main

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import one.mann.weatherman.WeatherManApp
import one.mann.weatherman.api.openweathermap.isOvercast
import one.mann.weatherman.common.ACTIVITY_BACKGROUND
import one.mann.weatherman.common.DETAIL_BUTTON_CLICKED
import one.mann.weatherman.common.PAGER_POSITION
import one.mann.weatherman.databinding.FragmentCityBinding
import one.mann.weatherman.ui.common.models.Weather
import one.mann.weatherman.ui.common.util.getGradient
import one.mann.weatherman.ui.common.util.getViewModel
import one.mann.weatherman.ui.common.util.loadIcon
import one.mann.weatherman.ui.detail.DetailActivity
import one.mann.weatherman.ui.main.MainUiModel.State.Loading
import javax.inject.Inject

/* Created by Psmann. */

internal class CityFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var position = 0
    private var backgroundResources = 0
    private var detailButtonClicked = false
    private var _binding: FragmentCityBinding? = null
    private val binding: FragmentCityBinding get() = _binding!!
    private val detailIntent: Intent by lazy { Intent(context, DetailActivity::class.java) }
    private val mainViewModel: MainViewModel by lazy { requireActivity().getViewModel(viewModelFactory) }
    private val startActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        // Reset detailButtonClicked after returning back to fragment
        if (it.resultCode == RESULT_OK) detailButtonClicked = false
    }

    companion object {
        @JvmStatic
        fun newInstance(position: Int) = CityFragment().apply {
            arguments = Bundle().apply { putInt(PAGER_POSITION, position) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            detailButtonClicked = it.getBoolean(DETAIL_BUTTON_CLICKED)
            if (detailButtonClicked) {
                position = it.getInt(PAGER_POSITION)
                setupIntent(it.getInt(ACTIVITY_BACKGROUND))
                startActivityResult.launch(detailIntent)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getInt(PAGER_POSITION)?.let { position = it }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        injectDependencies()
        binding.cityDetailButton.setOnClickListener {
            detailButtonClicked = true
            startActivityResult.launch(detailIntent)
        }
        mainViewModel.uiModel.observe(viewLifecycleOwner, ::observeUiModel)
    }

    /** Save ViewPager position and button click event state */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(DETAIL_BUTTON_CLICKED, detailButtonClicked)
        outState.putInt(PAGER_POSITION, position)
        outState.putInt(ACTIVITY_BACKGROUND, detailIntent.getIntExtra(ACTIVITY_BACKGROUND, getGradient()))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun injectDependencies() = WeatherManApp.appComponent.getSubComponent().injectMainFragment(this)

    private fun observeUiModel(model: MainUiModel) {
        val weatherData = model.weatherData
        // Hide layout until data is loaded
        binding.root.visibility = if (model.viewState is Loading) View.GONE else View.VISIBLE
        if (weatherData.size >= position + 1) setupViews(weatherData[position])
    }

    private fun setupViews(weather: Weather) {
        val newBackground = getGradient(weather.currentWeather.sunPosition, isOvercast(weather.currentWeather.iconId))
        binding.cityWeatherIconImageView.loadIcon(weather.currentWeather.iconId, weather.currentWeather.sunPosition)
        binding.cityCurrentTempTextView.text = weather.currentWeather.currentTemperature
        binding.cityTimeUpdatedTextView.text = weather.currentWeather.lastChecked
        binding.cityNameTextView.text = weather.city.cityName
        // Update layout background only if it changes after a data refresh
        if (backgroundResources != newBackground) {
            binding.root.setBackgroundResource(newBackground)
            backgroundResources = newBackground
        }
        setupIntent(newBackground)
    }

    private fun setupIntent(backgroundId: Int) {
        // City to show details for
        detailIntent.putExtra(PAGER_POSITION, position)
        // Background to be used in detail activity
        detailIntent.putExtra(ACTIVITY_BACKGROUND, backgroundId)
    }
}