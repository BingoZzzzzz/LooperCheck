package com.bingo.library

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.Log

object LooperCheck {

    private var TAG = "LooperCheck"

    private var mHandler: Handler? = null

    init {
        val thread = HandlerThread("log")
        thread.start()
        mHandler = Handler(thread.looper)
    }

    fun start(logName: String, time: Long) {
        TAG = logName;
        start(time)
    }

    fun start(time: Long) {
        Looper.getMainLooper().setMessageLogging {
            Log.i(TAG, it)
            if (it.startsWith(">>>>> Dispatching")) {
                mHandler?.postDelayed(mLogRunnable, time)
            }
            if (it.startsWith("<<<<< Finished")) {
                mHandler?.removeCallbacks(mLogRunnable)
            }
        }
    }

    private val mLogRunnable = Runnable {
        val sb = StringBuilder()
        val stackTrace = Looper.getMainLooper().thread.stackTrace
        for (s in stackTrace) {
            sb.append(s.toString())
            sb.append("\n")
        }
        Log.e(
            TAG, sb.toString()
        )
    }
}