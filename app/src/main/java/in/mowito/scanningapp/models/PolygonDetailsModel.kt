package `in`.mowito.scanningapp.models

import android.graphics.Point

data class PolygonDetailsModel(
    val sides : Int,
    val sideLength : Double,
    val points : List<Point>
)
