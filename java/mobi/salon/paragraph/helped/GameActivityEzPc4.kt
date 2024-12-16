package mobi.salon.paragraph.helped

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.DriverManager

class GameActivityEzPc4 : AppCompatActivity() {
    private lateinit var serviceIntent: Intent
    private lateinit var musicService: MusicService
    private var isActivityCheck = true
    private var isMusicOn = false
    private lateinit var puzzlesOnDeskp4ez: Array<ImageView>
    private lateinit var puzzlesGrp4ez: Array<ImageView>
    private lateinit var hintEzPc4: ImageButton
    private lateinit var btnWithTp: ImageButton
    private lateinit var btnWithoutTp: ImageButton
    private lateinit var pauseBgEzPc4: ImageView
    private lateinit var counterEzPc4: TextView
    private lateinit var textChooseM1EzPc4: TextView
    private lateinit var textChooseM2EzPc4: TextView
    private lateinit var timerEzPc4: TextView
    private val timerHelper = TimerHelper()
    private var previousX: Float = 0f
    private var previousY: Float = 0f
    private var score: Int = 0
    private var startTime: Long = 0
    private var endTime: Long = 0
    private var recivedData : Int = -1
    ////////
    private lateinit var bgImageView: ImageView
    private lateinit var sharedPrefsBg: SharedPreferences
    private val bgColors = arrayOf(
        R.drawable.bg,R.drawable.bgch1,R.drawable.bgch2,R.drawable.bgch3,R.drawable.bgch4,
        R.drawable.bgch5,R.drawable.bgch6,R.drawable.bgch7,R.drawable.bgch8,
    )
    ////////
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_ez_pc4)
        hideUi()
        ////////
        bgImageView = findViewById(R.id.bgActGameEzPc4)
        val sharedPreferencesForCurrentUser = getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val UserData = sharedPreferencesForCurrentUser.getString("currentData", "")
        sharedPrefsBg = getSharedPreferences("BgPrefs", Context.MODE_PRIVATE)
        val bgIndex = sharedPrefsBg.getInt("bgIndex${UserData}", 0)
        bgImageView.setImageResource(bgColors[bgIndex])
        //////////
        recivedData = intent.getIntExtra("extra_data", -1)
        val click = MediaPlayer.create(this, R.raw.click)
        serviceIntent = Intent(this, MusicService::class.java)
        bindService(serviceIntent, musicConnection, BIND_AUTO_CREATE)
        val backBtn: ImageButton = findViewById(R.id.backEzPc4)
        hintEzPc4 = findViewById(R.id.hintEzPc4)
        hintEzPc4.isEnabled = false
        counterEzPc4 = findViewById(R.id.counterEzPc4)

        //change
        backBtn.setOnClickListener {
            click.start()
            val intent = Intent(this, ChooseActivityLvl1::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
        val sharedPreferencesForBrigthness = getSharedPreferences("Brigthness", Context.MODE_PRIVATE)
        val Brigthness = sharedPreferencesForBrigthness.getFloat("brigthnessValue", 1f)
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val layoutParams = window.attributes
        textChooseM1EzPc4 = findViewById(R.id.textChooseM1EzPc4)
        textChooseM2EzPc4 = findViewById(R.id.textChooseM2EzPc4)
        layoutParams.screenBrightness = Brigthness
        window.attributes = layoutParams
        puzzlesOnDeskp4ez = arrayOf(
            findViewById(R.id.ez_p4_im1),
            findViewById(R.id.ez_p4_im2),
            findViewById(R.id.ez_p4_im3),
            findViewById(R.id.ez_p4_im4),
            findViewById(R.id.ez_p4_im5),
            findViewById(R.id.ez_p4_im6),
            findViewById(R.id.ez_p4_im7),
            findViewById(R.id.ez_p4_im8),
            findViewById(R.id.ez_p4_im9),
            findViewById(R.id.ez_p4_im10),
            findViewById(R.id.ez_p4_im11),
            findViewById(R.id.ez_p4_im12),
            findViewById(R.id.ez_p4_im13),
            findViewById(R.id.ez_p4_im14),
            findViewById(R.id.ez_p4_im15),
            findViewById(R.id.ez_p4_im16),
            findViewById(R.id.ez_p4_im17),
            findViewById(R.id.ez_p4_im18),
            findViewById(R.id.ez_p4_im19),
            findViewById(R.id.ez_p4_im20),
            findViewById(R.id.ez_p4_im21),
            findViewById(R.id.ez_p4_im22),
            findViewById(R.id.ez_p4_im23),
            findViewById(R.id.ez_p4_im24),
        )
        puzzlesGrp4ez = arrayOf(
            findViewById(R.id.ez_p4_imgr1),
            findViewById(R.id.ez_p4_imgr2),
            findViewById(R.id.ez_p4_imgr3),
            findViewById(R.id.ez_p4_imgr4),
            findViewById(R.id.ez_p4_imgr5),
            findViewById(R.id.ez_p4_imgr6),
            findViewById(R.id.ez_p4_imgr7),
            findViewById(R.id.ez_p4_imgr8),
            findViewById(R.id.ez_p4_imgr9),
            findViewById(R.id.ez_p4_imgr10),
            findViewById(R.id.ez_p4_imgr11),
            findViewById(R.id.ez_p4_imgr12),
            findViewById(R.id.ez_p4_imgr13),
            findViewById(R.id.ez_p4_imgr14),
            findViewById(R.id.ez_p4_imgr15),
            findViewById(R.id.ez_p4_imgr16),
            findViewById(R.id.ez_p4_imgr17),
            findViewById(R.id.ez_p4_imgr18),
            findViewById(R.id.ez_p4_imgr19),
            findViewById(R.id.ez_p4_imgr20),
            findViewById(R.id.ez_p4_imgr21),
            findViewById(R.id.ez_p4_imgr22),
            findViewById(R.id.ez_p4_imgr23),
            findViewById(R.id.ez_p4_imgr24),
        )
        puzzlesOnDeskp4ez.forEach { puzzle ->
            puzzle.setOnTouchListener(null)
        }
        hintEzPc4.setOnClickListener {
            if (score >= 4) {
                score -= 4
                counterEzPc4.text = score.toString()
                showHintImage()
                hintEzPc4.isEnabled = false
            } else {
            }
        }
        startTime = System.currentTimeMillis()
        pauseBgEzPc4 = findViewById(R.id.foneEzPc4)
        btnWithTp = findViewById(R.id.btn_WithTpEzPc4)
        btnWithoutTp = findViewById(R.id.btn_WithoutTpEzPc4)
        timerEzPc4 = findViewById(R.id.timer_Ez_pc4)
        ChooseMode()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun spawnRandomObjects() {
        puzzlesOnDeskp4ez.forEach { puzzle ->
            puzzle.setOnTouchListener { view, motionEvent ->
                handleTouch(view, motionEvent)
            }
        }
    }
    private fun spawnRandomObjectsWithoutTp() {
        puzzlesGrp4ez.forEach { puzzle ->
            puzzle.setOnTouchListener { view, motionEvent ->
                handleTouchWithoutTp(view, motionEvent)
            }
        }
    }
    private fun handleTouch(view: View, motionEvent: MotionEvent): Boolean {
        val vegetables = view as ImageView
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                previousX = vegetables.x - motionEvent.rawX
                previousY = vegetables.y - motionEvent.rawY
            }
            MotionEvent.ACTION_MOVE -> {
                vegetables.animate()
                    .x(motionEvent.rawX + previousX)
                    .y(motionEvent.rawY + previousY)
                    .setDuration(0)
                    .start()

                puzzlesOnDeskp4ez.forEach { deskPuzzle ->
                    puzzlesGrp4ez.forEach { grPuzzle ->
                        if (deskPuzzle.contentDescription == grPuzzle.contentDescription) {
                            val deskPuzzleId = deskPuzzle.contentDescription.toString()
                            if (isCollision(deskPuzzle, grPuzzle) && collisionFlags[deskPuzzleId] != true) {
                                // Співпадіння виявлено
                                deskPuzzle.isVisible = false
                                deskPuzzle.isEnabled = false
                                grPuzzle.isVisible = true
                                collisionFlags[deskPuzzleId] = true

                                // Нарахування балів
                                score += 2
                                counterEzPc4.text = score.toString()

                                if (score >= 10) {
                                    hintEzPc4.isEnabled = true
                                }

                                // Перевірка завершення гри
                                if (isGameCompleted()) {
                                    timerHelper.stopTimer()
                                }
                            }
                        }
                    }
                }
            }
        }
        return true
    }

    private val collisionFlags = mutableMapOf<String, Boolean>()

    private fun handleTouchWithoutTp(view: View, motionEvent: MotionEvent): Boolean {
        val vegetables = view as ImageView
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                previousX = vegetables.x - motionEvent.rawX
                previousY = vegetables.y - motionEvent.rawY
            }
            MotionEvent.ACTION_MOVE -> {
                vegetables.animate()
                    .x(motionEvent.rawX + previousX)
                    .y(motionEvent.rawY + previousY)
                    .setDuration(0)
                    .start()
            }
            MotionEvent.ACTION_UP -> {
                var isMatched = false
                puzzlesOnDeskp4ez.forEach { deskPuzzle ->
                    puzzlesGrp4ez.forEach { grPuzzle ->
                        if (deskPuzzle.contentDescription == grPuzzle.contentDescription) {
                            val deskPuzzleId = deskPuzzle.contentDescription.toString()
                            if (collisionFlags[deskPuzzleId] != true) {
                                Log.d("TAG", "Перевіряємо колізію між ${deskPuzzle.contentDescription} та ${grPuzzle.contentDescription}")
                                if (isCollision(deskPuzzle, grPuzzle)) {
                                    deskPuzzle.isVisible = false
                                    deskPuzzle.isEnabled = false
                                    grPuzzle.isVisible = true
                                    isMatched = true
                                    collisionFlags[deskPuzzleId] = true
                                    Log.d("TAG", "Співпадіння знайдено!")
                                }
                            }
                        }
                    }
                }
                if (isMatched) {
                    score += 2
                    counterEzPc4.text = score.toString()

                    if (score >= 10) {
                        hintEzPc4.isEnabled = true
                    }
                    if (isGameCompleted()){
                        timerHelper.stopTimer()
                    }
                } else {
                }
            }
        }
        return true
    }
    private fun ChooseMode(){
        btnWithTp.setOnClickListener {
            spawnRandomObjects()

            btnWithTp.isEnabled = false
            btnWithoutTp.isEnabled = false
            btnWithTp.isVisible = false
            btnWithoutTp.isVisible = false
            pauseBgEzPc4.isVisible = false
            textChooseM1EzPc4.isVisible = false
            textChooseM2EzPc4.isVisible = false
            timerHelper.startTimer(timerEzPc4)
        }
        btnWithoutTp.setOnClickListener {
            spawnRandomObjectsWithoutTp()
            btnWithTp.isEnabled = false
            btnWithoutTp.isEnabled = false
            btnWithTp.isVisible = false
            btnWithoutTp.isVisible = false
            pauseBgEzPc4.isVisible = false
            textChooseM1EzPc4.isVisible = false
            textChooseM2EzPc4.isVisible = false
            timerHelper.startTimer(timerEzPc4)
        }
    }
    private fun showHintImage() {
        val hintImageView: ImageView = findViewById(R.id.hintImgEzPc4)
        hintImageView.isVisible = true
        hintEzPc4.isEnabled = false
        hintImageView.postDelayed({
            hintImageView.isVisible = false
            hintEzPc4.isEnabled = true
        }, 5000)
    }

    private fun isCollision(obj1: View, obj2: View): Boolean {
        val obj1Location = IntArray(2)
        val obj2Location = IntArray(2)
        obj1.getLocationOnScreen(obj1Location)
        obj2.getLocationOnScreen(obj2Location)

        val obj1Left = obj1Location[0]
        val obj1Top = obj1Location[1]
        val obj1Right = obj1Left + obj1.width
        val obj1Bottom = obj1Top + obj1.height

        val obj2Left = obj2Location[0]
        val obj2Top = obj2Location[1]
        val obj2Right = obj2Left + obj2.width
        val obj2Bottom = obj2Top + obj2.height

        val isCollision = obj1Left < obj2Right &&
                obj1Right > obj2Left &&
                obj1Top < obj2Bottom &&
                obj1Bottom > obj2Top
        return isCollision
    }

    private val musicConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
            val binder: MusicService.MusicBinder = iBinder as MusicService.MusicBinder
            musicService = binder.getService()
            val preferences = getSharedPreferences("MusicPrefs", MODE_PRIVATE)
            val preferences1 = getSharedPreferences("MyPrefs", MODE_PRIVATE)
            val musicVolume = preferences1.getInt("music_volume", 50 )
            musicService.setMusicVolume(musicVolume)
            isMusicOn = preferences.getBoolean("music_on", true)
            if (isMusicOn) {
                musicService.resumeMusic()
            } else musicService.pauseMusic()
        }
        override fun onServiceDisconnected(componentName: ComponentName) {
            musicService.stopService(serviceIntent)
        }
    }
    override fun onPause() {
        super.onPause()
        if (isActivityCheck) {
            unbindService(musicConnection)
            isActivityCheck = false
        }
        stopService(serviceIntent)
        if (::musicService.isInitialized) {
            musicService.pauseMusic()
        }
    }
    override fun onResume() {
        super.onResume()
        bindService(serviceIntent, musicConnection, BIND_AUTO_CREATE)
        startService(serviceIntent)
    }
    override fun onStart() {
        super.onStart()

    }

    override fun onStop() {
        super.onStop()
        timerHelper.stopTimer()
    }
    fun animateImageSize4() {
        val animImageEzPc4: ImageView = findViewById(R.id.AnimImgEzPc4)
        animImageEzPc4.isVisible = true
        puzzlesGrp4ez.forEach { puzzle ->
            puzzle.isVisible = false
        }

        val scaleUpX = ObjectAnimator.ofFloat(animImageEzPc4, "scaleX", 1f, 2f)
        val scaleUpY = ObjectAnimator.ofFloat(animImageEzPc4, "scaleY", 1f, 2f)
        scaleUpX.duration = 5000
        scaleUpY.duration = 5000

        val scaleDownX = ObjectAnimator.ofFloat(animImageEzPc4, "scaleX", 2f, 1f)
        val scaleDownY = ObjectAnimator.ofFloat(animImageEzPc4, "scaleY", 2f, 1f)
        scaleDownX.duration = 5000
        scaleDownY.duration = 5000

        val animatorSet = AnimatorSet()
        animatorSet.play(scaleUpX).with(scaleUpY)
        animatorSet.play(scaleDownX).with(scaleDownY).after(scaleUpX)
        animatorSet.start()
    }
    private fun isGameCompleted(): Boolean {
        val isCompleted = puzzlesOnDeskp4ez.all { !it.isVisible }
        val gameDuration = endTime - startTime
        if (isCompleted) {
            val gameTime = System.currentTimeMillis()
            CoroutineScope(Dispatchers.IO).launch {
                saveScoreToDatabase(1, gameDuration)
            }
            timerHelper.stopTimer()
            endTime = System.currentTimeMillis()
            animateImageSize4()
        }

        return isCompleted
    }

    // Ваш AsyncTask для запису результату в базу
    private suspend fun saveScoreToDatabase(userId: Int, gameDuration: Long,) {
        try {
            Log.d("SaveScoreTask", "Start Time: $startTime")
            Log.d("SaveScoreTask", "End Time: $endTime")
            val gameDifficultList = arrayOf("Easy", "Middle", "Hard")
            val gameDuration = endTime - startTime
            Log.d("SaveScoreTask", "Game Duration (ms): $gameDuration")

            // Форматування тривалості гри (в хвилинах та секундах)
            val formattedTime = formatTime(gameDuration)
            Log.d("SaveScoreTask", "Formatted Time: $formattedTime")

            val connection = DriverManager.getConnection(
                "jdbc:mysql://10.0.2.2:3306/game4cr",
                "root",
                "root"
            )
            val query = "UPDATE `game4cr`.`scores` SET `played_time` = '$formattedTime' WHERE (`id` = '$recivedData');"
            val preparedStatement = connection.prepareStatement(query)
            val rowsAffected = preparedStatement.executeUpdate()
            Log.d("SaveScoreTask", "Rows affected: $rowsAffected")
        } catch (e: Exception) {
            Log.e("SaveScoreTask", "Error: ${e.message}")
        }
    }
    fun formatTime(milliseconds: Long): String {
        val seconds = (milliseconds / 1000) % 60
        val minutes = (milliseconds / (1000 * 60)) % 60
        val hours = (milliseconds / (1000 * 60 * 60)) % 24

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
    private fun hideUi() {
        val uiOptions = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LOW_PROFILE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val insetsController = ViewCompat.getWindowInsetsController(window.decorView)
            insetsController?.let {it.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                it.hide(WindowInsetsCompat.Type.systemBars())        }
        } else {
            val decorView = window.decorView
            decorView.systemUiVisibility = uiOptions
        }
    }
}