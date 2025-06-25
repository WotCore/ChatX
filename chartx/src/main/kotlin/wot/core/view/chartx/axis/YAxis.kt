package wot.core.view.chartx.axis

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import wot.core.view.chartx.axis.formatter.YAxisLabelFormatter
import wot.core.view.chartx.axis.model.AxisPosition
import wot.core.view.chartx.axis.model.AxisSide

/**
 * 功能说明
 *
 * @author : yangsn
 * @date : 2025/6/27
 */
class YAxis(
    axisSide: AxisSide,
    axisPosition: AxisPosition,
    axisSize: Float
) : BaseAxis(axisSide, axisPosition, axisSize) {

    init {
        require(axisSide == AxisSide.LEFT || axisSide == AxisSide.RIGHT) {
            "Y 轴只能是 AxisSide.LEFT 或 AxisSide.RIGHT"
        }
    }

    /**
     * 标签与轴之间的间距, 防止贴边。
     */
    var horizontalMargin = 8F

    override fun calculateAxisBounds(contentBounds: RectF) {
        var left = contentBounds.left
        var right = contentBounds.right
        if (axisPosition == AxisPosition.OUTSIDE) {
            if (axisSide == AxisSide.LEFT) left -= axisSize else right += axisSize
        }
        val xLeft = if (axisSide == AxisSide.LEFT) left else right - axisSize
        axisBounds.set(xLeft, contentBounds.top, xLeft + axisSize, contentBounds.bottom)
    }

    override fun prepareLines() {
        val bounds = panelContentBounds ?: return
        val height = bounds.height()
        for (line in lineList) {
            val y = bounds.bottom - height * line.ratio
            line.startPoint.set(bounds.left, y)
            line.endPoint.set(bounds.right, y)
        }
    }

    override fun prepareLabels() {
        paint.textSize = labelTextSize
        val formatter = formatter as? YAxisLabelFormatter
        val descent = paint.descent()
        val ascent = paint.ascent()

        val x = when (axisSide) {
            AxisSide.LEFT -> if (axisPosition == AxisPosition.INSIDE) {
                axisBounds.left + horizontalMargin
            } else {
                axisBounds.right - horizontalMargin
            }

            AxisSide.RIGHT -> if (axisPosition == AxisPosition.INSIDE) {
                axisBounds.right - horizontalMargin
            } else {
                axisBounds.left + horizontalMargin
            }

            else -> axisBounds.left + horizontalMargin // 容错处理
        }

        val totalHeight = axisBounds.height()
        val bottom = axisBounds.bottom
        for (label in labelList) {
            val ratio = label.ratio
            val axisValue = totalHeight * ratio
            if (label.isNeedCalculateText()) {
                label.text = if (formatter == null) {
                    "$axisValue" // 兜底
                } else {
                    buffer[1] = axisValue
                    formatter.format(axisSide, axisPosition, buffer[1])
                }
            }

            val y = bottom - axisValue
            label.y = when {
                ratio <= 0f -> y - descent // 顶部对齐
                ratio >= 1f -> y - ascent // 底部对齐
                else -> y - (ascent + descent) / 2f // 居中对齐
            }

            label.x = x
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
        linePaint.pathEffect = null
    }

    override fun drawLabels(canvas: Canvas) {
        paint.apply {
            color = labelTextColor
            textSize = labelTextSize
            textAlign = when (axisSide) {
                AxisSide.LEFT -> if (axisPosition == AxisPosition.INSIDE) Paint.Align.LEFT else Paint.Align.RIGHT
                AxisSide.RIGHT -> if (axisPosition == AxisPosition.INSIDE) Paint.Align.RIGHT else Paint.Align.LEFT
                else -> Paint.Align.LEFT
            }
        }
        labelList.forEach {
            canvas.drawText(it.text.orEmpty(), it.x, it.y, paint)
        }
    }
}
