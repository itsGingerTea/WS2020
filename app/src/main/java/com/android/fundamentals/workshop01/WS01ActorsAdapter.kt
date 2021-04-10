package com.android.fundamentals.workshop01

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.academy.fundamentals.R
import com.android.fundamentals.data.models.Actor

class WS01ActorsAdapter : RecyclerView.Adapter<EmptyViewHolder>() {

    private var actors = listOf<Actor>()

    fun bindActors(newActors: List<Actor>) {
        actors = newActors
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmptyViewHolder {
        return EmptyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_actors_empty,parent, false))
    }

    override fun getItemCount(): Int = 1

    override fun onBindViewHolder(holder: EmptyViewHolder, position: Int) {
        Toast.makeText(holder.itemView.context, "We are inside a method onBindViewHolder", Toast.LENGTH_SHORT).show()
    }

}

class EmptyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
