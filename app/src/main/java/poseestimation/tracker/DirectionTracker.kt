package poseestimation.tracker

import android.graphics.PointF

class DirectionTracker {

    private val points = arrayListOf<PointF>()
    var currentDirection = DIRECTION.NOT_MOVING

    fun calculateDirection(point: PointF): DIRECTION {
        println("ZAXA point Y of shoulder is ${point.y}")
        points.add(point)
        //println("ZAXA Points are ${points.toList()}")

        while (points.size <= 4 ) { // we cant detect movement with less than 4 points
            currentDirection = DIRECTION.NOT_MOVING
            return currentDirection
        }

        val d1 = points[1].y - points[0].y //if d > 0 then is UP
        val d2 = points[2].y - points[1].y
        val d3 = points[3].y - points[2].y

        val average = (d1 + d2 + d3) / 3
        val threshold = 10
        if (average < threshold) {
            currentDirection = DIRECTION.UP
        } else if (average > threshold) {
            currentDirection = DIRECTION.DOWN
        }

        points.removeAt(0)

        return currentDirection

    }

    enum class DIRECTION { UP, DOWN, NOT_MOVING }
}