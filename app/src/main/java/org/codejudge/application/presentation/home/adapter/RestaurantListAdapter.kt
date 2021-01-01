package org.codejudge.application.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.codejudge.application.R
import org.codejudge.application.data.models.Restaurant
import org.codejudge.application.databinding.CellRestaurantItemBinding
import org.codejudge.application.presentation.core.BaseRecyclerViewAdapter
import org.codejudge.application.presentation.utility.AppConstant
import org.codejudge.application.presentation.utility.getRestaurantPhotoUrl
import org.codejudge.application.presentation.utility.loadImageRound

class RestaurantListAdapter(val onClick: (restaurant: Restaurant) -> Unit) :
    BaseRecyclerViewAdapter<Restaurant>() {

    override fun getViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            CellRestaurantItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun bindData(holder: RecyclerView.ViewHolder?, item: Restaurant?, position: Int) {
        if (holder != null && item != null) {
            val viewHolder = holder as ViewHolder
            viewHolder.bindData(item)
        }
    }

    inner class ViewHolder(private val itemBinding: CellRestaurantItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        init {
            itemBinding.ivImage.setOnClickListener {
                onClick(getItem(adapterPosition))
            }
        }

        fun bindData(item: Restaurant) {
            //set or bind data
            if (item.photos?.isNotEmpty() == true) {
                itemBinding.ivImage.loadImageRound(
                    itemBinding.ivImage.context.getRestaurantPhotoUrl(item.photos?.get(0)?.photoReference.orEmpty()),
                    R.drawable.icn_placeholder_square
                )
            }
            if (item.types?.isNotEmpty() == true) {
                itemBinding.tvRestaurantType.text = item.types?.get(0)?.orEmpty()
            }
            if (item.types?.isNotEmpty() == true) {
                itemBinding.tvFoodTypes.text = item.getRestaurantFoodTypes()
            }

            itemBinding.tvCloseIn.isSelected = item.openingHours?.openNow ?: false
            if (item.openingHours?.openNow == true) {
                itemBinding.tvCloseIn.text =
                    itemBinding.tvCloseIn.context.getString(R.string.restaurant_open)
            } else {
                itemBinding.tvCloseIn.text =
                    itemBinding.tvCloseIn.context.getString(R.string.restaurant_closed)
            }

            itemBinding.tvRestaurantName.text = item.name.orEmpty()
            itemBinding.tvRestaurantRating.text = item.rating.toString()
            itemBinding.tvPrice.text = String.format(
                itemBinding.tvPrice.context.getString(R.string.price_rest),
                AppConstant.getPriceRangeFromLevel(item.priceLevel ?: 0)
            )

        }
    }
}