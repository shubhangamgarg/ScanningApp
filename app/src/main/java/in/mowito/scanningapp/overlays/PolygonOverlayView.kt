package `in`.mowito.scanningapp.overlays


import `in`.mowito.scanningapp.R
import `in`.mowito.scanningapp.models.PolygonDetailsModel
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import java.lang.Math.pow
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Creates and Displays polygon over captured image
 * @author Shubhangam Garg
 *  */
class PolygonOverlayView constructor(context: Context?, attributeSet: AttributeSet?) :
    View(context, attributeSet) {
    private val paint = Paint().apply {
        style = Paint.Style.STROKE
        color = ContextCompat.getColor(context!!, R.color.blue)
        strokeWidth = 4f
    }
    private val radius = 200

    private var path = Path()
    private var point = Point(0, 0)


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.translate(point.x.toFloat(), point.y.toFloat())
        canvas.drawPath(path, paint)
    }
    /**
     * Draws a regular polygon by considering the plain as POLAR PLAIN
     * @param sides No. of sides of a polygon
     * @param point Circum-center of the polygon
     * @return [PolygonDetailsModel] Attributes of polygon created
     * @see PolygonDetailsModel*/
    fun drawPolygon(sides: Int, point: Point): PolygonDetailsModel {
        path = Path()
        val points = mutableListOf<Point>()
        val a: Float = Math.PI.toFloat() * 2 / sides * 1

        //Calculating side length
        val x = radius * cos((a * 1).toDouble())
        val y = radius * sin((a * 1).toDouble())
        val sideLength = sqrt(pow((radius - x), 2.0) + pow(y, 2.0))

        //Drawing a regular polygon
        path.moveTo(radius.toFloat(), 0f)
        points.add(Point(point.x + radius, point.y))
        for (i in 1 until sides) {
            val x1 = radius * cos((a * i).toDouble()).toFloat()
            val y1 = radius * sin((a * i).toDouble()).toFloat()
            path.lineTo(x1, y1)
            points.add(Point((x1+point.x).toInt(), (y1+point.y).toInt()))
        }
        path.close()
        this.point = point
        invalidate()
        Log.d("Polygon", "Sides : $sides")
        return PolygonDetailsModel(sides = sides, sideLength = sideLength, points = points)
    }
    /**
     * Removes polygon from the view
     * @return Unit*/
    fun removePolygon() {
        path = Path()
        point = Point(0, 0)
        invalidate()

    }

}