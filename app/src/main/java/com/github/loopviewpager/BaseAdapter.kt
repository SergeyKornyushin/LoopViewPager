package com.github.loopviewpager

import android.view.LayoutInflater
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

/**
 * Базовый Adapter
 */
abstract class BaseAdapter<ItemClass, VH : BaseViewHolder>(
    protected val layoutInflater: LayoutInflater,
) : RecyclerView.Adapter<VH>() {

    protected var items: MutableList<ItemClass> = mutableListOf()

    override fun getItemCount() = items.size

    fun updateItems(items: List<ItemClass>) {
        this.items = items.toMutableList()
        notifyDataSetChanged()
    }

    fun addItems(items: List<ItemClass>) {
        val startPosition = this.items.size
        this.items.addAll(items)
        notifyItemRangeInserted(startPosition, items.size)
    }

    /**
     * Выкинуть исплючение для несуществующего типа ViewHolder
     */
    protected fun throwUnknownViewHolderTypeException(): Nothing {
        throw IllegalArgumentException("Unknown ViewHolder Type!")
    }

    protected fun updateItem(item: ItemClass, position: Int, type: Int) {
        val startPosition: Int = items.size

        if (items.size <= position) {
            items.add(item)
            notifyItemInserted(startPosition)
            return
        }

        val typeForPosition = getItemViewType(position)

        if (type == typeForPosition) {
            items[position] = item
            notifyItemChanged(position, item)
            return
        }

        items.add(position, item)
        notifyItemInserted(position)
    }

    protected fun removeItemsByTypes(vararg types: Int) {
        items = (items.filterIndexed { index, _ ->
            val type = getItemViewType(index)
            !types.contains(type)
        }).toMutableList()

        notifyDataSetChanged()
    }

    protected fun getItem(position: Int) = items[position]

    protected fun updateItemsWithDiffUtil(
        items: List<ItemClass>,
        diffResult: DiffUtil.DiffResult
    ) {
        this.items = items.toMutableList()
        diffResult.dispatchUpdatesTo(this)
    }

    protected companion object {
        const val NOT_FOUND = -1
    }
}