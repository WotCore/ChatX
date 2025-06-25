package wot.core.demo.chartx.ui

import wot.core.view.chartx.model.data.ChartEntry
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.random.Random

/**
 * 功能说明
 *
 * @author : yangsn
 * @date : 2025/5/29
 */
object DataUtils {

    private val YYYY_MM_DD by lazy { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }
    private val HH_MM by lazy { SimpleDateFormat("HH:mm", Locale.getDefault()) }

    private fun createCalendar(): Calendar {
        val calendar = Calendar.getInstance().apply {
            set(2025, Calendar.MAY, 1, 9, 30, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar
    }

    fun chatData(
        times: Int = 80,
        min: Float = 9.5F,
        max: Float = 10.5F,
    ): MutableList<ChartEntry> {
        val dataList = mutableListOf<ChartEntry>()
        val calendar = createCalendar()
        repeat(times) {
            val xValue = YYYY_MM_DD.format(calendar.time)
            val yValue = Random.nextFloat() * (max - min) + min
            dataList.add(ChartEntry(xValue, yValue))
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        return dataList
    }

    fun chatData1(
        times: Int = 10,
    ): MutableList<ChartEntry> {
        val dataList = mutableListOf<ChartEntry>()
        for (i in 0 until times) {
            val xValue = "$i"
            val yValue = i.toFloat()
            dataList.add(ChartEntry(xValue, yValue))
        }
        return dataList
    }
}