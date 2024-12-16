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
import kotlin.random.Random

class GameActivityMdPc1 : AppCompatActivity() {
    private lateinit var puzzlesOnDesk: Array<ImageView>
    private lateinit var puzzlesGr: Array<ImageView>
    private var previousX: Float = 0f
    private var previousY: Float = 0f
    private lateinit var serviceIntent: Intent
    private lateinit var musicService: MusicService
    private var isActivityCheck = true
    private var isMusicOn = false
    //////////////////////////////////////////
    private lateinit var timerMdPc1: TextView
    private val timerHelper = TimerHelper()
    private var score: Int = 0
    private var startTime: Long = 0
    private var endTime: Long = 0
    private lateinit var hintMdPc1: ImageButton
    private lateinit var btnWithTp: ImageButton
    private lateinit var btnWithoutTp: ImageButton
    private lateinit var counterMdPc1: TextView
    private var recivedData : Int = -1
    private lateinit var pauseBgMdPc1: ImageView
    private lateinit var textChooseM1MdPc1: TextView
    private lateinit var textChooseM2MdPc1: TextView
    /////////////////////////////////////////////
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
        setContentView(R.layout.activity_game_mid_pc1)
        hideUi()
        recivedData = intent.getIntExtra("extra_data", -1)
        ////////
        bgImageView = findViewById(R.id.bgActGameMdPc1)
        val sharedPreferencesForCurrentUser = getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val UserData = sharedPreferencesForCurrentUser.getString("currentData", "")
        sharedPrefsBg = getSharedPreferences("BgPrefs", Context.MODE_PRIVATE)
        val bgIndex = sharedPrefsBg.getInt("bgIndex${UserData}", 0)
        bgImageView.setImageResource(bgColors[bgIndex])
        //////////
        serviceIntent = Intent(this, MusicService::class.java)
        bindService(serviceIntent, musicConnection, BIND_AUTO_CREATE)
        val click = MediaPlayer.create(this, R.raw.click)
        val backBtn: ImageButton = findViewById(R.id.backMdPc1)
        backBtn.setOnClickListener {
            click.start()
            val intent = Intent(this, ChooseActivityLvl1::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
        //////////////////////////////////////
        hintMdPc1 = findViewById(R.id.hintMdPc1)
        hintMdPc1.isEnabled = false
        counterMdPc1 = findViewById(R.id.counterMdPc1)
        //////////////////////////////////////////
        val sharedPreferencesForBrigthness = getSharedPreferences("Brigthness", Context.MODE_PRIVATE)
        val Brigthness = sharedPreferencesForBrigthness.getFloat("brigthnessValue", 1f)
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val layoutParams = window.attributes
        layoutParams.screenBrightness = Brigthness
        window.attributes = layoutParams
        puzzlesOnDesk = arrayOf(
            findViewById(R.id.md_im1),
            findViewById(R.id.md_im2),
            findViewById(R.id.md_im3),
            findViewById(R.id.md_im4),
            findViewById(R.id.md_im5),
            findViewById(R.id.md_im6),
            findViewById(R.id.md_im7),
            findViewById(R.id.md_im8),
            findViewById(R.id.md_im9),
            findViewById(R.id.md_im10),
            findViewById(R.id.md_im11),
            findViewById(R.id.md_im12),
            findViewById(R.id.md_im13),
            findViewById(R.id.md_im14),
            findViewById(R.id.md_im15),
            findViewById(R.id.md_im16),
            findViewById(R.id.md_im17),
            findViewById(R.id.md_im18),
            findViewById(R.id.md_im19),
            findViewById(R.id.md_im20),
            findViewById(R.id.md_im21),
            findViewById(R.id.md_im22),
            findViewById(R.id.md_im23),
            findViewById(R.id.md_im24),
            findViewById(R.id.md_im25),
            findViewById(R.id.md_im26),
            findViewById(R.id.md_im27),
            findViewById(R.id.md_im28),
            findViewById(R.id.md_im29),
            findViewById(R.id.md_im30),
            findViewById(R.id.md_im31),
            findViewById(R.id.md_im32),
            findViewById(R.id.md_im33),
            findViewById(R.id.md_im34),
            findViewById(R.id.md_im35),
            findViewById(R.id.md_im36),
            findViewById(R.id.md_im37),
            findViewById(R.id.md_im38),
            findViewById(R.id.md_im39),
            findViewById(R.id.md_im40),
            findViewById(R.id.md_im41),
            findViewById(R.id.md_im42),
            findViewById(R.id.md_im43),
            findViewById(R.id.md_im44),
            findViewById(R.id.md_im45),
            findViewById(R.id.md_im46),
            findViewById(R.id.md_im47),
            findViewById(R.id.md_im48),
        )
        puzzlesGr = arrayOf(
            findViewById(R.id.md_imgr1),
            findViewById(R.id.md_imgr2),
            findViewById(R.id.md_imgr3),
            findViewById(R.id.md_imgr4),
            findViewById(R.id.md_imgr5),
            findViewById(R.id.md_imgr6),
            findViewById(R.id.md_imgr7),
            findViewById(R.id.md_imgr8),
            findViewById(R.id.md_imgr9),
            findViewById(R.id.md_imgr10),
            findViewById(R.id.md_imgr11),
            findViewById(R.id.md_imgr12),
            findViewById(R.id.md_imgr13),
            findViewById(R.id.md_imgr14),
            findViewById(R.id.md_imgr15),
            findViewById(R.id.md_imgr16),
            findViewById(R.id.md_imgr17),
            findViewById(R.id.md_imgr18),
            findViewById(R.id.md_imgr19),
            findViewById(R.id.md_imgr20),
            findViewById(R.id.md_imgr21),
            findViewById(R.id.md_imgr22),
            findViewById(R.id.md_imgr23),
            findViewById(R.id.md_imgr24),
            findViewById(R.id.md_imgr25),
            findViewById(R.id.md_imgr26),
            findViewById(R.id.md_imgr27),
            findViewById(R.id.md_imgr28),
            findViewById(R.id.md_imgr29),
            findViewById(R.id.md_imgr30),
            findViewById(R.id.md_imgr31),
            findViewById(R.id.md_imgr32),
            findViewById(R.id.md_imgr33),
            findViewById(R.id.md_imgr34),
            findViewById(R.id.md_imgr35),
            findViewById(R.id.md_imgr36),
            findViewById(R.id.md_imgr37),
            findViewById(R.id.md_imgr38),
            findViewById(R.id.md_imgr39),
            findViewById(R.id.md_imgr40),
            findViewById(R.id.md_imgr41),
            findViewById(R.id.md_imgr42),
            findViewById(R.id.md_imgr43),
            findViewById(R.id.md_imgr44),
            findViewById(R.id.md_imgr45),
            findViewById(R.id.md_imgr46),
            findViewById(R.id.md_imgr47),
            findViewById(R.id.md_imgr48),
        )
        puzzlesOnDesk.forEach { puzzle ->
            puzzle.setOnTouchListener(null)
        }
        /////////////////////////////////////////////
        hintMdPc1.setOnClickListener {
            if (score >= 4) {
                score -= 4
                counterMdPc1.text = score.toString()
                showHintImage()
                hintMdPc1.isEnabled = false
            } else {
            }
        }

        startTime = System.currentTimeMillis()
        pauseBgMdPc1 = findViewById(R.id.foneMdPc1)
        btnWithTp = findViewById(R.id.btn_WithTpMdPc1)
        btnWithoutTp = findViewById(R.id.btn_WithoutTpMdPc1)
        timerMdPc1 = findViewById(R.id.timer_Md_pc1)
        textChooseM1MdPc1 = findViewById(R.id.textChooseM1MdPc1)
        textChooseM2MdPc1 = findViewById(R.id.textChooseM2MdPc1)
        ChooseMode()
        //////////////////////////////////////////////////////////////
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun spawnRandomObjects() {
        puzzlesOnDesk.forEach { puzzle ->
            puzzle.setOnTouchListener { view, motionEvent ->
                handleTouch(view, motionEvent)
            }
        }
    }
    private fun spawnRandomObjectsWithoutTp() {
        puzzlesOnDesk.forEach { puzzle ->
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

                puzzlesOnDesk.forEach { deskPuzzle ->
                    puzzlesGr.forEach { grPuzzle ->
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
                                counterMdPc1.text = score.toString()

                                if (score >= 10) {
                                    hintMdPc1.isEnabled = true
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
                puzzlesOnDesk.forEach { deskPuzzle ->
                    puzzlesGr.forEach { grPuzzle ->
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
                    counterMdPc1.text = score.toString()

                    if (score >= 10) {
                        hintMdPc1.isEnabled = true
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
            pauseBgMdPc1.isVisible = false
            textChooseM1MdPc1.isVisible = false
            textChooseM2MdPc1.isVisible = false
            timerHelper.startTimer(timerMdPc1)
        }
        btnWithoutTp.setOnClickListener {
            spawnRandomObjectsWithoutTp()
            btnWithTp.isEnabled = false
            btnWithoutTp.isEnabled = false
            btnWithTp.isVisible = false
            btnWithoutTp.isVisible = false
            pauseBgMdPc1.isVisible = false
            textChooseM1MdPc1.isVisible = false
            textChooseM2MdPc1.isVisible = false
            timerHelper.startTimer(timerMdPc1)
        }
    }
    private fun showHintImage() {
        val hintImageView: ImageView = findViewById(R.id.hintImgMdPc1)
        hintImageView.isVisible = true
        hintMdPc1.isEnabled = false
        hintImageView.postDelayed({
            hintImageView.isVisible = false
            hintMdPc1.isEnabled = true
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
        return obj1Left < obj2Right &&
                obj1Right > obj2Left &&
                obj1Top < obj2Bottom &&
                obj1Bottom > obj2Top
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
    fun animateImageSize9() {
        val animImageEzPc4: ImageView = findViewById(R.id.animImgMdPc1)
        animImageEzPc4.isVisible = true
        puzzlesGr.forEach { puzzle ->
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
        val isCompleted = puzzlesOnDesk.all { !it.isVisible }
        val gameDuration = endTime - startTime
        if (isCompleted) {
            val gameTime = System.currentTimeMillis()
            CoroutineScope(Dispatchers.IO).launch {
                saveScoreToDatabase(1, gameDuration)
            }
            timerHelper.stopTimer()
            endTime = System.currentTimeMillis()
            animateImageSize9()
        }

        return isCompleted
    }
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
            decorView.systemUiVisibility = uiOptions    }
    }
}