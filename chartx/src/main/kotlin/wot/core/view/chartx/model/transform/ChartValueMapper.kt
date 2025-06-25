package wot.core.view.chartx.model.transform

import android.graphics.Matrix
import android.graphics.RectF

/**
 * 标映射器：用于值 <=> 像素 的变换
 *
 * @author : yangsn
 * @date : 2025/6/20
 */
/**
 * 坐
 */
class ChartValueMapper {

    private val valueToPxMatrix = Matrix()
    private val pxToValueMatrix = Matrix()

    /**
     * 坐标映射
     */
    fun buildMatrix(
        contentRect: RectF,
        yMin: Float,
        yRange: Float,
        startIndex: Int,
        pointWidth: Float
    ) {
        val scaleX = pointWidth.takeIf { it.isFinite() } ?: 0f
        val scaleY = (contentRect.height() / yRange).takeIf { it.isFinite() } ?: 0f

        valueToPxMatrix.reset()
        valueToPxMatrix.postTranslate(-startIndex.toFloat(), -yMin)
        valueToPxMatrix.postScale(scaleX, -scaleY)
        valueToPxMatrix.postTranslate(contentRect.left, contentRect.bottom)
    }

    /**
     * 值数据映射成像素数据
     */
    fun toPixels(data: FloatArray) {
        valueToPxMatrix.mapPoints(data)
    }

    /**
     * 坐标像素映射成数据值
     *
     * ===================== ⚠ 注意 =====================
     * [buildMatrix] 对 x 轴进行了 [contentRect.left] 偏移，所以传入的像素坐标必须是已加偏移的值！否则会导致坐标映射错误。
     * =================================================
     */
    fun toData(pixels: FloatArray) {
        valueToPxMatrix.invert(pxToValueMatrix)
        pxToValueMatrix.mapPoints(pixels)
    }
}
