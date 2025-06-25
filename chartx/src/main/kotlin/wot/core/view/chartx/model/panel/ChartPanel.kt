package wot.core.view.chartx.model.panel

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import wot.core.view.chartx.axis.BaseAxis
import wot.core.view.chartx.axis.model.AxisPosition
import wot.core.view.chartx.axis.model.AxisSide
import wot.core.view.chartx.log.Logcat
import wot.core.view.chartx.model.data.ChartEntry
import wot.core.view.chartx.model.renderer.BaseDataRenderer
import wot.core.view.chartx.model.viewport.ChartViewport

/**
 * 图表面板
 *
 * @author : yangsn
 * @date : 2025/5/29
 */
data class ChartPanel(val viewport: ChartViewport) {

    /**
     *  面板区域
     */
    private val panelBounds by lazy { RectF() }

    /**
     * 内容区域( [panelBounds] 除去 坐标轴[axisList] 所有 [AxisPosition.OUTSIDE] 坐标轴宽度剩余区域)
     */
    private val contentBounds by lazy { RectF() }

    private val dataRendererList by lazy { mutableListOf<BaseDataRenderer>() } // 数据渲染器列表

    private val axisList by lazy { mutableListOf<BaseAxis>() }

    /**
     * 设置面板区域边界
     */
    fun setBounds(left: Float, top: Float, right: Float, bottom: Float) {
        panelBounds.set(left, top, right, bottom)

        var cTop = panelBounds.top
        var cRight = panelBounds.right
        var cLeft = panelBounds.left
        var cBottom = panelBounds.bottom
        // 减掉坐标轴区域
        val outsideSizes = getOutsideAxisSizes()
        for ((axisSide, axisSize) in outsideSizes) {
            when (axisSide) {
                AxisSide.LEFT -> cLeft += axisSize
                AxisSide.TOP -> cTop += axisSize
                AxisSide.RIGHT -> cRight -= axisSize
                AxisSide.BOTTOM -> cBottom -= axisSize
            }
        }
        contentBounds.set(cLeft, cTop, cRight, cBottom)

        // 设置坐标绘制区域边界
        axisList.forEach {
            it.setBounds(contentBounds)
        }
    }

    /**
     * 绘制之前的一些工作
     */
    fun prepareToDraw() {
        dataRendererList.forEach { renderer ->
            viewport.updateMaxIndex(renderer.lastIndex)   // 更新最大索引
            renderer.prepareToDraw(viewport, contentBounds) // 渲染器的准备工作
        }
        axisList.forEach { it.prepareToDraw() } // 坐标的准备工作
    }

    /**
     * 绘制图表元素
     */
    fun startDraw(canvas: Canvas, paint: Paint) {
        axisList.forEach { it.startDraw(canvas) } // 坐标轴绘制
        dataRendererList.forEach { it.startDraw(canvas, paint, viewport, contentBounds) } // 数据绘制
    }

    /**
     * 添加坐标轴，如果已有相同 axisSide + axisPosition，则忽略。
     */
    fun addAxis(vararg chartAxes: BaseAxis) {
        chartAxes.forEach { axis ->
            val exists = axisList.any {
                it.axisSide == axis.axisSide && it.axisPosition == axis.axisPosition
            }
            if (exists) {
                Logcat.w("${axis.axisSide} ${axis.axisPosition} axis already exists")
            } else {
                axisList.add(axis)
            }
        }
    }

    /**
     * 添加坐标轴，如果已有相同 axisSide + axisPosition，则替换。
     */
    fun addOrReplaceAxis(axis: BaseAxis) {
        val index = axisList.indexOfFirst {
            it.axisSide == axis.axisSide && it.axisPosition == axis.axisPosition
        }
        if (index != -1) {
            axisList[index] = axis
        } else {
            axisList.add(axis)
        }
    }

    /**
     * 添加数据渲染器
     */
    fun addDataRenderers(vararg renderers: BaseDataRenderer) {
        for (renderer in renderers) {
            dataRendererList.add(renderer)
        }
    }

    /**
     * 设置新数据
     * @param dataIndex 索引，需要将数据设置到哪个渲染器。
     */
    fun setNewData(dataIndex: Int = 0, entryList: List<ChartEntry>) {
        val renderer = dataRendererList.getOrNull(dataIndex) ?: return
        renderer.setNewData(entryList)

        viewport.updateMaxIndex(renderer.lastIndex) // 更新最大索引
        renderer.prepareToDraw(viewport, contentBounds)// 渲染器的准备工作
        axisList.forEach { it.prepareToDraw() } // 坐标的准备工作
    }

    /**
     * 获取内容宽度
     */
    fun getContentWidth() = contentBounds.width()

    /**
     * 获取指定渲染器
     */
    fun getRenderer(index: Int) = dataRendererList.getOrNull(index)

    /**
     * 取出 坐标轴[axisList] 所有 [AxisPosition.OUTSIDE] 的最大 axisSize
     */
    private fun getOutsideAxisSizes(): Map<AxisSide, Float> {
        return axisList
            .filter { it.axisPosition == AxisPosition.OUTSIDE } // 找出 [AxisPosition.OUTSIDE]
            .associate { it.axisSide to it.axisSize } // 把集合转成 map
    }
}