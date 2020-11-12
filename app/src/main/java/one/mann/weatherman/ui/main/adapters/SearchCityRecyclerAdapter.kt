package one.mann.weatherman.ui.main.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import one.mann.domain.model.CitySearchResult
import one.mann.weatherman.R
import one.mann.weatherman.databinding.ItemCitySearchBinding
import one.mann.weatherman.ui.common.util.inflateView

/* Created by Psmann. */

internal class SearchCityRecyclerAdapter(val onClick: (searchResult: CitySearchResult) -> Unit)
    : RecyclerView.Adapter<SearchCityRecyclerAdapter.CitySearchViewHolder>() {

    private var citySearchList = listOf<CitySearchResult>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitySearchViewHolder {
        return CitySearchViewHolder(parent.inflateView(R.layout.item_city_search))
    }

    override fun onBindViewHolder(holder: CitySearchViewHolder, position: Int) {
        holder.binding.apply {
            cityResult1TextView.text = citySearchList[position].cityLine1
            cityResult2TextView.text = citySearchList[position].cityLine2
            countryFlagTextView.text = citySearchList[position].countryFlag
            root.setOnClickListener { onClick(citySearchList[position]) }
        }
    }

    override fun getItemCount(): Int = citySearchList.size

    fun update(searchList: List<CitySearchResult> = listOf()) {
        citySearchList = searchList
        notifyDataSetChanged()
    }

    class CitySearchViewHolder(
            itemView: View,
            val binding: ItemCitySearchBinding = ItemCitySearchBinding.bind(itemView)
    ) : RecyclerView.ViewHolder(itemView)
}