package wot.core.view.chartx.axis.model

import android.graphics.PointF

/**
 * 图表坐标轴线
 *
 * @author : yangsn
 * @date : 2025/6/19
 */
data class AxisLine(val ratio: Float) {

    /**
     * 起始点
     */
    val startPoint by lazy { PointF() }

    /**
     * 结束点
     */
    val endPoint by lazy { PointF() }

    val startX: Float
        get() = startPoint.x

    val startY: Float
        get() = startPoint.y

    val endX: Float
        get() = endPoint.x

    val endY: Float
        get() = endPoint.y
}