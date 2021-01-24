package org.android.application.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.android.application.data.models.CityItem
import org.android.application.databinding.CellCityItemBinding
import org.android.application.presentation.core.BaseRecyclerViewAdapter

class CityItemListAdapter(
    val onClick: (cityItem: CityItem) -> Unit,
    val onBookmarkClick: (cityItem: CityItem) -> Unit,
) : BaseRecyclerViewAdapter<CityItem>() {

    override fun getViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            CellCityItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun bindData(holder: RecyclerView.ViewHolder?, item: CityItem?, position: Int) {
        if (holder != null && item != null) {
            val viewHolder = holder as ViewHolder
            viewHolder.bindData(item)
        }
    }

    inner class ViewHolder(private val itemBinding: CellCityItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        init {
            itemBinding.tvName.setOnClickListener {
                onClick(getItem(adapterPosition))
            }

            itemBinding.ivBookmark.setOnClickListener {
                onBookmarkClick(getItem(adapterPosition))
            }
        }

        fun bindData(item: CityItem) {
            itemBinding.tvName.text = item.name.orEmpty()
            itemBinding.ivBookmark.isSelected = true
        }
    }
}