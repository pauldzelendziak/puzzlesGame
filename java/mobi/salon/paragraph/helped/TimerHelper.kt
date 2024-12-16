package mobi.salon.paragraph.helped

import android.os.Handler
import android.os.Looper
import android.widget.TextView

class TimerHelper {

    private var timeInSeconds = 0
    private var handler = Handler(Looper.getMainLooper())
    private var isRunning = false
    private lateinit var timerRunnable: Runnable

    fun startTimer(textView: TextView) {
        isRunning = true
        timerRunnable = object : Runnable {
            override fun run() {
                if (isRunning) {
                    timeInSeconds++
                    updateTimerUI(textView)
                    handler.postDelayed(this, 1000) // Оновлення кожну секунду
                }
            }
        }
        handler.post(timerRunnable)
    }

    fun stopTimer() {
        isRunning = false
        handler.removeCallbacks(timerRunnable)
    }

    private fun updateTimerUI(textView: TextView) {
        val minutes = timeInSeconds / 60
        val seconds = timeInSeconds % 60
        textView.text = String.format("%02d:%02d", minutes, seconds)
    }

    fun resetTimer(textView: TextView) {
        stopTimer()
        timeInSeconds = 0
        updateTimerUI(textView)
    }
}
