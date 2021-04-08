@file:Suppress("unused")

package com.android.academy.fundamentals.workshop_3

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.android.fundamentals.R
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main

class WS03CoroutinesProblemFragment : Fragment(R.layout.fragment_ws_03) {
    // This is exception handler that will print caught errors to log
    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, exception ->
        println("CoroutineExceptionHandler got $exception in $coroutineContext")
    }

    // This coroutines scope we run
    // it is variable, because Job can not be restarted after cancellation and
    // we have to create new one
    private var scope = CoroutineScope(
        SupervisorJob() +
                Dispatchers.Default +
                exceptionHandler
    )

    // This is job that will be run in Global Scope it
    private var globalScopeJob: Job? = null

    private var startButton: Button? = null
    private var stopButton: Button? = null
    private var firstCoroutineResultView: TextView? = null
    private var secondCoroutineResultView: TextView? = null
    private var thirdCoroutineResultView: TextView? = null
    private var fourthCoroutineResultView: TextView? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        cacheViews(view)

        startButton?.setOnClickListener {
            startCoroutines()
        }

        stopButton?.setOnClickListener {
            cancelCoroutines()
        }
    }

    private fun startCoroutines() {
        toggleButtons(true)

        scope.launch { runOddsCoroutine() }

        //scope.launch { runNegativesCoroutine() }

        scope.launch { runModByTwoCoroutine() }

        globalScopeJob = GlobalScope.launch { runModByTwoCoroutine() }

        scope.launch { runCoroutineThatFails() }
    }

    // Coroutine that produces odd numbers every second
    private suspend fun runOddsCoroutine() {
        var count = 1
        while (true) {
            count += 2
            showResult(count.toString(), firstCoroutineResultView)
            delay(1_000)
        }
    }

    // Coroutine that produces next negative numbers every 1.5 second
    private suspend fun runNegativesCoroutine() {
        var count = 0
        while (true) {
            count--
            showResult(count.toString(), secondCoroutineResultView)
            delay(1_500)
        }
    }

    // Coroutine that produces next mod by 2 result every half second
    private suspend fun runModByTwoCoroutine() {
        var count = 1
        while (count < 100) {
            count++
            val modByTwo = count % 2
            showResult(modByTwo.toString(), thirdCoroutineResultView)
            delay(500)
        }
        showResult(getString(R.string.finally_done), thirdCoroutineResultView)
    }

    // Coroutine that fails after one second
    private suspend fun runCoroutineThatFails() {
        delay(1_000)
        throw IllegalStateException("Some exception")
    }

    private suspend fun showResult(text: String, resultView: TextView?) {
        withContext(Main) {
            resultView?.text = text
        }
    }

    private fun cancelCoroutines() {

        scope.cancel()

        // Set new scope with fresh SupervisorJob after cancel
        scope = CoroutineScope(SupervisorJob() + Dispatchers.Default + exceptionHandler)

        firstCoroutineResultView?.text = getString(R.string.done)
        secondCoroutineResultView?.text = getString(R.string.done)
        thirdCoroutineResultView?.text = getString(R.string.done)
        fourthCoroutineResultView?.text = getString(R.string.done)

        toggleButtons(false)
    }

    private fun cacheViews(view: View) {
        startButton = view.findViewById(R.id.start_button)
        stopButton = view.findViewById(R.id.stop_button)
        firstCoroutineResultView = view.findViewById(R.id.first_coroutine_value)
        secondCoroutineResultView = view.findViewById(R.id.second_coroutine_value)
        thirdCoroutineResultView = view.findViewById(R.id.third_coroutine_value)
        fourthCoroutineResultView = view.findViewById(R.id.fourth_coroutine_value)
    }

    private fun toggleButtons(start: Boolean) {
        startButton?.isEnabled = !start
        stopButton?.isEnabled = start
    }

    override fun onDestroyView() {
        globalScopeJob?.cancel()

        cancelCoroutines()

        // Clear views to avoid leaks
        clearCachedViews()

        super.onDestroyView()
    }

    private fun clearCachedViews() {
        startButton = null
        stopButton = null
        firstCoroutineResultView = null
        secondCoroutineResultView = null
        thirdCoroutineResultView = null
        fourthCoroutineResultView = null
    }

    companion object {
        // TODO: WS_5_03_12 - How can I keep jobs simple while activity is rotating?
    }
}