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
import one.mann.weatherman.R
import one.mann.weatherman.application.WeatherManApp
import one.mann.weatherman.ui.common.base.ViewModelFactory
import one.mann.weatherman.ui.common.util.getViewModel
import one.mann.weatherman.ui.main.adapter.MainRecyclerAdapter
import javax.inject.Inject

internal class MainFragment : Fragment() {

    private var position = 0
    private val mainViewModel: MainViewModel by lazy { activity?.run { getViewModel(viewModelFactory) }!! }
    private val mainRecyclerAdapter by lazy { MainRecyclerAdapter() }
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

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
            inflater.inflate(R.layout.fragment_weather, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        injectDependencies()
        city_recyclerview.setHasFixedSize(true)
        city_recyclerview.layoutManager = LinearLayoutManager(context)
        city_recyclerview.adapter = mainRecyclerAdapter
        mainViewModel.displayUI.observe(this, Observer {
            if (it) city_recyclerview.visibility = View.VISIBLE
            else city_recyclerview.visibility = View.GONE
        })
        mainViewModel.weatherData.observe(this, Observer {
            if (it.size >= position + 1) mainRecyclerAdapter.update(it[position])
        })
    }

    private fun injectDependencies() = WeatherManApp.appComponent.getMainComponent().injectFragment(this)
}