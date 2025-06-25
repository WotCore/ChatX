package wot.core.view.chartx.axis

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import wot.core.view.chartx.axis.formatter.XAxisLabelFormatter
import wot.core.view.chartx.axis.model.AxisPosition
import wot.core.view.chartx.axis.model.AxisSide
import wot.core.view.chartx.ext.centerY
import wot.core.view.chartx.ext.textWidth

/**
 * X 轴
 *
 * @author : yangsn
 * @date : 2025/6/27
 */
class XAxis(
    axisSide: AxisSide,
    axisPosition: AxisPosition,
    axisSize: Float
) : BaseAxis(axisSide, axisPosition, axisSize) {

    init {
        require(axisSide == AxisSide.BOTTOM || axisSide == AxisSide.TOP) {
            "X 轴只能是 AxisSide.TOP 或 AxisSide.BOTTOM"
        }
    }

    override fun calculateAxisBounds(contentBounds: RectF) {
        var top = contentBounds.top
        var bottom = contentBounds.bottom
        if (axisPosition == AxisPosition.OUTSIDE) {
            if (axisSide == AxisSide.TOP) top -= axisSize else bottom += axisSize
        }
        val axisTop = if (axisSide == AxisSide.TOP) top else bottom - axisSize
        axisBounds.set(contentBounds.left, axisTop, contentBounds.right, axisTop + axisSize)
    }

    override fun prepareLines() {
        val bounds = panelContentBounds ?: return
        val width = bounds.width()
        for (line in lineList) {
            val x = bounds.left + width * line.ratio
            line.startPoint.set(x, bounds.top)
            line.endPoint.set(x, bounds.bottom)
        }
    }

    override fun prepareLabels() {
        paint.textSize = labelTextSize
        val formatter = formatter as? XAxisLabelFormatter
        val y = paint.centerY(axisBounds)

        labelList.forEach { label ->
            val x = axisBounds.left + axisBounds.width() * label.ratio
            if (label.isNeedCalculateText()) {
                label.text = if (formatter == null) {
                    "$x" // 兜底
                } else {
                    buffer[0] = x
                    val renderer = formatter.provideRenderer()
                    renderer.pixelsToData(buffer)
                    val index = buffer[0].toInt()
                    val value = renderer.entryAt(index)?.xValue.orEmpty()
                    formatter.format(axisSide, axisPosition, value)
                }
            }
            label.x = when {
                label.ratio <= 0f -> x  // 左边对齐轴线
                label.ratio >= 1f -> axisBounds.right - paint.textWidth(label.text) // 右边对齐轴线
                else -> x - paint.textWidth(label.text) / 2 // 居中对齐
            }
            label.y = y
        }
    }

    override fun drawLines(canvas: Canvas) {
        linePaint.apply {
            strokeWidth = lineWidth
            color = lineColor
            pathEffect = linePathEffect
        }
        for (line in lineList) {
            linePath.reset()
            linePath.moveTo(line.startX, line.startY)
            linePath.lineTo(line.endX, line.endY)
            canvas.drawPath(linePath, linePaint)
        }
        linePaint.pathEffect = null  // 清除 pathEffect，避免影响后续绘制
    }

    override fun drawLabels(canvas: Canvas) {
        paint.apply {
            color = labelTextColor
            textSize = labelTextSize
            textAlign = Paint.Align.LEFT
        }
        labelList.forEach {
            canvas.drawText(it.text.orEmpty(), it.x, it.y, paint)
        }
    }
}
