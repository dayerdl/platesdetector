package com.example.platesdetector

import org.opencv.core.*
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

        Imgproc.HoughCircles(
            edges,
            circles,
            Imgproc.CV_HOUGH_GRADIENT,
            1.0,
            60.0,
            200.0,
            20.0,
            30,
            100
        )

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
        val blur = Mat()
        val thresh = Mat()
        Imgproc.cvtColor(img, gray, Imgproc.COLOR_BGR2GRAY)
        Imgproc.medianBlur(gray, blur, 11)
        Imgproc.threshold(blur, thresh, 0.0, 255.0, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU)

        val opening = Mat()
        val kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, Size(5.0,5.0))
        Imgproc.morphologyEx(thresh, opening, Imgproc.MORPH_OPEN, kernel)

        val hierarchy = Mat()
        val contours = arrayListOf<MatOfPoint>()

        Imgproc.findContours(
            opening,
            contours,
            hierarchy,
            Imgproc.RETR_EXTERNAL,
            Imgproc.CHAIN_APPROX_SIMPLE
        )

        if(contours.size == 0) {
            return
        } else {
        }

        var cnts = contours[0].toArray()

        if (contours.size > 2) {
            cnts = contours[1].toArray()
        }

        for (c in cnts) {
            val matOfPoint = MatOfPoint2f(c)
            val peri = Imgproc.arcLength(matOfPoint, true)
            if (peri > 0) {
            }
            val approx = MatOfPoint2f()
            val area = Imgproc.contourArea(matOfPoint)
            Imgproc.approxPolyDP(matOfPoint, approx, 0.04 * peri, true)
            if (approx.toArray().size > 5 && area > 1000 && area < 500000) {
                val center = Point()
                val radius = FloatArray(1)
                Imgproc.minEnclosingCircle(matOfPoint, center, radius)
                Imgproc.circle(img, center, radius[0].toInt(), Scalar(0.0, 0.0, 255.0), 1)
            }
        }
    }
}