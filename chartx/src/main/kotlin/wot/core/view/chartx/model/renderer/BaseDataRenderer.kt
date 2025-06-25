package wot.core.view.chartx.model.renderer

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import wot.core.view.chartx.model.data.ChartDataSet
import wot.core.view.chartx.model.data.ChartEntry
import wot.core.view.chartx.model.transform.ChartValueMapper
import wot.core.view.chartx.model.viewport.ChartViewport
import kotlin.math.max
import kotlin.math.min

/**
 * 数据渲染器 基类
 *
 * @author : yangsn
 * @date : 2025/5/29
 */
abstract class BaseDataRenderer {

    /**
     *数据的起始索引
     */
    protected var startIndex: Int = 0
        private set

    /**
     *数据的结束索引
     */
    protected var endIndex: Int = 0
        private set

    private val dataSet by lazy { ChartDataSet() }
    private val valueMapper by lazy { ChartValueMapper() }

    val entryCount: Int
        get() = dataSet.entryCount()

    val lastIndex: Int
        get() = dataSet.lastIndex()

    /**
     * 绘制
     * @param canvas 画布
     * @param paint 画笔
     * @param contentRectF 内容区域
     * @param dataSet 图表数据管理器
     */
    abstract fun onDraw(canvas: Canvas, paint: Paint, contentRectF: RectF, dataSet: ChartDataSet)

    /**
     * 根据索引获取指定的 entry
     */
    fun entryAt(index: Int) = dataSet.entryAt(index)

    fun setNewData(newEntries: List<ChartEntry>) {
        dataSet.setNewData(newEntries)
    }

    /**
     * 绘制前的工作
     */
    fun prepareToDraw(viewport: ChartViewport, contentRect: RectF) {
        val startIndex = viewport.startIndex
        val pointWidth = viewport.pointRealWidth
        val yMin = dataSet.yMin
        val yRange = dataSet.yRange
        valueMapper.buildMatrix(contentRect, yMin, yRange, startIndex, pointWidth) // 坐标映射
    }

    /**
     * 开始绘制
     */
    fun startDraw(canvas: Canvas, paint: Paint, viewport: ChartViewport, contentRect: RectF) {
        // 计算渲染实际索引
        calcRenderRange(viewport.startIndex, viewport.endIndex, dataSet.lastIndex())
        // 绘制
        onDraw(canvas, paint, contentRect, dataSet)
    }

    /**
     * 值数据映射成像素数据
     */
    fun dataToPixels(data: FloatArray) {
        valueMapper.toPixels(data)
    }

    /**
     * 坐标像素映射成数据值
     */
    fun pixelsToData(pixels: FloatArray) {
        valueMapper.toData(pixels)
    }

    /**
     * 计算渲染范围
     */
    private fun calcRenderRange(panelStartIndex: Int, panelEndIndex: Int, entryMaxIndex: Int) {
        this.startIndex = max(panelStartIndex, 0)
        this.endIndex = min(panelEndIndex, entryMaxIndex)
    }
}