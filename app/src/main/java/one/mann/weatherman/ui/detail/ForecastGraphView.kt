package one.mann.weatherman.ui.detail

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

internal class ForecastGraphView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val alphaValue = 64 // Alpha for paintLine
        private const val redValue = 255 // Red value for paintLine
        private const val greenValue = 175 // Green value for paintLine
        private const val blueValue = 75 // Blue value for paintLine
        private const val lineWidth = 37f // Width of line
        private const val bottomOffset = lineWidth // Bottom offset to remove cropping of line
        private const val topOffset = lineWidth / 2 // Top offset to remove cropping of line
    }

    private val paintLine = Paint()
    private lateinit var coordinates: FloatArray
    private var points = listOf(0f, 0f, 0f, 0f, 0f, 0f, 0f)

    fun setPoints(pts: List<Float>) {
        points = pts
    }

    /** Calculate coordinates and set-up the lines */
    private fun updateView() {
        // Set up all the coordinates
        val min = points.min() ?: return // Return if null
        val max = points.max() ?: return
        val heightGraph = height - bottomOffset
        val gap = max - min
        val percentX = ((width - left) / 6).toFloat()
        val aY = (((max - points[0]) / gap) * heightGraph) + topOffset
        val bY = (((max - points[1]) / gap) * heightGraph) + topOffset
        val cY = (((max - points[2]) / gap) * heightGraph) + topOffset
        val dY = (((max - points[3]) / gap) * heightGraph) + topOffset
        val eY = (((max - points[4]) / gap) * heightGraph) + topOffset
        val fY = (((max - points[5]) / gap) * heightGraph) + topOffset
        val gY = (((max - points[6]) / gap) * heightGraph) + topOffset
        // Add calculated coordinates to the array used for drawing
        coordinates = floatArrayOf(
                left.toFloat(), aY, percentX * 1, bY,
                percentX * 1, bY, percentX * 2, cY,
                percentX * 2, cY, percentX * 3, dY,
                percentX * 3, dY, percentX * 4, eY,
                percentX * 4, eY, percentX * 5, fY,
                percentX * 5, fY, percentX * 6, gY
        )
        setupPaint()
    }

    /** Set-up attributes of paint */
    private fun setupPaint(change: Int = 73) {
        paintLine.strokeWidth = lineWidth
        paintLine.isAntiAlias = true
        paintLine.strokeCap = Paint.Cap.ROUND
        paintLine.setARGB(alphaValue, redValue - change, greenValue - change, blueValue + change)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) =
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec))

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        updateView() // Update parameters only after correct height and width are known
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawLines(coordinates, paintLine)
    }
}