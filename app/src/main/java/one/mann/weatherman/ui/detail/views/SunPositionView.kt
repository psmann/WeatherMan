package one.mann.weatherman.ui.detail.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import one.mann.weatherman.R
import kotlin.math.pow

/* Created by Psmann. */

internal class SunPositionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val CONTROL_Y = -37f // y control points 1 and 2 (y1 and y2) = height of curve
        private const val OFFSET = 46f // Fix extra image padding for file used
        private const val HORIZONTAL_PADDING = 21f // Horizontal padding for image file used
        private const val ALPHA_VALUE = 165 // Alpha for paintCurve
        private const val RED_VALUE = 255 // Red value for paintCurve
        private const val GREEN_VALUE = 175 // Green value for paintCurve
        private const val BLUE_VALUE = 75 // Blue value for paintCurve
    }

    private val pathCurve = Path()
    private val paintCurve = Paint()
    private val paintBitmap = Paint()
    private val sunImage = BitmapFactory.decodeResource(resources, R.drawable.weather_parameter_sun_position)
    private val screenDensity = resources.displayMetrics.density
    private var displaySun = false
    private var startX = 0f // x start point (x0)
    private var startEndY = 0f // y start and end points (y0 and y3) = height of view
    private var controlX1 = 0f // x control point 1 (x1)
    private var controlX2 = 0f // x control point 2 (x2)
    private var endX = 0f // x end point (x3) = width of view
    private var pointX = 0f // x coordinate on curve
    private var pointY = 0f // y coordinate on curve
    private var tValue = 0f // Location on the curve (0 to 1) where coordinates are required

    /** Update sun position on the graph */
    fun setT(t: Float) {
        tValue = t
    }

    /** Calculate coordinates and set-up the curve */
    private fun updateView() {
        // Set up coordinates
        startX = pxToDip(left.toFloat()) + HORIZONTAL_PADDING
        endX = width - startX
        startEndY = height - pxToDip(top.toFloat())
        controlX1 = (endX + startX) * 0.25f // = one-fourth distance
        controlX2 = (endX + startX) * 0.75f // = three-fourth distance

        // Set up Cubic Bezier curve
        pathCurve.reset() // Reset previous path if any
        pathCurve.moveTo(startX, startEndY) // (x0, y0)
        pathCurve.cubicTo(controlX1, CONTROL_Y, controlX2, CONTROL_Y, endX, startEndY) // (x1, y1, x2, y2, x3, y3)

        // Calculate desired coordinates on the curve using formula:
        // X(t) = (1-t)^3 * X0 + 3*(1-t)^2 * t * X1 + 3*(1-t) * t^2 * X2 + t^3 * X3
        // Y(t) = (1-t)^3 * Y0 + 3*(1-t)^2 * t * Y1 + 3*(1-t) * t^2 * Y2 + t^3 * Y3
        if (tValue in 0f..1f) {
            val k = 1 - tValue // constant calculated for brevity
            pointX = ((k.pow(3) * startX) + (3 * k.pow(2) * tValue * controlX1)
                + (3 * k * tValue.pow(2) * controlX2) + (tValue.pow(3) * endX)) - OFFSET
            pointY = ((k.pow(3) * startEndY) + (3 * k.pow(2) * tValue * CONTROL_Y)
                + (3 * k * tValue.pow(2) * CONTROL_Y) + (tValue.pow(3) * startEndY)) - OFFSET
            // Colour transition according to amount of day light left. 0 = yellow; 100 = purple
            updatePaintColour((tValue * 100).toInt())
            displaySun = true
        } else {
            updatePaintColour()
            displaySun = false
        }
    }

    /** Change colour of the graph */
    private fun updatePaintColour(change: Int = 125) {
        paintCurve.setARGB(ALPHA_VALUE, RED_VALUE - change, GREEN_VALUE - change, BLUE_VALUE + change)
    }

    /** Convert pixels to density independent pixels */
    private fun pxToDip(value: Float): Float = value / screenDensity

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec))
    }

    /** Update parameters only after correct height and width are known */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        updateView()
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawPath(pathCurve, paintCurve)
        if (displaySun) canvas.drawBitmap(sunImage, pointX, pointY, paintBitmap)
    }
}