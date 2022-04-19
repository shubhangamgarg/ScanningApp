package `in`.mowito.scanningapp.overlays

import `in`.mowito.scanningapp.R
import `in`.mowito.scanningapp.models.PointModel
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat

/**
 * @author Shubhangam Garg
 * Custom view to show random points over Camera Feed*/
class PointsOverlayView constructor (context : Context?, attributeSet : AttributeSet?)
    : View(context, attributeSet){
    private val  circlePoints :MutableList<PointModel> = mutableListOf()
    private val paint = Paint().apply {
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context!!,R.color.blue)

    }
    private val radius = 15f;

    override fun onDraw(canvas: Canvas){
        super.onDraw(canvas)
        circlePoints.forEach {
                canvas.drawCircle(it.point.x.toFloat(), it.point.y.toFloat(),radius, it.paint)
        }

    }
    /**
     * Adds all given points to [circlePoints]
     * @param points List of [PointModel]
     * @return Unit
     * */
    fun drawPoints(points : List<PointModel>){
        this.circlePoints.clear()
        this.circlePoints.addAll(points)
        invalidate()
    }
    /**
     * Resets all color of points in [circlePoints] and re-draws
     * @return Unit
     * */
    fun restPointsToOriginalColor(){
        this.circlePoints.forEach {
            it.paint = paint
        }
        invalidate()
    }
    /**
     * Removes all  points in [circlePoints] and re-draws
     * @return Unit
     * */
    fun removePoints(){
        this.circlePoints.clear()
        invalidate()
    }
    fun getPoints() : List<PointModel>{
        return this.circlePoints
    }


}