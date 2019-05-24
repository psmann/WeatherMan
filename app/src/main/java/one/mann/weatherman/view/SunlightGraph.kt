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

/*
    To use set in xml:
    layout_width = 0dp (match_constraint)
    layout_height = wrap_content
    layout_constraintEnd_toEndOf="parent"
    layout_constraintStart_toStartOf="parent"
    margins 16dp (preferably)
*/

class SunlightGraph @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val path = Path()
    private val paint = Paint()
    private var startX = 0f // x start point (x0)
    private var startEndY = 300f // y start and end points (y0 and y3) = height of the view
    private var controlX1 = 0f // x control point 1 (x1)
    private var controlY = 0f // y control points 1 and 2 (y1 and y2) = height of the curve
    private var controlX2 = 0f // x control point 2 (x2)
    private var endX = 0f // x end point (x3)
    private var pointX = 0f // x coordinate on curve
    private var pointY = 0f // y coordinate on curve
    private val offsetX = 6f // fix extra padding of image for x-axis
    private val offsetY = 46f // fix extra padding of image for y-axis
    private val alphaValue = 165
    private var redValue = 255
    private var greenValue = 175
    private var blueValue = 75
    private var screenDensity = 0f

    init {
        updatePaintColour()
        paint.style = Paint.Style.FILL
        screenDensity = resources.displayMetrics.density
    }

    fun updateView(image: ImageView, t: Float = 0.0f) {
        // set up coordinates
        startX = pixelToDip(left.toFloat())
        endX = width - startX
        controlX1 = (endX + startX) * 0.25f // one-fourth distance
        controlX2 = (endX + startX) * 0.75f // three-fourth distance

        // set up Cubic Bezier curve
        path.moveTo(startX, startEndY) // (x0, y0)
        path.cubicTo(controlX1, controlY, controlX2, controlY, endX, startEndY) // (x1, y1, x2, y2, x3, y3)

        /* calculate desired coordinates on the curve using formula:
           X(t) = (1-t)^3 * X0 + 3*(1-t)^2 * t * X1 + 3*(1-t) * t^2 * X2 + t^3 * X3
           Y(t) = (1-t)^3 * Y0 + 3*(1-t)^2 * t * Y1 + 3*(1-t) * t^2 * Y2 + t^3 * Y3
           t = percent of curve (0 to 1) where coordinates are required */
        if (t in 0f..1f) {
            val k = 1 - t // constant calculated for brevity
            pointX = ((k.pow(3) * startX) + (3 * k.pow(2) * t * controlX1)
                    + (3 * k * t.pow(2) * controlX2) + (t.pow(3) * endX)) + offsetX
            pointY = ((k.pow(3) * startEndY) + (3 * k.pow(2) * t * controlY)
                    + (3 * k * t.pow(2) * controlY) + (t.pow(3) * startEndY)) - offsetY

            // colour transition according to amount of day light left; 0 = yellow, 100 = purple
            updatePaintColour((t * 100).toInt())
            // translate image to calculated coordinates
            val params = image.layoutParams as ConstraintLayout.LayoutParams
            params.marginStart = pointX.toInt()
            params.topMargin = pointY.toInt()
            image.layoutParams = params
            image.visibility = VISIBLE
        } else image.visibility = GONE

        // get layout and redraw
        requestLayout()
        invalidate()
    }

    private fun updatePaintColour(transition: Int = 125) = paint.setARGB(alphaValue,
            redValue - transition, greenValue - transition, blueValue + transition)

    private fun pixelToDip(value: Float): Float = value / screenDensity

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension((endX).toInt(), startEndY.toInt())
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawPath(path, paint)
        //canvas?.drawCircle(pointX - offsetX, pointY + offsetY, 25f, paint) // remove offset
    }
}