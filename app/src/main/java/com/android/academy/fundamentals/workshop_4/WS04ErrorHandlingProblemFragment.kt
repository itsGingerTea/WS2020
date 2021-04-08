package com.android.academy.fundamentals.workshop_4

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.android.fundamentals.R
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default
import java.io.IOException

class WS04ErrorHandlingProblemFragment : Fragment(R.layout.fragment_ws_04) {

    private var fallByLaunchButton: Button? = null
    private var fallByAwaitButton: Button? = null
    private var handleWithTryCatchButton: Button? = null
    private var handleAwaitWithTryCatchButton: Button? = null
    private var handleWithHandler: Button? = null
    private var handleWithSuperHandler: Button? = null
    private var exceptionLogView: TextView? = null

    private val exceptionHandler = CoroutineExceptionHandler { canceledContext, exception ->
        val isActive = coroutineScope.isActive
        Log.e(TAG, "ExceptionHandler [Scope active:$isActive, canceledContext:$canceledContext]")
        coroutineScope = createScope().apply {
            launch {
                sb.append("exceptionHandler scope active:$isActive\n")
                logExceptionSuspend("exceptionHandler", exception)
            }
        }
    }

    private val superExceptionHandler = CoroutineExceptionHandler { canceledContext, exception ->
        val isActive = coroutineSupervisorScope.isActive
        Log.e(TAG, "SuperExceptionHandler [Scope active:$isActive, canceledContext:$canceledContext]")
        coroutineSupervisorScope.launch {
            sb.append("superExceptionHandler scope active:$isActive\n")
            logExceptionSuspend("superExceptionHandler", exception)
        }
    }

    private fun createScope(): CoroutineScope = CoroutineScope(Default + Job())

    private fun createSuperScope(): CoroutineScope = CoroutineScope(Default + Job())

    private var coroutineScope = createScope()
    private var coroutineSupervisorScope = createSuperScope()
    private val sb: StringBuilder = StringBuilder()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews(view)
        setupListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        cancelCoroutines()
        clearViews()
    }

    // TODO: UI thread logger SAMPLE.
    private fun loggerSample() {
        try {
            coroutineScope.launch {  }

        } catch (throwable: Throwable) {
            logException("loggerSample", throwable)
        }
    }

    // TODO: Coroutine-To-UI thread logger SAMPLE.
    private fun suspendLoggerSample() {
        coroutineScope.launch {
            try {

            } catch (throwable: Throwable) {
                logExceptionSuspend("suspendLoggerSample", throwable)
            }
        }
    }

    private fun failLaunchWithException() {
        try {
            coroutineScope.launch {
                methodWithException("failLaunchWithException", coroutineScope.isActive)
            }
        }
        catch (throwable : Throwable) {
            logException("failLaunchWithException", throwable)
        }
    }

    private fun workWithHandledException() {
        try {
            coroutineScope.launch {
                methodWithException("workWithHandledException", coroutineScope.isActive)
            }
        }
        catch (throwable : Throwable) {
            logException("workWithHandledException", throwable)
        }
    }

    private fun failAwaitWithException() {
        methodWithException("failAwaitWithException", coroutineScope.isActive)
    }

    private fun awaitWorkWithHandledException() {
        methodWithException("awaitWorkWithHandledException", coroutineScope.isActive)
    }

    private fun workWithExceptionHandler() {
        methodWithException("workWithExceptionHandler", coroutineScope.isActive)
    }

    private fun superWorkWithExceptionHandler() {
        methodWithException("superWorkWithExceptionHandler", coroutineScope.isActive)
    }

    private fun methodWithException(who: String, isActive: Boolean) {
        sb.clear().append("scope isActive:$isActive")
                .append("\n")
                .append("$who::Started")
                .append("\n")

        throw IOException()
    }

    private suspend fun logExceptionSuspend(who: String, throwable: Throwable) = withContext(Dispatchers.Main) {
        logException(who, throwable)
    }

    private fun logException(who: String, throwable: Throwable) {
        Log.e(TAG, "$who::Failed", throwable)
        sb.append("$who::Recovered")
        exceptionLogView?.text = getString(
                R.string.ws04_exception_log_text,
                sb.toString(),
                throwable
        )
    }

    private fun setupViews(view: View) {
        fallByLaunchButton = view.findViewById(R.id.btnFallByLaunch)
        fallByAwaitButton = view.findViewById(R.id.btnFallByAwait)
        handleWithTryCatchButton = view.findViewById(R.id.btnHandleWithTryCatch)
        handleAwaitWithTryCatchButton = view.findViewById(R.id.btnHandleAwaitWithTryCatch)
        handleWithHandler = view.findViewById(R.id.btnHandleWithHandler)
        handleWithSuperHandler = view.findViewById(R.id.btnHandleWithSuperHandler)
        exceptionLogView = view.findViewById(R.id.tvExceptionLogText)
    }

    private fun clearViews() {
        fallByLaunchButton = null
        fallByAwaitButton = null
        handleWithTryCatchButton = null
        handleAwaitWithTryCatchButton = null
        handleWithHandler = null
        handleWithSuperHandler = null
        exceptionLogView = null
    }

    private fun setupListeners() {
        fallByLaunchButton?.setOnClickListener { failLaunchWithException() }
        fallByAwaitButton?.setOnClickListener { failAwaitWithException() }
        handleWithTryCatchButton?.setOnClickListener { workWithHandledException() }
        handleAwaitWithTryCatchButton?.setOnClickListener { awaitWorkWithHandledException() }
        handleWithHandler?.setOnClickListener { workWithExceptionHandler() }
        handleWithSuperHandler?.setOnClickListener { superWorkWithExceptionHandler() }
    }

    private fun cancelCoroutines() {
        coroutineScope.cancel("It's time")
        coroutineSupervisorScope.cancel("It's time")
    }
}

private const val TAG = "WS04"