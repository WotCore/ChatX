package wot.core.view.chartx

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import wot.core.view.chartx.axis.XAxis
import wot.core.view.chartx.axis.YAxis
import wot.core.view.chartx.axis.formatter.XAxisLabelFormatter
import wot.core.view.chartx.axis.model.AxisLabel
import wot.core.view.chartx.axis.model.AxisLine
import wot.core.view.chartx.axis.model.AxisPosition
import wot.core.view.chartx.axis.model.AxisSide
import wot.core.view.chartx.model.panel.ChartPanel
import wot.core.view.chartx.model.renderer.BaseDataRenderer
import wot.core.view.chartx.model.renderer.LineDataRenderer
import wot.core.view.chartx.model.viewport.ChartViewport

/**
 * 测试用示例控件
 *
 * @author : yangsn
 * @date : 2025/5/29
 */
class SampleChartView(context: Context, attrs: AttributeSet? = null) :
    BaseChartView(context, attrs) {

    override fun createChartPanels(viewport: ChartViewport): MutableList<ChartPanel> {
        return mutableListOf(
            ChartPanel(viewport).apply {
                val xRenderers = LineDataRenderer()
                addDataRenderers(xRenderers, LineDataRenderer(Color.RED))
                addAxis(
                    YAxis(AxisSide.LEFT, AxisPosition.OUTSIDE, 60F).apply {
                        addLabels(
                            AxisLabel(0f),
                            AxisLabel(0.25F),
                            AxisLabel(0.5F),
                            AxisLabel(0.75F),
                            AxisLabel(1F)
                        )

                        addLines(
                            AxisLine(0f),
                            AxisLine(0.25F),
                            AxisLine(0.5F),
                            AxisLine(0.75F),
                            AxisLine(1F)
                        )
                    },
                    XAxis(AxisSide.BOTTOM, AxisPosition.OUTSIDE, 56F).apply {
                        addLabels(
                            AxisLabel(0f),
                            AxisLabel(0.25F),
                            AxisLabel(0.5F),
                            AxisLabel(0.75F),
                            AxisLabel(1F)
                        )

                        addLines(
                            AxisLine(0f),
                            AxisLine(0.25F),
                            AxisLine(0.5F),
                            AxisLine(0.75F),
                            AxisLine(1F)
                        )

                        formatter = object : XAxisLabelFormatter {
                            override fun provideRenderer(): BaseDataRenderer {
                                return xRenderers
                            }

                            override fun format(
                                axisSide: AxisSide, axisPosition: AxisPosition, value: String
                            ): String {
                                return value
                            }
                        }
                    }
                )
            }
        )
    }

    override fun setPanelBounds(
        panelList: MutableList<ChartPanel>, viewWidth: Int, viewHeight: Int
    ): Float {
        val panel = panelList[0]
        panel.setBounds(0f, 0f, viewWidth.toFloat(), viewHeight.toFloat())
        return panel.getContentWidth()
    }
}