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

class GameActivityHdPc5 : AppCompatActivity() {
    private lateinit var serviceIntent: Intent
    private lateinit var musicService: MusicService
    private var isActivityCheck = true
    private var isMusicOn = false
    private lateinit var puzzlesOnDeskPc5: Array<ImageView>
    private lateinit var puzzlesGrPc5: Array<ImageView>
    private var previousX: Float = 0f
    private var previousY: Float = 0f
    //////////////////////////////////////////
    private lateinit var timerHdPc5: TextView
    private val timerHelper = TimerHelper()
    private var score: Int = 0
    private var startTime: Long = 0
    private var endTime: Long = 0
    private lateinit var hintHdPc5: ImageButton
    private lateinit var btnWithTpHdP5: ImageButton
    private lateinit var btnWithoutTpHdP5: ImageButton
    private lateinit var counterHdPc5: TextView
    private var recivedData : Int = -1
    private lateinit var pauseBgHdPc5: ImageView
    private lateinit var textChooseM1HdPc5: TextView
    private lateinit var textChooseM2HdPc5: TextView
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
        setContentView(R.layout.activity_game_hd_pc5)
        hideUi()
        recivedData = intent.getIntExtra("extra_data", -1)
        bgImageView = findViewById(R.id.bgActGameEzPc5)
        val sharedPreferencesForCurrentUser = getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val UserData = sharedPreferencesForCurrentUser.getString("currentData", "")
        sharedPrefsBg = getSharedPreferences("BgPrefs", Context.MODE_PRIVATE)
        val bgIndex = sharedPrefsBg.getInt("bgIndex${UserData}", 0)
        bgImageView.setImageResource(bgColors[bgIndex])
        val click = MediaPlayer.create(this, R.raw.click)
        val backBtn: ImageButton = findViewById(R.id.backHdPc5)
        backBtn.setOnClickListener {
            click.start()
            val intent = Intent(this, ChooseActivityLvl5::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
        //////////////////////////////////////
        hintHdPc5 = findViewById(R.id.hintHdPc5)
        hintHdPc5.isEnabled = false
        counterHdPc5 = findViewById(R.id.counterHdPc5)
        //////////////////////////////////////////
        serviceIntent = Intent(this, MusicService::class.java)
        bindService(serviceIntent, musicConnection, BIND_AUTO_CREATE)
        val sharedPreferencesForBrigthness = getSharedPreferences("Brigthness", Context.MODE_PRIVATE)
        val Brigthness = sharedPreferencesForBrigthness.getFloat("brigthnessValue", 1f)
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val layoutParams = window.attributes
        layoutParams.screenBrightness = Brigthness
        window.attributes = layoutParams
        puzzlesOnDeskPc5 = arrayOf(
            findViewById(R.id.p5_hd_im1),
            findViewById(R.id.p5_hd_im2),
            findViewById(R.id.p5_hd_im3),
            findViewById(R.id.p5_hd_im4),
            findViewById(R.id.p5_hd_im5),
            findViewById(R.id.p5_hd_im6),
            findViewById(R.id.p5_hd_im7),
            findViewById(R.id.p5_hd_im8),
            findViewById(R.id.p5_hd_im9),
            findViewById(R.id.p5_hd_im10),
            findViewById(R.id.p5_hd_im11),
            findViewById(R.id.p5_hd_im12),
            findViewById(R.id.p5_hd_im13),
            findViewById(R.id.p5_hd_im14),
            findViewById(R.id.p5_hd_im15),
            findViewById(R.id.p5_hd_im16),
            findViewById(R.id.p5_hd_im17),
            findViewById(R.id.p5_hd_im18),
            findViewById(R.id.p5_hd_im19),
            findViewById(R.id.p5_hd_im20),
            findViewById(R.id.p5_hd_im21),
            findViewById(R.id.p5_hd_im22),
            findViewById(R.id.p5_hd_im23),
            findViewById(R.id.p5_hd_im24),
            findViewById(R.id.p5_hd_im25),
            findViewById(R.id.p5_hd_im26),
            findViewById(R.id.p5_hd_im27),
            findViewById(R.id.p5_hd_im28),
            findViewById(R.id.p5_hd_im29),
            findViewById(R.id.p5_hd_im30),
            findViewById(R.id.p5_hd_im31),
            findViewById(R.id.p5_hd_im32),
            findViewById(R.id.p5_hd_im33),
            findViewById(R.id.p5_hd_im34),
            findViewById(R.id.p5_hd_im35),
            findViewById(R.id.p5_hd_im36),
            findViewById(R.id.p5_hd_im37),
            findViewById(R.id.p5_hd_im38),
            findViewById(R.id.p5_hd_im39),
            findViewById(R.id.p5_hd_im40),
            findViewById(R.id.p5_hd_im41),
            findViewById(R.id.p5_hd_im42),
            findViewById(R.id.p5_hd_im43),
            findViewById(R.id.p5_hd_im44),
            findViewById(R.id.p5_hd_im45),
            findViewById(R.id.p5_hd_im46),
            findViewById(R.id.p5_hd_im47),
            findViewById(R.id.p5_hd_im48),
            findViewById(R.id.p5_hd_im49),
            findViewById(R.id.p5_hd_im50),
            findViewById(R.id.p5_hd_im51),
            findViewById(R.id.p5_hd_im52),
            findViewById(R.id.p5_hd_im53),
            findViewById(R.id.p5_hd_im54),
            findViewById(R.id.p5_hd_im55),
            findViewById(R.id.p5_hd_im56),
            findViewById(R.id.p5_hd_im57),
            findViewById(R.id.p5_hd_im58),
            findViewById(R.id.p5_hd_im59),
            findViewById(R.id.p5_hd_im60),
            findViewById(R.id.p5_hd_im61),
            findViewById(R.id.p5_hd_im62),
            findViewById(R.id.p5_hd_im63),
            findViewById(R.id.p5_hd_im64),
            findViewById(R.id.p5_hd_im65),
            findViewById(R.id.p5_hd_im66),
            findViewById(R.id.p5_hd_im67),
            findViewById(R.id.p5_hd_im68),
            findViewById(R.id.p5_hd_im69),
            findViewById(R.id.p5_hd_im70),
            findViewById(R.id.p5_hd_im71),
            findViewById(R.id.p5_hd_im72),
            findViewById(R.id.p5_hd_im73),
            findViewById(R.id.p5_hd_im74),
            findViewById(R.id.p5_hd_im75),
            findViewById(R.id.p5_hd_im76),
            findViewById(R.id.p5_hd_im77),
            findViewById(R.id.p5_hd_im78),
            findViewById(R.id.p5_hd_im79),
            findViewById(R.id.p5_hd_im80),
            findViewById(R.id.p5_hd_im81),
            findViewById(R.id.p5_hd_im82),
            findViewById(R.id.p5_hd_im83),
            findViewById(R.id.p5_hd_im84),
            findViewById(R.id.p5_hd_im85),
            findViewById(R.id.p5_hd_im86),
            findViewById(R.id.p5_hd_im87),
            findViewById(R.id.p5_hd_im88),
            findViewById(R.id.p5_hd_im89),
            findViewById(R.id.p5_hd_im90),
            findViewById(R.id.p5_hd_im91),
            findViewById(R.id.p5_hd_im92),
            findViewById(R.id.p5_hd_im93),
            findViewById(R.id.p5_hd_im94),
            findViewById(R.id.p5_hd_im95),
            findViewById(R.id.p5_hd_im96),
        )
        puzzlesGrPc5 = arrayOf(
            findViewById(R.id.p5_hd_imgr1),
            findViewById(R.id.p5_hd_imgr2),
            findViewById(R.id.p5_hd_imgr3),
            findViewById(R.id.p5_hd_imgr4),
            findViewById(R.id.p5_hd_imgr5),
            findViewById(R.id.p5_hd_imgr6),
            findViewById(R.id.p5_hd_imgr7),
            findViewById(R.id.p5_hd_imgr8),
            findViewById(R.id.p5_hd_imgr9),
            findViewById(R.id.p5_hd_imgr10),
            findViewById(R.id.p5_hd_imgr11),
            findViewById(R.id.p5_hd_imgr12),
            findViewById(R.id.p5_hd_imgr13),
            findViewById(R.id.p5_hd_imgr14),
            findViewById(R.id.p5_hd_imgr15),
            findViewById(R.id.p5_hd_imgr16),
            findViewById(R.id.p5_hd_imgr17),
            findViewById(R.id.p5_hd_imgr18),
            findViewById(R.id.p5_hd_imgr19),
            findViewById(R.id.p5_hd_imgr20),
            findViewById(R.id.p5_hd_imgr21),
            findViewById(R.id.p5_hd_imgr22),
            findViewById(R.id.p5_hd_imgr23),
            findViewById(R.id.p5_hd_imgr24),
            findViewById(R.id.p5_hd_imgr25),
            findViewById(R.id.p5_hd_imgr26),
            findViewById(R.id.p5_hd_imgr27),
            findViewById(R.id.p5_hd_imgr28),
            findViewById(R.id.p5_hd_imgr29),
            findViewById(R.id.p5_hd_imgr30),
            findViewById(R.id.p5_hd_imgr31),
            findViewById(R.id.p5_hd_imgr32),
            findViewById(R.id.p5_hd_imgr33),
            findViewById(R.id.p5_hd_imgr34),
            findViewById(R.id.p5_hd_imgr35),
            findViewById(R.id.p5_hd_imgr36),
            findViewById(R.id.p5_hd_imgr37),
            findViewById(R.id.p5_hd_imgr38),
            findViewById(R.id.p5_hd_imgr39),
            findViewById(R.id.p5_hd_imgr40),
            findViewById(R.id.p5_hd_imgr41),
            findViewById(R.id.p5_hd_imgr42),
            findViewById(R.id.p5_hd_imgr43),
            findViewById(R.id.p5_hd_imgr44),
            findViewById(R.id.p5_hd_imgr45),
            findViewById(R.id.p5_hd_imgr46),
            findViewById(R.id.p5_hd_imgr47),
            findViewById(R.id.p5_hd_imgr48),
            findViewById(R.id.p5_hd_imgr49),
            findViewById(R.id.p5_hd_imgr50),
            findViewById(R.id.p5_hd_imgr51),
            findViewById(R.id.p5_hd_imgr52),
            findViewById(R.id.p5_hd_imgr53),
            findViewById(R.id.p5_hd_imgr54),
            findViewById(R.id.p5_hd_imgr55),
            findViewById(R.id.p5_hd_imgr56),
            findViewById(R.id.p5_hd_imgr57),
            findViewById(R.id.p5_hd_imgr58),
            findViewById(R.id.p5_hd_imgr59),
            findViewById(R.id.p5_hd_imgr60),
            findViewById(R.id.p5_hd_imgr61),
            findViewById(R.id.p5_hd_imgr62),
            findViewById(R.id.p5_hd_imgr63),
            findViewById(R.id.p5_hd_imgr64),
            findViewById(R.id.p5_hd_imgr65),
            findViewById(R.id.p5_hd_imgr66),
            findViewById(R.id.p5_hd_imgr67),
            findViewById(R.id.p5_hd_imgr68),
            findViewById(R.id.p5_hd_imgr69),
            findViewById(R.id.p5_hd_imgr70),
            findViewById(R.id.p5_hd_imgr71),
            findViewById(R.id.p5_hd_imgr72),
            findViewById(R.id.p5_hd_imgr73),
            findViewById(R.id.p5_hd_imgr74),
            findViewById(R.id.p5_hd_imgr75),
            findViewById(R.id.p5_hd_imgr76),
            findViewById(R.id.p5_hd_imgr77),
            findViewById(R.id.p5_hd_imgr78),
            findViewById(R.id.p5_hd_imgr79),
            findViewById(R.id.p5_hd_imgr80),
            findViewById(R.id.p5_hd_imgr81),
            findViewById(R.id.p5_hd_imgr82),
            findViewById(R.id.p5_hd_imgr83),
            findViewById(R.id.p5_hd_imgr84),
            findViewById(R.id.p5_hd_imgr85),
            findViewById(R.id.p5_hd_imgr86),
            findViewById(R.id.p5_hd_imgr87),
            findViewById(R.id.p5_hd_imgr88),
            findViewById(R.id.p5_hd_imgr89),
            findViewById(R.id.p5_hd_imgr90),
            findViewById(R.id.p5_hd_imgr91),
            findViewById(R.id.p5_hd_imgr92),
            findViewById(R.id.p5_hd_imgr93),
            findViewById(R.id.p5_hd_imgr94),
            findViewById(R.id.p5_hd_imgr95),
            findViewById(R.id.p5_hd_imgr96),
        )
        puzzlesOnDeskPc5.forEach { puzzle ->
            puzzle.setOnTouchListener(null)
        }
        /////////////////////////////////////////////
        hintHdPc5.setOnClickListener {
            if (score >= 4) {
                score -= 4
                counterHdPc5.text = score.toString()
                showHintImage()
                hintHdPc5.isEnabled = false
            } else {
            }
        }

        startTime = System.currentTimeMillis()
        pauseBgHdPc5 = findViewById(R.id.foneHdPc5)
        btnWithTpHdP5 = findViewById(R.id.btn_WithTpHdPc5)
        btnWithoutTpHdP5 = findViewById(R.id.btn_WithoutTpHdPc5)
        timerHdPc5 = findViewById(R.id.timer_Hd_pc5)
        textChooseM1HdPc5 = findViewById(R.id.textChooseM1HdPc5)
        textChooseM2HdPc5 = findViewById(R.id.textChooseM2HdPc5)
        ChooseMode()
        //////////////////////////////////////////////////////////////
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun spawnRandomObjects() {
        puzzlesOnDeskPc5.forEach { puzzle ->
            puzzle.setOnTouchListener { view, motionEvent ->
                handleTouch(view, motionEvent)
            }
        }
    }
    private fun spawnRandomObjectsWithoutTp() {
        puzzlesOnDeskPc5.forEach { puzzle ->
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

                puzzlesOnDeskPc5.forEach { deskPuzzle ->
                    puzzlesGrPc5.forEach { grPuzzle ->
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
                                counterHdPc5.text = score.toString()

                                if (score >= 10) {
                                    hintHdPc5.isEnabled = true
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
                puzzlesOnDeskPc5.forEach { deskPuzzle ->
                    puzzlesGrPc5.forEach { grPuzzle ->
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
                    counterHdPc5.text = score.toString()

                    if (score >= 10) {
                        hintHdPc5.isEnabled = true
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
        btnWithTpHdP5.setOnClickListener {
            spawnRandomObjects()

            btnWithTpHdP5.isEnabled = false
            btnWithoutTpHdP5.isEnabled = false
            btnWithTpHdP5.isVisible = false
            btnWithoutTpHdP5.isVisible = false
            pauseBgHdPc5.isVisible = false
            textChooseM1HdPc5.isVisible = false
            textChooseM2HdPc5.isVisible = false
            timerHelper.startTimer(timerHdPc5)
        }
        btnWithoutTpHdP5.setOnClickListener {
            spawnRandomObjectsWithoutTp()
            btnWithTpHdP5.isEnabled = false
            btnWithoutTpHdP5.isEnabled = false
            btnWithTpHdP5.isVisible = false
            btnWithoutTpHdP5.isVisible = false
            pauseBgHdPc5.isVisible = false
            textChooseM1HdPc5.isVisible = false
            textChooseM2HdPc5.isVisible = false
            timerHelper.startTimer(timerHdPc5)
        }
    }
    private fun showHintImage() {
        val hintImageView: ImageView = findViewById(R.id.hintImgHdPc5)
        hintImageView.isVisible = true
        hintHdPc5.isEnabled = false
        hintImageView.postDelayed({
            hintImageView.isVisible = false
            hintHdPc5.isEnabled = true
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
    fun animateImageSize5() {
        val animImageEzPc5: ImageView = findViewById(R.id.animImgHdPc5)
        animImageEzPc5.isVisible = true
        puzzlesGrPc5.forEach { puzzle ->
            puzzle.isVisible = false
        }

        val scaleUpX = ObjectAnimator.ofFloat(animImageEzPc5, "scaleX", 1f, 2f)
        val scaleUpY = ObjectAnimator.ofFloat(animImageEzPc5, "scaleY", 1f, 2f)
        scaleUpX.duration = 5000
        scaleUpY.duration = 5000

        val scaleDownX = ObjectAnimator.ofFloat(animImageEzPc5, "scaleX", 2f, 1f)
        val scaleDownY = ObjectAnimator.ofFloat(animImageEzPc5, "scaleY", 2f, 1f)
        scaleDownX.duration = 5000
        scaleDownY.duration = 5000

        val animatorSet = AnimatorSet()
        animatorSet.play(scaleUpX).with(scaleUpY)
        animatorSet.play(scaleDownX).with(scaleDownY).after(scaleUpX)
        animatorSet.start()
    }
    private fun isGameCompleted(): Boolean {
        val isCompleted = puzzlesOnDeskPc5.all { !it.isVisible }
        val gameDuration = endTime - startTime
        if (isCompleted) {
            val gameTime = System.currentTimeMillis()
            CoroutineScope(Dispatchers.IO).launch {
                saveScoreToDatabase(1, gameDuration)
            }
            timerHelper.stopTimer()
            endTime = System.currentTimeMillis()
            animateImageSize5()
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