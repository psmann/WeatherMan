package one.mann.weatherman.ui.main

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import one.mann.weatherman.R
import kotlin.math.pow

/*  layout_width must be either match_parent/constraint or specific value, NOT wrap_content
    layout_height must be wrap_content, height can be changed by editing startEndY value
    constants offset and padding are set for the image file being used    */

internal class SunGraphView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val pathCurve = Path()
    private val paintCurve = Paint()
    private val paintBitmap = Paint()
    private val image = BitmapFactory.decodeResource(resources, R.drawable.sun_cropped)
    private val screenDensity = resources.displayMetrics.density
    private var displayImage = false
    private var startX = 0f // x start point (x0)
    private val startEndY = 275f // y start and end points (y0 and y3) = height of the view
    private val controlY = -38f // y control points 1 and 2 (y1 and y2) = relative height of the curve
    private var controlX1 = 0f // x control point 1 (x1)
    private var controlX2 = 0f // x control point 2 (x2)
    private var endX = 0f // x end point (x3)
    private var pointX = 0f // x coordinate on curve
    private var pointY = 0f // y coordinate on curve
    private val offset = 46f // fix extra image padding
    private val padding = 21f // horizontal padding for image
    private val alphaValue = 165 // alpha for paintCurve
    private val redValue = 255 // red for paintCurve
    private val greenValue = 175 // green for paintCurve
    private val blueValue = 75 // blue for paintCurve

    fun updateView(t: Float = 0f) {
        // set up coordinates
        startX = pxToDip(left.toFloat()) + padding
        endX = width - startX
        controlX1 = (endX + startX) * 0.25f // one-fourth distance
        controlX2 = (endX + startX) * 0.75f // three-fourth distance

        // set up Cubic Bezier curve
        pathCurve.moveTo(startX, startEndY) // (x0, y0)
        pathCurve.cubicTo(controlX1, controlY, controlX2, controlY, endX, startEndY) // (x1, y1, x2, y2, x3, y3)

        /*  calculate desired coordinates on the curve using formula:
            X(t) = (1-t)^3 * X0 + 3*(1-t)^2 * t * X1 + 3*(1-t) * t^2 * X2 + t^3 * X3
            Y(t) = (1-t)^3 * Y0 + 3*(1-t)^2 * t * Y1 + 3*(1-t) * t^2 * Y2 + t^3 * Y3
            t = percent of curve (0 to 1) where coordinates are required    */
        if (t in 0f..1f) {
            val k = 1 - t // constant calculated for brevity
            pointX = ((k.pow(3) * startX) + (3 * k.pow(2) * t * controlX1)
                    + (3 * k * t.pow(2) * controlX2) + (t.pow(3) * endX)) - offset
            pointY = ((k.pow(3) * startEndY) + (3 * k.pow(2) * t * controlY)
                    + (3 * k * t.pow(2) * controlY) + (t.pow(3) * startEndY)) - offset

            // colour transition according to amount of day light left; 0 = yellow, 100 = purple
            updatePaintColour((t * 100).toInt())
            displayImage = true
        } else {
            updatePaintColour()
            displayImage = false
        }
        // get layout and redraw
        requestLayout()
        invalidate()
    }

    private fun updatePaintColour(transition: Int = 125) = paintCurve.setARGB(alphaValue,
            redValue - transition, greenValue - transition, blueValue + transition)

    private fun pxToDip(value: Float): Float = value / screenDensity

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) =
            setMeasuredDimension((endX).toInt(), startEndY.toInt())

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawPath(pathCurve, paintCurve)
        if (displayImage) canvas?.drawBitmap(image, pointX, pointY, paintBitmap)
        //canvas?.drawCircle(pointX + offset, pointY + offset, 25f, paintCurve) // remove offset
    }
}