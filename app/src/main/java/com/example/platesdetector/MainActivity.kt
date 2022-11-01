package com.example.platesdetector

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.SurfaceView
import android.view.View
import android.view.Window
import android.view.WindowManager
import org.opencv.android.BaseLoaderCallback
import org.opencv.android.CameraBridgeViewBase
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2
import org.opencv.android.LoaderCallbackInterface
import org.opencv.android.OpenCVLoader
import org.opencv.core.*
import org.opencv.imgproc.Imgproc

class MainActivity : Activity(), CvCameraViewListener2 {
    private var mIsColorSelected = false
    private var mRgba: Mat? = null
    private var mBlobColorRgba: Scalar? = null
    private var mBlobColorHsv: Scalar? = null
    private var mSpectrum: Mat? = null
    private var SPECTRUM_SIZE: Size? = null
    private var CONTOUR_COLOR: Scalar? = null
    private var mOpenCvCameraView: CameraBridgeViewBase? = null
    private val mLoaderCallback: BaseLoaderCallback = object : BaseLoaderCallback(this) {
        override fun onManagerConnected(status: Int) {
            when (status) {
                SUCCESS -> {
                    Log.i(TAG, "OpenCV loaded successfully")
                    mOpenCvCameraView!!.enableView()
                    mOpenCvCameraView!!.setCameraPermissionGranted()
                }
                else -> {
                    super.onManagerConnected(status)
                }
            }
        }
    }

    /** Called when the activity is first created.  */
    public override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "called onCreate")
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContentView(R.layout.color_blob_detection_surface_view)
        mOpenCvCameraView =
            findViewById<View>(R.id.color_blob_detection_activity_surface_view) as CameraBridgeViewBase
        mOpenCvCameraView!!.visibility = SurfaceView.VISIBLE
        mOpenCvCameraView!!.setCvCameraViewListener(this)
    }

    public override fun onPause() {
        super.onPause()
        if (mOpenCvCameraView != null) mOpenCvCameraView!!.disableView()
    }

    public override fun onResume() {
        super.onResume()
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization")
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback)
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!")
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS)
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
        if (mOpenCvCameraView != null) mOpenCvCameraView!!.disableView()
    }

    override fun onCameraViewStarted(width: Int, height: Int) {
        mRgba = Mat(height, width, CvType.CV_8UC4)
        mSpectrum = Mat()
        mBlobColorRgba = Scalar(255.0)
        mBlobColorHsv = Scalar(255.0)
        SPECTRUM_SIZE = Size(200.0, 64.0)
        CONTOUR_COLOR = Scalar(255.0, 0.0, 0.0, 255.0)
    }

    override fun onCameraViewStopped() {
        mRgba!!.release()
    }


    override fun onCameraFrame(inputFrame: CvCameraViewFrame): Mat {
        mRgba = inputFrame.rgba()
        CirclesDetector.drawBoxesOnTheImage(mRgba!!)
        return mRgba!!
    }

    private fun converScalarHsv2Rgba(hsvColor: Scalar?): Scalar {
        val pointMatRgba = Mat()
        val pointMatHsv = Mat(1, 1, CvType.CV_8UC3, hsvColor)
        Imgproc.cvtColor(pointMatHsv, pointMatRgba, Imgproc.COLOR_HSV2RGB_FULL, 4)
        return Scalar(pointMatRgba[0, 0])
    }

    companion object {
        private const val TAG = "MainActivity"
    }

    init {
        Log.i(TAG, "Instantiated new " + this.javaClass)
    }

}