package one.mann.weatherman.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_weather.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import one.mann.weatherman.R
import one.mann.weatherman.ui.main.adapter.CityRecyclerAdapter

internal class MainFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private var position = 1
    private val cityRecyclerAdapter: CityRecyclerAdapter = CityRecyclerAdapter()

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
        city_recyclerview.visibility = View.GONE
        city_recyclerview.setHasFixedSize(true)
        city_recyclerview.layoutManager = LinearLayoutManager(context)
        city_recyclerview.adapter = cityRecyclerAdapter

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        mainViewModel.weatherLiveData.observe(this,
                Observer { weatherSharedPref ->
                    cityRecyclerAdapter.bindData(weatherSharedPref!!, position)
                })
        mainViewModel.displayUi.observe(this, Observer { aBoolean ->
            if (aBoolean!! && city_recyclerview.visibility == View.GONE)
                GlobalScope.launch(Dispatchers.Main) {
                    delay(10L)
                    city_recyclerview.visibility = View.VISIBLE
                }
        })
    }
}