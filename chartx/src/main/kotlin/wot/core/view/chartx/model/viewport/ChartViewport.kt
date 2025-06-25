package wot.core.view.chartx.model.viewport

import kotlin.math.roundToInt

/**
 * 图表 视窗
 *
 * @author : yangsn
 * @date : 2025/6/3
 */
class ChartViewport {

    /**
     * 点的最大宽度
     */
    private var pointMaxWidth: Float = 100F

    /**
     * 一页可以展示的点数
     */
    var pageSize: Int = 0
        private set
    var maxIndex: Int = 0 // 最大索引
        private set
    var startIndex: Int = 0 // 起始索引
        private set
    var endIndex: Int = -1 // 结束索引
        private set

    /**
     * 图表内容宽
     */
    private var contentWidth: Float = 0F

    /**
     * 点的真实宽度
     */
    val pointRealWidth: Float
        get() = contentWidth / (pageSize - 1)

    fun setContentWidth(contentWidth: Float) {
        this.contentWidth = contentWidth
        calcMaxVisiblePoints()
        checkIndex()
    }

    /**
     * 更新最大索引
     */
    fun updateMaxIndex(max: Int) {
        // 先计算结束索引
        endIndex = if (endIndex == -1) {
            // 未赋值, 直接赋值
            max
        } else {
            // 已赋值, 则计算出新的结束索引
            endIndex - maxIndex + max
        }

        maxIndex = maxIndex.coerceAtLeast(max)

        // 计算开始索引
        calcStartIndex()
    }

    /**
     * 根据用户在图表中的水平滑动距离，平移当前可见的数据范围。
     *
     * - 向右滑动（moveX > 0）：查看更早的数据，前移数据窗口。
     * - 向左滑动（moveX < 0）：查看更新的数据，后移数据窗口。
     *
     * 自动处理索引边界，避免越界。
     *
     * @param moveX 水平方向滑动的距离（单位：像素）
     */
    fun panVisibleRange(moveX: Float) {
        panVisibleRange((moveX / pointMaxWidth).toInt())
    }

    /**
     * 平移当前可见的数据范围。
     *
     * - 向右滑动（moveCount > 0）：查看更早的数据，前移数据窗口。
     * - 向左滑动（moveCount < 0）：查看更新的数据，后移数据窗口。
     *
     * 自动处理索引边界，避免越界。
     *
     * @param moveCount 移动点数
     */
    fun panVisibleRange(moveCount: Int) {
        if (moveCount > 0) {
            // 向右滑动，显示更早的数据
            startIndex = (startIndex - moveCount).coerceAtLeast(0)
            calcEndIndex()
        } else if (moveCount < 0) {
            // 向左滑动，显示更新的数据
            endIndex = (endIndex - moveCount).coerceAtMost(maxIndex)
            calcStartIndex()
        }
    }

    /**
     * 计算最大可见点数
     */
    private fun calcMaxVisiblePoints() {
        pageSize = (contentWidth / pointMaxWidth).roundToInt()
    }

    /**
     * 检查索引
     */
    private fun checkIndex() {
        if (startIndex == 0) {
            calcEndIndex()
        } else {
            calcStartIndex()
        }
    }

    /**
     * 计算开始索引
     */
    private fun calcStartIndex() {
        startIndex = endIndex - pageSize + 1
        // 限制边界
        if (startIndex < 0) {
            startIndex = 0
        } else if (startIndex > maxIndex) {
            startIndex = maxIndex
        }
    }

    /**
     * 计算结束索引
     */
    private fun calcEndIndex() {
        endIndex = startIndex + pageSize - 1
        // 限制边界
        if (endIndex < 0) {
            endIndex = 0
        } else if (endIndex > maxIndex) {
            endIndex = maxIndex
        }
    }

    override fun toString(): String {
        return "ChartViewport(pageSize=$pageSize, maxIndex=$maxIndex, startIndex=$startIndex, endIndex=$endIndex, contentWidth=$contentWidth, pointMaxWidth=$pointMaxWidth, pointRealWidth=$pointRealWidth)"
    }
}