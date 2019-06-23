package one.mann.weatherman.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_weather.*
import one.mann.interactors.data.repository.WeatherRepository
import one.mann.interactors.usecase.*
import one.mann.weatherman.R
import one.mann.weatherman.api.openweathermap.OwmDataSource
import one.mann.weatherman.api.teleport.TeleportDataSource
import one.mann.weatherman.framework.data.database.DbDataSource
import one.mann.weatherman.framework.data.location.LocationDataSource
import one.mann.weatherman.ui.common.util.app
import one.mann.weatherman.ui.common.util.getViewModel
import one.mann.weatherman.ui.main.adapter.MainRecyclerAdapter

internal class MainFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private var position = 0
    private lateinit var mainRecyclerAdapter: MainRecyclerAdapter

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainRecyclerAdapter = MainRecyclerAdapter()
        city_recyclerview.setHasFixedSize(true)
        city_recyclerview.layoutManager = LinearLayoutManager(context)
        city_recyclerview.adapter = mainRecyclerAdapter

        // Use the same mainViewModel instance from activity
        mainViewModel = activity?.run {
            getViewModel {
                val weatherRepository = WeatherRepository(OwmDataSource(), TeleportDataSource(),
                        LocationDataSource(app), DbDataSource(app.db))
                MainViewModel(
                        AddCity(weatherRepository),
                        GetAllWeather(weatherRepository),
                        RemoveCity(weatherRepository),
                        UpdateWeather(weatherRepository),
                        GetCityCount(weatherRepository)
                )
            }
        }!!
        mainViewModel.displayUI.observe(this, Observer {
            if (it) city_recyclerview.visibility = View.VISIBLE
            else city_recyclerview.visibility = View.GONE
        })
        mainViewModel.weatherData.observe(this, Observer {
            if (it.size >= position + 1) {
                if (::mainRecyclerAdapter.isInitialized)
                    mainRecyclerAdapter.putData(it[position])
            }
        })
    }
}