package com.example.platesdetector

import android.app.Activity
import android.graphics.*
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.Button

class SurfaceDrawingExample : Activity() {

    lateinit var surfaceView: SurfaceView
    lateinit var holder: SurfaceHolder

    val ball = Paint().apply { color = Color.RED }
    val radius = 105f
    var x = 500f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_first)
        surfaceView = findViewById(R.id.surfaceView)
        holder = surfaceView.holder


        findViewById<Button>(R.id.button_left).setOnClickListener {
            x -= 100
            paint()
        }

        findViewById<Button>(R.id.button_right).setOnClickListener {
            x += 100
            paint()
        }

        holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                surfaceView.setWillNotDraw(false)
                val canvas = holder.lockCanvas()
                val destRect = Rect(0,0, canvas.width, canvas.height)
                val bmp = BitmapFactory.decodeResource(resources, R.drawable.peso_muerto_imagen)
                canvas.drawBitmap(bmp, null, destRect, null)
                holder.unlockCanvasAndPost(canvas)
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
                println("Pos of X $x")
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {

            }

        })
    }

    private fun setCircle() {
        val paint = Paint().apply { color = Color.RED }
        val canvas = holder.lockCanvas(null)
        canvas.drawCircle(x, 500f, radius, paint)
        holder.unlockCanvasAndPost(canvas)
    }

    private fun paint() {
        surfaceView.invalidate()
        val canvas = holder.lockCanvas(null)
        canvas.drawColor(Color.BLACK)
        canvas.drawCircle(x, 500f, radius, ball)

        holder.unlockCanvasAndPost(canvas)
    }

}