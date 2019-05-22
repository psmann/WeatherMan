package one.mann.weatherman.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import kotlin.math.pow

class SunlightGraph @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val path = Path()
    private val paint = Paint()
    private var startX = 0f // x start point (x0)
    private var startY = 0f // y start point (y0)
    private var controlX1 = 0f // x control point 1 (x1)
    private var controlY = 0f // y control points 1 and 2 (y1 and y2)
    private var controlX2 = 0f // x control point 2 (x2)
    private var endX = 0f // x end point (x3)
    private var endY = 0f // y end point (y3)
    private var pointX = 0f // x coordinate on curve
    private var pointY = 0f // y coordinate on curve
    private val startPointLoc = intArrayOf(0, 0)
    private val endPointLoc = intArrayOf(0, 0)
    private val curveLoc = intArrayOf(0, 0)
    private val offset = 75f // fix extra padding of image, startX must be 24dp from left border
    private val alphaValue = 165
    private var redValue = 255
    private var greenValue = 175
    private var blueValue = 75

    init { // todo add more paint attributes, change colour
        updateColour()
        paint.style = Paint.Style.FILL
    }

    fun updateView(startPoint: View, endPoint: View, image: ImageView, t: Float = 0f) {
        // get location of all views
        startPoint.getLocationInWindow(startPointLoc)
        endPoint.getLocationInWindow(endPointLoc)
        this.getLocationInWindow(curveLoc)

        // set up all the coordinates
        startX = (startPointLoc[0] - curveLoc[0]).toFloat()
        startY = (startPointLoc[1] - curveLoc[1]).toFloat()
        endX = (endPointLoc[0] - curveLoc[0] + 1).toFloat() // +1 to adjust for endPoint view width
        endY = (endPointLoc[1] - curveLoc[1]).toFloat() // = startY
        controlX1 = (endX + startX) * 0.25f // one-fourth distance
        controlX2 = (endX + startX) * 0.75f // three-fourth distance
        controlY = startY - 300 // = height of the curve

        // set up Cubic Bezier curve
        path.moveTo(startX, startY) // (x0, y0)
        path.cubicTo(controlX1, controlY, controlX2, controlY, endX, endY) // (x1, y1, x2, y2, x3, y3)
        /*
           calculate desired coordinates on the curve using formula:
           X(t) = (1-t)^3 * X0 + 3*(1-t)^2 * t * X1 + 3*(1-t) * t^2 * X2 + t^3 * X3
           Y(t) = (1-t)^3 * Y0 + 3*(1-t)^2 * t * Y1 + 3*(1-t) * t^2 * Y2 + t^3 * Y3
           t = percent of curve (0 to 1) where coordinates are required
        */
        if (t in 0f..1f) {
            val k = 1 - t // constant calculated for brevity
            pointX = ((k.pow(3) * startX) + (3 * k.pow(2) * t * controlX1)
                    + (3 * k * t.pow(2) * controlX2) + (t.pow(3) * endX)) - offset
            pointY = ((k.pow(3) * startY) + (3 * k.pow(2) * t * controlY)
                    + (3 * k * t.pow(2) * controlY) + (t.pow(3) * endY)) - offset

            // colour transition according to amount of day light left; 0 = yellow, 100 = purple
            updateColour((t * 100).toInt())

            // translate image to calculated coordinates
            val params = image.layoutParams as ConstraintLayout.LayoutParams
            params.marginStart = pointX.toInt()
            params.topMargin = pointY.toInt()
            image.layoutParams = params
            image.visibility = VISIBLE
        } else image.visibility = GONE

        // re-draw view
        invalidate()
    }

    private fun updateColour(transition: Int = 100) = paint.setARGB(alphaValue,
            redValue - transition, greenValue - transition, blueValue + transition)

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawPath(path, paint)
//        canvas?.drawCircle(pointX + offset, pointY + offset, 25f, paint) // remove offset
    }
}