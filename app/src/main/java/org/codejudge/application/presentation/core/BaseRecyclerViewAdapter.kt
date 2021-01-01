package org.codejudge.application.presentation.core

import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import kotlin.collections.ArrayList

abstract class BaseRecyclerViewAdapter<T>(
    private var mList: ArrayList<T> = arrayListOf()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var mListFiltered: ArrayList<T> = arrayListOf()

    init {
        mListFiltered = arrayListOf()
        mListFiltered.addAll(mList)
    }

    @NonNull
    override fun onCreateViewHolder(
        @NonNull parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return getViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(@NonNull holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.adapterPosition >= mList.size) {
            bindData(holder, null, position)
            return
        }
        val item = mList.get(position)
        bindData(holder, item, position)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    protected fun onItemClick(position: Int, data: Any?) {}

    abstract fun getViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    abstract fun bindData(holder: RecyclerView.ViewHolder?, item: T?, position: Int)

    fun clear() {
        val oldSize = mList.size
        mList.clear()
        assignToFilteredList()
        notifyItemRangeRemoved(0, oldSize)
    }

    fun setData(list: List<T>) {
        val oldSize = this.mList.size
        mList.clear()
        notifyItemRangeRemoved(0, oldSize)
        this.mList.addAll(list)
        assignToFilteredList()
        notifyItemRangeInserted(0, mList.size)
    }

    fun setFilteredData(list: List<T>) {
        val oldSize = this.mList.size
        mList.clear()
        notifyItemRangeRemoved(0, oldSize)
        this.mList.addAll(list)
        notifyItemRangeInserted(0, mList.size)
    }

    fun addData(list: List<T>) {
        val oldSize = mList.size
        mList.addAll(list)
        assignToFilteredList()
        notifyItemRangeInserted(oldSize, mList.size)
    }

    fun updateItem(position: Int, item: T) {
        mList[position] = item
        notifyItemChanged(position)
    }

    fun removeItem(position: Int) {
        mList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun addItem(item: T) {
        val position = mList.size
        mList.add(item)
        notifyItemInserted(position)
    }

    fun getItem(position: Int): T {
        return mList.get(position)
    }

    private fun assignToFilteredList() {
        mListFiltered = arrayListOf()
        mListFiltered.addAll(mList)
    }
}