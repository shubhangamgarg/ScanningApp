package `in`.mowito.scanningapp.util

import `in`.mowito.scanningapp.R
import `in`.mowito.scanningapp.models.PointModel
import `in`.mowito.scanningapp.models.PolygonDetailsModel
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.util.Log
import androidx.core.content.ContextCompat
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.tan
/**
 * @author Shubhangam Garg
 * Handles all calculation related to drawing*/

class AppUtil {
    companion object{
        /**
         * Generate random points for a given screen co-ordinate bounds
         * @param pair Max Co-Ordinate Value (x,y)
         * @param context application context
         * @return List<PointModel>
         * @see PointModel*/
        fun getPointsCoordinates (pair : Pair<Int, Int>,context : Context) :  List<PointModel>{
            val paint = Paint().apply {
                style = Paint.Style.FILL
                color = ContextCompat.getColor(context, R.color.blue)
            }
            val numberOfPoints =  Random(System.currentTimeMillis()).nextInt(15)
            Log.d("PointCo-OrdinateCount","$numberOfPoints")
            val listOfPoints   = mutableListOf<PointModel>()
            for(i in 1..numberOfPoints) {
                val seedSuffix = System.currentTimeMillis() %100
                val x = Random(i*100 + seedSuffix).nextInt(pair.first)
                val y = Random(i*100 - seedSuffix).nextInt(pair.first)
                listOfPoints.add(PointModel(point= Point(x,y),paint= paint))
                Log.d("Point Co-Ordinate","$x  $y  index $i")
            }
            return listOfPoints
        }

        /**
         * Generates  no. of sides and circumcenter of a regular polygon
         * @param maxCoOrdinate Max Co-Ordinate Value (x,y)
         * @return Pair<Int, Point>
         * */
        fun getPolygonDetail(maxCoOrdinate : Pair<Int, Int>) : Pair<Int, Point> {
            val seedSuffix = System.currentTimeMillis() %100
            val sides = Random(System.currentTimeMillis()%100).nextInt(15)
            val x = Random(maxCoOrdinate.first/2.toLong()).nextInt(maxCoOrdinate.first)
            val y = Random(maxCoOrdinate.second/2.toLong()).nextInt(maxCoOrdinate.second)
            Log.d("PolygonCentCo-Ordinate","$x  $y")
            return Pair(sides,Point(x,y))
        }
        /**
         * Checks whether a point lies inside the given polygon or not
         * @param pointsList list points drawn randomly on the view
         * @param polygonDetail details of drawn polygon on the view
         * @return List<PointModel>
         * @see PointModel
         * @see PolygonDetailsModel*/
        fun checkForPointsInsidePolygon(pointsList : List<PointModel>, polygonDetail : PolygonDetailsModel)
        : List<PointModel> {
            val modifiedPointsList = mutableListOf<PointModel>()
            //Yellow Paint
            val paint = Paint().apply {
                style = Paint.Style.FILL
                color = Color.YELLOW
            }

            //Calculate area of the regular polygon
            val apothem = polygonDetail.sideLength / (2*tan((PI/polygonDetail.sides)))
            val area = roundOff((polygonDetail.sides * polygonDetail.sideLength * apothem) / 2.0).toDouble()

            pointsList.forEach{
                val pointC = it.point
                var totalCalculatedArea = 0.0
                //Calculate area in accordance with polygon and given random point
                for (i in 0 until polygonDetail.points.size-1){
                    val pointA = polygonDetail.points[i]
                    val pointB = polygonDetail.points[i+1]
                    totalCalculatedArea += abs(0.5 * ((pointA.x*(pointB.y - pointC.y))+(pointB.x*(pointC.y - pointA.y))+(pointC.x*(pointA.y- pointB.y))))
//                    modifiedPointsList.add(PointModel(Point(pointA.x,pointA.y),paint))
                }
                val pointA = polygonDetail.points[0]
                val pointB = polygonDetail.points[polygonDetail.points.size-1]
                totalCalculatedArea += abs(0.5 * ((pointA.x*(pointB.y - pointC.y))+(pointB.x*(pointC.y - pointA.y))+(pointC.x*(pointA.y- pointB.y))))
                totalCalculatedArea = roundOff(totalCalculatedArea).toDouble()
                Log.d("PolygonInsidePoints", "Added Area : $totalCalculatedArea")
                Log.d("PolygonInsidePoints", "Total Area : $area")

                //Compare area
                if(area == totalCalculatedArea){
                    Log.d("PolygonInsidePoints", "Point found")
                    it.paint = paint
                }
                modifiedPointsList.add(it)
            }

            return modifiedPointsList
        }
        /**
         * Rounds off a Double value to nearest thousand
         * @param number a Double value
         * @return Int
         * */
        private fun roundOff(number : Double) : Int{
            val intNumber = number.toInt()
            return ((intNumber + 999)/1000) * 1000
        }
    }

}