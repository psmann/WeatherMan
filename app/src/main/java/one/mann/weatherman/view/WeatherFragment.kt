package one.mann.weatherman.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_weather.*

import one.mann.weatherman.R
import one.mann.weatherman.view.adapter.CityRecyclerAdapter
import one.mann.weatherman.viewmodel.WeatherViewModel

class WeatherFragment : Fragment() {

    private lateinit var weatherViewModel: WeatherViewModel
    private var position = 1
    private val cityRecyclerAdapter: CityRecyclerAdapter = CityRecyclerAdapter()

    companion object {
        private const val POSITION = "POSITION"
        @JvmStatic
        fun newInstance(position: Int) = WeatherFragment().apply {
            arguments = Bundle().apply { putInt(POSITION, position) }
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        arguments?.getInt(POSITION)?.let { position = it }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        city_recyclerview.visibility = View.INVISIBLE
        city_recyclerview.layoutManager = LinearLayoutManager(context)
        city_recyclerview.adapter = cityRecyclerAdapter
        weatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel::class.java)
        weatherViewModel.weatherLiveData.observe(this,
                Observer { weatherData -> cityRecyclerAdapter.bindData(weatherData!!, position) })
        weatherViewModel.displayUi.observe(this, Observer { aBoolean ->
            if (aBoolean!! && city_recyclerview.visibility == View.INVISIBLE)
                city_recyclerview.visibility = View.VISIBLE
        })
    }
}