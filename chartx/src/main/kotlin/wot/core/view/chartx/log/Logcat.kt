package wot.core.view.chartx.log

import android.util.Log
import wot.core.view.chartx.BuildConfig

/**
 * 日志输出
 *
 * @author : yangsn
 * @date : 2025/6/16
 */
object Logcat {

    private const val TAG = "ChartX"
    private var DEBUG = BuildConfig.DEBUG

    fun i(msg: String) {
        Log.i(TAG, msg)
    }

    fun d(msg: String) {
        if (DEBUG) {
            Log.d(TAG, msg)
        }
    }

    fun w(msg: String) {
        Log.w(TAG, msg)
    }
}