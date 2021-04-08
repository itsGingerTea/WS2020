package com.android.academy.fundamentals.workshop_1

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.android.fundamentals.R

class WS01ThreadsTaskFragment: Fragment(R.layout.fragment_ws_01) {

    private var threadButton : Button? = null
    private var threadTextView : TextView? = null

    private val handler = MyHandler()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        findViews(view)
        threadButton?.setOnClickListener {
            startThread()
            startRunnable()
        }
    }

    override fun onDestroyView() {
        threadButton = null
        threadTextView = null
        super.onDestroyView()
    }

    private fun findViews(view: View) {
        threadButton = view.findViewById(R.id.thread_button)
        threadTextView = view.findViewById(R.id.thread_text_view)
    }

    private fun startThread() {
        printMessage(getString(R.string.wait))
        val thread = MyThread()
        thread.start()
        printMessage("Wait, Thread is working")
    }

    private fun startRunnable() {
        printMessage(getString(R.string.wait))
        val thread = Thread(MyRunnable())
        thread.start()
        printMessage("Wait, Runnable is working")
    }

    private fun printMessage(mes: String){
        threadTextView?.text =  mes
    }

    inner class MyHandler : Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            printMessage(msg.data.getString(MESSAGE_KEY, ""))
        }
    }

    inner class MyThread : Thread() {
        override fun run() {
            sleep(6000)
            val msg = Message()
            msg.data.putString(MESSAGE_KEY, "Thread work")
            handler.sendMessage(msg)
        }
    }

    inner class MyRunnable : Runnable {
        override fun run() {
            Thread.sleep(6000)
            val msg = Message()
            msg.data.putString(MESSAGE_KEY, "Runnable work")
            handler.sendMessage(msg)
        }
    }

    companion object {
        private const val MESSAGE_KEY = "key"
    }
}