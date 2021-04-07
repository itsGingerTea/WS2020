package com.android.fundamentals.workshop04

import androidx.recyclerview.widget.DiffUtil
import com.android.fundamentals.data.models.Actor

class ActorDiffUtilCallback(
    private val oldList : List<Actor>,
    private val newList : List<Actor>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
       return oldList[oldItemPosition].name == newList[newItemPosition].name
    }

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}