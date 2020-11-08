package one.mann.weatherman.ui.main.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import one.mann.domain.model.CitySearch
import one.mann.weatherman.R
import one.mann.weatherman.databinding.ItemCitySearchBinding
import one.mann.weatherman.ui.common.util.inflateView

/* Created by Psmann. */

internal class SearchCityRecyclerAdapter(val onClick: () -> Unit)
    : RecyclerView.Adapter<SearchCityRecyclerAdapter.CitySearchViewHolder>() {

    private var citySearchList = listOf<CitySearch>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitySearchViewHolder {
        return CitySearchViewHolder(parent.inflateView(R.layout.item_city_search))
    }

    override fun onBindViewHolder(holder: CitySearchViewHolder, position: Int) {
        holder.binding.apply {
            cityResult1TextView.text = citySearchList[position].cityLine1
            cityResult2TextView.text = citySearchList[position].cityLine2
            root.setOnClickListener { onClick() }
        }
    }

    override fun getItemCount(): Int = citySearchList.size

    fun update(searchList: List<CitySearch>) {
        citySearchList = searchList
        notifyDataSetChanged()
    }

    class CitySearchViewHolder(
            itemView: View,
            val binding: ItemCitySearchBinding = ItemCitySearchBinding.bind(itemView)
    ) : RecyclerView.ViewHolder(itemView)
}