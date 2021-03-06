package com.ibrajix.timerapp

import android.graphics.Color
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.ibrajix.timerapp.databinding.ActivityMainBinding
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size
import kotlin.math.min

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var tag: String? = null
    private var paused: Boolean = true
    private var isRunning: Boolean = false

    //start time in milliseconds
    var startTime = 60000L
    lateinit var countDownTimer: CountDownTimer
    var timeInMilliSeconds = 0L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)

        //make status bar transparent
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            window.decorView.systemUiVisibility =
                (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        } else {
            window.setDecorFitsSystemWindows(false)
        }

        initViews()
    }

    private fun initViews() {

        tag = binding.imgPause.tag.toString()

        binding.button.setOnClickListener {

            if (isRunning){
                pauseTimer()
            }
            else{
                val time = 60.toLong() * 60000L
                startTimer(time)
                binding.button.visibility = View.GONE
            }

        }

        binding.imgClose.setOnClickListener {

            resetTimer()

        }

        //on click of pause
        binding.imgPause.setOnClickListener {

            if (isRunning) {

                binding.imgPause.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_play
                    )
                )

                pauseTimer()

                isRunning = false

            } else {
                binding.imgPause.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_pause
                    )
                )

                resumeTimer()

                isRunning = true
            }

        }


    }

    private fun resetTimer(){

        timeInMilliSeconds = startTime
        updateTextUiZero()
        binding.imgClose.visibility = View.GONE

    }

    private fun pauseTimer(){

        //pause the timer
        countDownTimer.cancel()
        isRunning = false

        //set reset/close visibility true
        binding.imgClose.visibility = View.VISIBLE


    }

    private fun resumeTimer(){


        //resume timer
        countDownTimer = object : CountDownTimer(timeInMilliSeconds, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                timeInMilliSeconds = millisUntilFinished
                updateTextUi()

            }

            override fun onFinish() {
                loadOthers()

            }

        }

        //start countdown
        countDownTimer.start()

        //set running to true
        isRunning = true




    }


    private fun startTimer(time: Long) {

        //set the hr to zero
        binding.txtHrs.text = "00:"

        countDownTimer = object : CountDownTimer(time, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                timeInMilliSeconds = millisUntilFinished
                updateTextUi()

            }

            override fun onFinish() {
                loadOthers()

            }

        }

        //start countdown
        countDownTimer.start()

        //set running to true
        isRunning = true

        //set button text to pause
        binding.button.text = applicationContext.getString(R.string.pause)

        //set reset visibility to gone
        binding.imgClose.visibility = View.GONE

    }


    /**
     * Update text view, set required seconds, minutes and hour
     */

    private fun updateTextUi() {

        val minutes = (timeInMilliSeconds / 1000) / 60
        val seconds = (timeInMilliSeconds / 1000) % 60

        //set the text view
        binding.txtMin.text = minutes.toString()
        binding.txtSecs.text = seconds.toString()


    }

    private fun updateTextUiZero(){

        binding.txtHrs.text = "00:"
        binding.txtMin.text = "00:"
        binding.txtSecs.text = "00"

    }


    //show view after countdown elapses
    private fun loadOthers() {

        binding.viewKonfetti.build()
            .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
            .setDirection(0.0, 359.0)
            .setSpeed(1f, 5f)
            .setFadeOutEnabled(true)
            .setTimeToLive(2000L)
            .addShapes(Shape.Square, Shape.Circle)
            .addSizes(Size(12))
            .setPosition(-50f, -50f, -50f)
            .streamFor(300, 5000L)
    }

}
