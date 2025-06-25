package wot.core.view.chartx.axis.formatter

import wot.core.view.chartx.axis.model.AxisPosition
import wot.core.view.chartx.axis.model.AxisSide
import wot.core.view.chartx.model.renderer.BaseDataRenderer

/**
 * 图表轴值格式化 [AxisSide.TOP] or [AxisSide.BOTTOM]
 *
 * @author : yangsn
 * @date : 2025/6/16
 */
interface XAxisLabelFormatter : IAxisLabelFormatter {

    /**
     * 提供数据渲染器
     * 1. 需要提供数据渲染器, 才知道显示的是哪份数据的值, 才知道渲染的数据是什么
     */
    fun provideRenderer(): BaseDataRenderer

    /**
     * 格式化轴值
     * @param axisSide 轴所在侧
     * @param axisPosition 轴所在位置
     * @param value 坐标值
     */
    fun format(axisSide: AxisSide, axisPosition: AxisPosition, value: String): String
}