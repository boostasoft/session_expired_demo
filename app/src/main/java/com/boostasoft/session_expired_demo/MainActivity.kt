package com.boostasoft.session_expired_demo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.Editable
import android.util.Log
import com.boostasoft.session_expired_demo.databinding.ActivityMainBinding
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.floor


class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null

    private var sessionHandler: Handler? = null
    private var sessionRunnable: Runnable? = null

    private var countDownHandler: Handler? = null

    private val SESSION_TIMEOUT = 10*1000L // 10 secondes

    private var sessionIsClosed = false // Eviter le reset supplémentaire
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setupSession()
        startSession()
    }
    private fun setupSession(){
        sessionHandler = Handler(Looper.getMainLooper())
        sessionRunnable = SessionHandlerRunnable(context = this){
            sessionIsClosed = true
            closeSession()
            finish()
        }
        countDownHandler = CountDownHandler(mainLooper, binding!!, SESSION_TIMEOUT)
        countDownHandler?.sendEmptyMessage(CountDownHandler.DECREASE_COUNTER)
    }
    private fun startSession(){
        sessionHandler?.postDelayed(sessionRunnable!!, SESSION_TIMEOUT)
    }
    private fun closeSession(){
        sessionHandler?.removeCallbacks(sessionRunnable!!)
    }
    private fun resetSession(){
        closeSession()
        countDownHandler?.sendEmptyMessage(CountDownHandler.RESET_COUNTER)
        Log.d(this::class.java.toString(), "Reset Fired")
        startSession()
    }
    override fun onUserInteraction() {
        super.onUserInteraction()
        if(!sessionIsClosed)
            resetSession()
    }

}
class SessionHandlerRunnable(private val context: Context, private val  onFired: () -> Unit) : Runnable{
    override fun run() {
        val intent = Intent(context, MyLoginActivity::class.java)
        context.startActivity(intent)
        Log.d(this::class.java.toString(), "fired")
        this.onFired()
    }

}

class CountDownHandler(looper: Looper, val viewBinding: ActivityMainBinding, var maxTimes: Long): Handler(looper){
    private val remainTime = AtomicInteger(maxTimes.toSecond())
    companion object{
        const val DECREASE_COUNTER = 6045
        const val RESET_COUNTER = 6048
    }
    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
        when(msg.what){
            DECREASE_COUNTER -> {

                if(remainTime.decrementAndGet() >= 0){
                    viewBinding.countDownText.text = remainTime.get().toString().toEditable()
                    this.sendEmptyMessageDelayed(DECREASE_COUNTER, 1000)
                }
            }
            RESET_COUNTER -> {
                reset()
            }
        }
    }
    public fun reset(): Unit{
        this.remainTime.set(maxTimes.toSecond())
    }

}


fun Long.toSecond(): Int = floor((this/1000).toDouble()).toInt()
fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)