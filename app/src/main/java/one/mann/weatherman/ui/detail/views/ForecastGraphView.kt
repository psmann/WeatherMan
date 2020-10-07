package one.mann.weatherman.ui.detail.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

internal class ForecastGraphView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val alphaValue = 73 // Alpha for paintLine
        private const val redValue = 255 // Red value for paintLine
        private const val greenValue = 231 // Green value for paintLine
        private const val blueValue = 231 // Blue value for paintLine
        private const val lineWidth = 37f // Width of line
        private const val bottomOffset = lineWidth // Bottom offset to remove cropping of line
        private const val topOffset = lineWidth / 2 // Top offset to remove cropping of line
    }

    private val paintLine = Paint()
    private lateinit var coordinates: FloatArray
    private var points = listOf(0f, 0f, 0f, 0f, 0f, 0f, 0f)

    /** Set input points for the graph */
    fun setPoints(pts: List<Float>) {
        points = pts
    }

    /** Calculates coordinates for lines in the graph using seven given input points (for six lines) */
    private fun updateView() {
        // Set up all the coordinates
        val min = points.minOrNull() ?: return // Minimum value in the list, return if null
        val max = points.maxOrNull() ?: return // Maximum value in the list, return if null
        val heightGraph = height - bottomOffset // Height of the graph
        val gap = max - min // Used as a metric to calculate percentages within its scope
        val line1StartX = left.toFloat() // startX coordinate of line 1
        val line1StartY = (((max - points[0]) / gap) * heightGraph) + topOffset // startY coordinate of line 1
        val line2StartY = (((max - points[1]) / gap) * heightGraph) + topOffset // startY coordinate of line 2
        val line3StartY = (((max - points[2]) / gap) * heightGraph) + topOffset // startY coordinate of line 3
        val line4StartY = (((max - points[3]) / gap) * heightGraph) + topOffset // startY coordinate of line 4
        val line5StartY = (((max - points[4]) / gap) * heightGraph) + topOffset // startY coordinate of line 5
        val line6StartY = (((max - points[5]) / gap) * heightGraph) + topOffset // startY coordinate of line 6
        val line6EndY = (((max - points[6]) / gap) * heightGraph) + topOffset // endY coordinate of line 6
        val percentX = (width - line1StartX) / 6 // Width of graph divided into 6 equal segments (startX for lines)

        // Add calculated coordinates to the array used for drawing
        coordinates = floatArrayOf(
                line1StartX, line1StartY, percentX * 1, line2StartY, // Line 1
                percentX * 1, line2StartY, percentX * 2, line3StartY, // Line 2
                percentX * 2, line3StartY, percentX * 3, line4StartY, // Line 3
                percentX * 3, line4StartY, percentX * 4, line5StartY, // Line 4
                percentX * 4, line5StartY, percentX * 5, line6StartY, // Line 5
                percentX * 5, line6StartY, percentX * 6, line6EndY // Line 6
        )
        setupPaint()
    }

    /** Set up attributes for paint */
    private fun setupPaint() {
        paintLine.strokeWidth = lineWidth
        paintLine.strokeCap = Paint.Cap.ROUND
        paintLine.setARGB(alphaValue, redValue, greenValue, blueValue)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) =
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec))

    /** Update parameters only after correct height and width are known */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        updateView()
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawLines(coordinates, paintLine)
    }
}