package com.example.platesdetector

import android.app.Activity
import android.os.Bundle
import org.opencv.videoio.VideoCapture


class OpenCVActivity: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.fragment_first)


        val cap = VideoCapture("peso-muerto.mp4")
        cap.isOpened

    }
}