package mobi.salon.paragraph.helped

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import mobi.salon.paragraph.helped.databinding.ActivityGameRecordsBinding
import java.sql.DriverManager

class GameRecordsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameRecordsBinding
    private val records = mutableListOf<GameRecord>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameRecordsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvGameRecords.layoutManager = LinearLayoutManager(this)
        val adapter = GameRecordAdapter(records)
        binding.rvGameRecords.adapter = adapter

        fetchGameRecords(adapter)
        hideUi()
        val click = MediaPlayer.create(this, R.raw.click)
        val backBtnAch: ImageButton = findViewById(R.id.backBtnAchAct)
        backBtnAch.setOnClickListener {
            click.start()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }

    private fun fetchGameRecords(adapter: GameRecordAdapter) {
        records.add(GameRecord("10 хвилин", "Середній"))
        adapter.notifyDataSetChanged()

        Thread {
            try {
                // Логування перед підключенням
                Log.d("Database", "Початок підключення до бази даних")

                Class.forName("com.mysql.jdbc.Driver")
                val connection = DriverManager.getConnection(
                    "jdbc:mysql://10.0.2.2:3306/game4cr",
                    "root",
                    "root"
                )
                Log.d("Database", "Підключення до бази даних успішне")

                // Отримуємо id користувача з SharedPreferences
                val sharedPreferences = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
                val userId = sharedPreferences.getInt("id", -1)  // Якщо id немає, повернеться -1
                if (userId == -1) {
                    Log.d("Database", "Не знайдено id користувача в SharedPreferences")
                }

                // Запит для вибірки записів за id користувача
                val query = "SELECT played_time, lvl FROM game4cr.scores WHERE user_id = ?"
                val statement = connection.prepareStatement(query)
                statement.setInt(1, userId) // Підставляємо id користувача в запит

                // Логування запиту
                Log.d("Database", "Виконання запиту: $query")

                val resultSet = statement.executeQuery()

                // Перевірка чи є результати
                if (!resultSet.next()) {
                    Log.d("Database", "Немає записів у базі даних для користувача з id: $userId")
                }

                while (resultSet.next()) {
                    val playedTime = resultSet.getString("played_time") ?: "null"  // Якщо null, то використовуємо дефолтне значення
                    val level = resultSet.getString("lvl") ?: "Невідомо"  // Якщо null, то використовуємо дефолтне значення
                    records.add(GameRecord(playedTime, level))
                    Log.d("GameRecords", "Додано запис: $playedTime, $level")
                }

                runOnUiThread {
                    adapter.notifyDataSetChanged()
                }

                resultSet.close()
                statement.close()
                connection.close()
            } catch (e: Exception) {
                // Логування помилки
                Log.e("DatabaseError", "Помилка при підключенні або запиті", e)

                val errorMessage = e.message ?: "Невідома помилка"
                runOnUiThread {
                    Toast.makeText(this, "Помилка завантаження даних: $errorMessage", Toast.LENGTH_LONG).show()
                }
            }
        }.start()
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


