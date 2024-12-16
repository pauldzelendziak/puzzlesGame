package mobi.salon.paragraph.helped

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
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class ChooseActivity : AppCompatActivity() {
    private lateinit var chooseBtn : ImageButton
    private lateinit var imageButtons: Array<ImageButton>
    private lateinit var serviceIntent: Intent
    private lateinit var musicService: MusicService
    private var isActivityCheck = true
    private var isMusicOn = false
    private var selectedButtonIndex: Int? = null
    ////////
    private lateinit var bgImageView: ImageView
    private lateinit var sharedPrefsBg: SharedPreferences
    private val bgColors = arrayOf(
        R.drawable.bg,R.drawable.bgch1,R.drawable.bgch2,R.drawable.bgch3,R.drawable.bgch4,
        R.drawable.bgch5,R.drawable.bgch6,R.drawable.bgch7,R.drawable.bgch8,
    )
    ////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose)
        chooseBtn = findViewById(R.id.confirmBtn)
        hideUi()
        ////////
        bgImageView = findViewById(R.id.bgActivityChooseImg)
        val sharedPreferencesForCurrentUser = getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val UserData = sharedPreferencesForCurrentUser.getString("currentData", "")
        sharedPrefsBg = getSharedPreferences("BgPrefs", Context.MODE_PRIVATE)
        val bgIndex = sharedPrefsBg.getInt("bgIndex${UserData}", 0)
        bgImageView.setImageResource(bgColors[bgIndex])
        //////////
        val click = MediaPlayer.create(this, R.raw.click)
        serviceIntent = Intent(this, MusicService::class.java)
        bindService(serviceIntent, musicConnection, BIND_AUTO_CREATE)
        val sharedPreferencesForBrigthness = getSharedPreferences("Brigthness", Context.MODE_PRIVATE)
        val Brigthness = sharedPreferencesForBrigthness.getFloat("brigthnessValue", 1f)
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val layoutParams = window.attributes
        layoutParams.screenBrightness = Brigthness
        window.attributes = layoutParams
        imageButtons = arrayOf(
            findViewById(R.id.chIm1),
            findViewById(R.id.chIm2),
            findViewById(R.id.chIm3),
            findViewById(R.id.chIm4),
            findViewById(R.id.chIm5),

        )
        for ((index, button) in imageButtons.withIndex()) {
            button.setOnClickListener {
                selectedButtonIndex = index
            }
        }

        chooseBtn.setOnClickListener {
            selectedButtonIndex?.let { index ->
                val intent = when (index) {
                    0 -> Intent(this, ChooseActivityLvl1::class.java)
                    1 -> Intent(this, ChooseActivityLvl3::class.java)
                    2 -> Intent(this, ChooseActivityLvl2::class.java)
                    3 -> Intent(this, ChooseActivityLvl4::class.java)
                    4 -> Intent(this, ChooseActivityLvl5::class.java)
                    else -> throw IllegalArgumentException("Invalid button index")
                }
                startActivity(intent)
            }
        }
        val backBtn: ImageButton = findViewById(R.id.backBtnn)
        backBtn.setOnClickListener {
            click.start()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
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
