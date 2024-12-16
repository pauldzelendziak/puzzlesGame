package mobi.salon.paragraph.helped

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import kotlin.random.Random

class ChooseActivityLvl1 : AppCompatActivity() {
    private lateinit var serviceIntent: Intent
    private lateinit var musicService: MusicService
    private var isActivityCheck = true
    private var isMusicOn = false
    private lateinit var imageButtonsLvl: Array<ImageButton>
    private lateinit var randomChoosePc1: ImageButton
    ////////
    private lateinit var bgImageView: ImageView
    private lateinit var sharedPrefsBg: SharedPreferences
    private val bgColors = arrayOf(
        R.drawable.bg,R.drawable.bgch1,R.drawable.bgch2,R.drawable.bgch3,R.drawable.bgch4,
        R.drawable.bgch5,R.drawable.bgch6,R.drawable.bgch7,R.drawable.bgch8,
    )
    ////////
    @SuppressLint("StaticFieldLeak")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_lvl)
        ////////
        bgImageView = findViewById(R.id.bgActivityChooseLvlP1)
        val sharedPreferencesForCurrentUser = getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val UserData = sharedPreferencesForCurrentUser.getString("currentData", "")
        sharedPrefsBg = getSharedPreferences("BgPrefs", Context.MODE_PRIVATE)
        val bgIndex = sharedPrefsBg.getInt("bgIndex${UserData}", 0)
        bgImageView.setImageResource(bgColors[bgIndex])
        //////////
        hideUi()
        val click = MediaPlayer.create(this, R.raw.click)
        serviceIntent = Intent(this, MusicService::class.java)
        bindService(serviceIntent, musicConnection, BIND_AUTO_CREATE)
        imageButtonsLvl = arrayOf(
            findViewById(R.id.ezAct1),
            findViewById(R.id.midAct1),
            findViewById(R.id.hdAct1),
        )
        randomChoosePc1 = findViewById(R.id.randomBtnLvl1Pc1)
        randomChoosePc1 = findViewById(R.id.randomBtnLvl1Pc1)
        val sharedPreferencesForBrigthness =
            getSharedPreferences("Brigthness", Context.MODE_PRIVATE)
        val Brigthness = sharedPreferencesForBrigthness.getFloat("brigthnessValue", 1f)
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val layoutParams = window.attributes
        layoutParams.screenBrightness = Brigthness
        window.attributes = layoutParams
        val backBtn: ImageButton = findViewById(R.id.backBtnChoseLvl1)
        backBtn.setOnClickListener {
            click.start()
            val intent = Intent(this, ChooseActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
        for ((index, button) in imageButtonsLvl.withIndex()) {
            button.setOnClickListener {
                val gameDifficultList = arrayOf("Easy", "Middle", "Hard")
                object : AsyncTask<Void, Void, String>() {
                    override fun doInBackground(vararg params: Void?): String {
                        return try {
                            val sharedPreferences = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
                            val id = sharedPreferences.getInt("id", -1)

                            if (id == -1) {
                                return "Помилка: Користувач не авторизований."
                            }

                            val connection = DriverManager.getConnection(
                                "jdbc:mysql://10.0.2.2:3306/game4cr",
                                "root",
                                "root"
                            )
                            val insertQuery = "INSERT INTO `game4cr`.`scores` (`user_id`, `lvl`) VALUES (?, ?);"
                            val preparedStatement: PreparedStatement = connection.prepareStatement(insertQuery)
                            preparedStatement.setInt(1, id)
                            preparedStatement.setString(2, gameDifficultList[index].toString())
                            preparedStatement.executeUpdate()
                            val selectQuery = "SELECT MAX(id) AS max_id FROM game4cr.scores WHERE user_id = ?;"
                            val preparedStatement2: PreparedStatement = connection.prepareStatement(selectQuery)
                            preparedStatement2.setInt(1, id)
                            val resultSet: ResultSet = preparedStatement2.executeQuery()
                            val resultMessage = if (resultSet.next()) {
                                val maxId = resultSet.getInt("max_id")
                                val intent =  when (index) {
                                    0 -> Intent(this@ChooseActivityLvl1, GameActivityEzPc1::class.java)
                                    1 -> Intent(this@ChooseActivityLvl1, GameActivityMdPc1::class.java)
                                    2 -> Intent(this@ChooseActivityLvl1, GameActivityHdPc1::class.java)
                                    else -> throw IllegalArgumentException("Invalid button index")
                                }

                                intent.putExtra("extra_data", maxId)
                                startActivity(intent)

                                "Операція успішна! Найбільший ID: $maxId"
                            } else {
                                "Дані не знайдено."
                            }
                            resultSet.close()
                            preparedStatement.close()
                            preparedStatement2.close()
                            connection.close()

                            resultMessage
                        } catch (e: Exception) {
                            "Помилка: ${e.message}"
                        }
                    }

                    override fun onPostExecute(result: String) {
                        super.onPostExecute(result)
                        Log.d("MyLog", result)
                    }
                }.execute()
            }

        }
        randomChoosePc1.setOnClickListener {
            val randomIndex = Random.nextInt(3)
            val intentRandom = when (randomIndex) {
                0 -> Intent(this, GameActivityEzPc1::class.java)
                1 -> Intent(this, GameActivityMdPc1::class.java)
                2 -> Intent(this, GameActivityHdPc1::class.java)
                else -> throw IllegalArgumentException("Invalid button index")
            }
            startActivity(intentRandom)
        }

    }

    private val musicConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
            val binder: MusicService.MusicBinder = iBinder as MusicService.MusicBinder
            musicService = binder.getService()
            val preferences = getSharedPreferences("MusicPrefs", MODE_PRIVATE)
            val preferences1 = getSharedPreferences("MyPrefs", MODE_PRIVATE)
            val musicVolume = preferences1.getInt("music_volume", 50)
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
            insetsController?.let {
                it.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                it.hide(WindowInsetsCompat.Type.systemBars())
            }
        } else {
            val decorView = window.decorView
            decorView.systemUiVisibility = uiOptions
        }
    }
}


