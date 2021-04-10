package com.android.fundamentals.workshop04

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.academy.fundamentals.R
import com.android.fundamentals.data.models.Actor
import com.android.fundamentals.domain.ActorsDataSource
import kotlinx.android.synthetic.main.fragment_actors.rv_actors
import kotlinx.android.synthetic.main.fragment_workshop_04.*

class WS04DiffUtilsFragment : Fragment() {

    private lateinit var adapter: WS04ActorsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_workshop_04, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = WS04ActorsAdapter()
        rv_actors.layoutManager = LinearLayoutManager(requireContext())
        rv_actors.adapter = adapter
        shuffle_button.setOnClickListener {
            shuffleActors()
        }
    }

    override fun onStart() {
        super.onStart()
        updateData()
    }

    private fun updateData() {
        adapter.bindActors(ActorsDataSource().getActors())
        adapter.notifyDataSetChanged()
    }

    private fun shuffleActors() {
        val originalList : List<Actor> = ActorsDataSource().getActors()
        val shuffledList: List<Actor> = ActorsDataSource().getActors().shuffled()
        adapter.bindActors(shuffledList)
        // TODO: Replace notifyDataSetChanged for updating the recyclerView to DiffUtil.Callback.
        val callback = ActorDiffUtilCallback(originalList, shuffledList)
        val result : DiffUtil.DiffResult = DiffUtil.calculateDiff(callback)
        result.dispatchUpdatesTo(adapter)
    }

    companion object {
        fun newInstance() = WS04DiffUtilsFragment()
    }
}