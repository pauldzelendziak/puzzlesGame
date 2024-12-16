package mobi.salon.paragraph.helped

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.SharedPreferences
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.service.autofill.UserData
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SettingsActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var musicService: MusicService
    private lateinit var serviceIntent: Intent
    private lateinit var audioManager: AudioManager
    private var isBound = false
    private lateinit var bgOriginal: ImageView


    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as MusicService.MusicBinder
            musicService = binder.getService()
            isBound = true
            GlobalScope.launch { delay(700)
                val musicProgress = sharedPreferences.getInt("music_volume", 50)
                musicService.setMusicVolume(musicProgress) }

        }
        override fun onServiceDisconnected(arg0: ComponentName) {
            isBound = false
        }

    }
    private val bgColors = arrayOf(
        R.drawable.bg,R.drawable.bgch1,R.drawable.bgch2,R.drawable.bgch3,R.drawable.bgch4,
        R.drawable.bgch5,R.drawable.bgch6,R.drawable.bgch7,R.drawable.bgch8,
    )
    private var bgIndex: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        hideUi()
        // TODO трабл з тим userData
        val sharedPreferencesForCurrentUser = getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val UserData = sharedPreferencesForCurrentUser.getString("currentData", "")
        val sharedPrefsBg = getSharedPreferences("BgPrefs", Context.MODE_PRIVATE)
        bgIndex = sharedPrefsBg.getInt("bgIndex${UserData}", 0)
        val btnRight: ImageButton = findViewById(R.id.btn_right)
        bgOriginal = findViewById(R.id.bgSettingsAct)
        bgOriginal.setImageResource(bgColors[bgIndex])

        btnRight.setOnClickListener {
            bgIndex = (bgIndex + 1) % bgColors.size
            bgOriginal.setImageResource(bgColors[bgIndex])
            val sharedPrefsBg = getSharedPreferences("BgPrefs", Context.MODE_PRIVATE)
            val editor = sharedPrefsBg.edit()
            editor.putInt("bgIndex${UserData}", bgIndex)
            editor.apply()
        }

        val btnLeft: ImageButton = findViewById(R.id.btn_left)
        btnLeft.setOnClickListener {
            bgIndex = (bgIndex + 1) % bgColors.size
            bgOriginal.setImageResource(bgColors[bgIndex])
            val sharedPrefsBg = getSharedPreferences("BgPrefs", Context.MODE_PRIVATE)
            val editor = sharedPrefsBg.edit()
            editor.putInt("bgIndex${UserData}", bgIndex)
            editor.apply()
        }

        val click = MediaPlayer.create(this, R.raw.click)
        val seekBar: SeekBar = findViewById(R.id.VolumeSeekBar)
        val sbBrigthness: SeekBar = findViewById(R.id.sbBrigthness)
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        serviceIntent = Intent(this, MusicService::class.java)
        bindService(serviceIntent, connection, BIND_AUTO_CREATE)
        val musicProgress = sharedPreferences.getInt("music_volume", 50)
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        seekBar.progress = musicProgress
        ////
        val backBtn: ImageButton = findViewById(R.id.stBackBtn)
        backBtn.setOnClickListener {
            click.start()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val layoutParams = window.attributes
        sbBrigthness.progress  = (layoutParams.screenBrightness * 100).toInt()
        Log.d("TAG", "onCreate: sb progress: " + sbBrigthness.progress + " lp: " + layoutParams.screenBrightness)
        val sharedPreferencesForBrigthness = getSharedPreferences("Brigthness", Context.MODE_PRIVATE)
        val Brigthness = sharedPreferencesForBrigthness.getFloat("brigthnessValue", 1f)
        layoutParams.screenBrightness = Brigthness
        window.attributes = layoutParams
        sbBrigthness.progress = (Brigthness * 255f).toInt()
        sbBrigthness.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val brightnessValue =
                    progress / 255f // перетворення значення в діапазоні 0.0 до 1.0
                val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
                val layoutParams = window.attributes
                layoutParams.screenBrightness = brightnessValue
                window.attributes = layoutParams
                val editor = sharedPreferencesForBrigthness.edit()
                editor.putFloat("brigthnessValue", brightnessValue)
                editor.apply()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                sharedPreferences.edit().putInt("music_volume", progress).apply()
                musicService.setMusicVolume(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        layoutParams.screenBrightness = Brigthness
        window.attributes = layoutParams
    }
    override fun onPause() {
        super.onPause()
        if (isBound) {
            unbindService(connection)
            isBound = false
        }

    }

    override fun onResume() {
        super.onResume()
        bindService(serviceIntent, connection, BIND_AUTO_CREATE)
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