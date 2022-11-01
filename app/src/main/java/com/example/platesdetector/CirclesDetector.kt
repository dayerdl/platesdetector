package com.example.platesdetector

import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.core.Scalar
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc

object CirclesDetector {

    fun drawBoxesOnTheImage(img: Mat) {
        val gray = Mat()
        Imgproc.cvtColor(img, gray, Imgproc.COLOR_BGR2GRAY)
        Imgproc.blur(gray, gray, Size(3.0, 3.0))

        val edges = Mat()
        val lowThreshold = 40
        val ratio = 3
        Imgproc.Canny(gray, edges, lowThreshold.toDouble(), (lowThreshold * ratio).toDouble())

        val circles = Mat()

        Imgproc.HoughCircles(edges, circles, Imgproc.CV_HOUGH_GRADIENT, 1.0, 60.0, 200.0, 20.0, 30, 100)

        //println("#rows " + circles.rows() + " #cols " + circles.cols())
        var x = 0.0
        var y = 0.0
        var r = 0

        for (i in 0 until circles.rows()) {
            val data = circles[i, 0]
            for (j in data.indices) {
                x = data[0]
                y = data[1]
                r = data[2].toInt()
            }
            val center = Point(x, y)
            // circle outline
            Imgproc.circle(img, center, r, Scalar(0.0, 0.0, 255.0), 1)
        }
    }

    fun drawBoxesOnTheImage2(img: Mat) {
        val gray = Mat()
        Imgproc.cvtColor(img, gray, Imgproc.COLOR_BGR2GRAY)
        Imgproc.blur(gray, gray, Size(3.0, 3.0))

        val edges = Mat()
        val lowThreshold = 40
        val ratio = 3
        Imgproc.Canny(gray, edges, lowThreshold.toDouble(), (lowThreshold * ratio).toDouble())

        val circles = Mat()

        Imgproc.HoughCircles(edges, circles, Imgproc.CV_HOUGH_GRADIENT, 1.0, 60.0, 200.0, 20.0, 30, 100)

        //println("#rows " + circles.rows() + " #cols " + circles.cols())
        var x = 0.0
        var y = 0.0
        var r = 0

        for (i in 0 until circles.rows()) {
            val data = circles[i, 0]
            for (j in data.indices) {
                x = data[0]
                y = data[1]
                r = data[2].toInt()
            }
            val center = Point(x, y)
            // circle outline
            Imgproc.circle(img, center, r, Scalar(0.0, 0.0, 255.0), 1)
        }
    }
}