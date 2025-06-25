package wot.core.view.chartx.axis

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathEffect
import android.graphics.RectF
import wot.core.view.chartx.axis.formatter.IAxisLabelFormatter
import wot.core.view.chartx.axis.formatter.XAxisLabelFormatter
import wot.core.view.chartx.axis.formatter.YAxisLabelFormatter
import wot.core.view.chartx.axis.model.AxisLabel
import wot.core.view.chartx.axis.model.AxisLine
import wot.core.view.chartx.axis.model.AxisPosition
import wot.core.view.chartx.axis.model.AxisSide

/**
 * 图表坐标轴
 * 包含 [AxisLine] [AxisLabel]
 *
 * @author : yangsn
 * @date : 2025/5/29
 */
abstract class BaseAxis(
    val axisSide: AxisSide,
    val axisPosition: AxisPosition,
    val axisSize: Float
) {
    var labelTextSize: Float = 30F
    var labelTextColor: Int = Color.parseColor("#333333")

    /**
     * 图表轴值格式化
     * [YAxis] 使用 [YAxisLabelFormatter]
     * [XAxis] 使用 [XAxisLabelFormatter]
     */
    var formatter: IAxisLabelFormatter? = null

    protected var panelContentBounds: RectF? = null

    /**
     * 坐标轴的绘制边界
     */
    protected val axisBounds = RectF()

    protected val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL }
    protected val labelList = mutableListOf<AxisLabel>()

    var linePathEffect: PathEffect? = null
    var lineWidth = 3F
    var lineColor = Color.parseColor("#F0F0F0")
    protected val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.STROKE }
    protected val linePath = Path()
    protected val lineList = mutableListOf<AxisLine>()

    protected val buffer = FloatArray(2)

    /**
     * 添加轴线
     */
    fun addLines(vararg lines: AxisLine) {
        lineList.addAll(lines)
    }

    /**
     * 添加标签
     */
    fun addLabels(vararg label: AxisLabel) {
        labelList.addAll(label)
    }

    /**
     * 设置坐标区域
     * @param contentBounds 内容边界
     */
    fun setBounds(contentBounds: RectF) {
        panelContentBounds = contentBounds
        calculateAxisBounds(contentBounds)
        prepareToDraw()
    }

    /**
     * 绘制之前的一些准备工作
     */
    fun prepareToDraw() {
        prepareLines()
        prepareLabels()
    }

    /**
     * 开始绘制
     */
    fun startDraw(canvas: Canvas) {
        drawLines(canvas)
        drawLabels(canvas)
    }

    /**
     * 计算坐标轴边界
     */
    abstract fun calculateAxisBounds(contentBounds: RectF)

    abstract fun prepareLines()

    abstract fun prepareLabels()

    abstract fun drawLines(canvas: Canvas)

    abstract fun drawLabels(canvas: Canvas)
}