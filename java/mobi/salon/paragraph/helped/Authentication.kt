package mobi.salon.paragraph.helped

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet

class Authentication : AppCompatActivity() {
    private lateinit var usernameField: EditText
    private lateinit var passwordField: EditText
    private lateinit var logText: TextView
    private lateinit var regText: TextView
    private lateinit var buttonLogin: ImageButton
    private lateinit var registerButton: ImageButton
    private var chooseBtnClickCount = 0
    private var isButtonLoginVisible = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.authentication)
        hideUi()
        usernameField = findViewById(R.id.setLoginText)
        passwordField = findViewById(R.id.setPasswordText)
        buttonLogin = findViewById(R.id.loginBtn)
        registerButton = findViewById(R.id.createAcc)

        logText = findViewById(R.id.loginText)
        regText = findViewById(R.id.regText)
        val chooseBtn: ImageButton = findViewById(R.id.chooseBtn)
        chooseBtn.setOnClickListener {
            chooseBtnClickCount++
            isButtonLoginVisible = chooseBtnClickCount % 2 == 0
            setButtonVisibility()
        }
        // Обробка натискання кнопки реєстрації
        registerButton.setOnClickListener {
            val username = usernameField.text.toString()
            val password = passwordField.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Введіть усі дані", Toast.LENGTH_SHORT).show()
            } else {
                RegisterUserTask(this, username, password).execute()
            }
        }

        // Обробка натискання кнопки логіну
        buttonLogin.setOnClickListener {
            val username = usernameField.text.toString()
            val password = passwordField.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Введіть логін та пароль", Toast.LENGTH_SHORT).show()
            } else {
                LoginUserTask(this, username, password).execute()
            }
        }
        val sharedPreferences = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("id", -1)

        // Якщо id збережений, переходимо на MainActivity
        if (userId != -1) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()  // Закриваємо активність, щоб не повертатися до неї
        }
    }

    fun setButtonVisibility() {

        if (isButtonLoginVisible) {
            buttonLogin.visibility = View.VISIBLE
            regText.visibility = View.VISIBLE
            registerButton.visibility = View.INVISIBLE
            logText.visibility = View.INVISIBLE
        } else {
            regText.visibility = View.INVISIBLE
            buttonLogin.visibility = View.INVISIBLE
            registerButton.visibility = View.VISIBLE
            logText.visibility = View.VISIBLE
        }
    }

    // Фонове завдання для реєстрації
    private class RegisterUserTask(
        val context: Context,
        val username: String,
        val password: String,
    ) : AsyncTask<Void, Void, String>() {
        override fun doInBackground(vararg params: Void?): String {
            return try {
                Log.d("MyLog", "Before Connect")
                Class.forName("com.mysql.jdbc.Driver")
                val connection = DriverManager.getConnection(
                    "jdbc:mysql://10.0.2.2:3306/game4cr",
                    "root",
                    "root"
                )
                Log.d("MyLog", "Step Login")

                val query = "INSERT INTO Users (username, password) VALUES (?, ?)"
                Log.d("MyLog", "Step Insert")
                val preparedStatement: PreparedStatement = connection.prepareStatement(query)
                preparedStatement.setString(1, username)
                preparedStatement.setString(2, password) // Захистіть це реальним хешуванням!
                Log.d("MyLog", "Step Pass")

                preparedStatement.executeUpdate()
                preparedStatement.close()
                connection.close()

                "Реєстрація успішна!"
            } catch (e: Exception) {
                Log.d("MyLog", "Error during registration: ${e.message}", e)
                "Помилка: ${e.message}"
            }
        }

        override fun onPostExecute(result: String) {
            Toast.makeText(context, result, Toast.LENGTH_SHORT).show()

        }
    }

    // Фонове завдання для логіну
    private class LoginUserTask(
        val context: Context,
        val username: String,
        val password: String,
    ) : AsyncTask<Void, Void, Int?>() {
        override fun doInBackground(vararg params: Void?): Int? {
            return try {
                Class.forName("com.mysql.jdbc.Driver")
                val connection = DriverManager.getConnection(
                    "jdbc:mysql://10.0.2.2:3306/game4cr",
                    "root",
                    "root"
                )
                val query = "SELECT id FROM Users WHERE username = ? AND password = ?"
                val preparedStatement: PreparedStatement = connection.prepareStatement(query)
                preparedStatement.setString(1, username)
                preparedStatement.setString(
                    2,
                    password
                ) // Рекомендується використовувати хешування!

                val resultSet: ResultSet = preparedStatement.executeQuery()
                val id: Int? = if (resultSet.next()) resultSet.getInt("id") else null

                resultSet.close()
                preparedStatement.close()
                connection.close()

                id
            } catch (e: Exception) {
                null
            }
        }

        override fun onPostExecute(id: Int?) {
            if (id != null) {
                // Збереження id у SharedPreferences
                val sharedPreferences =
                    context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
                sharedPreferences.edit()
                    .putInt("id", id)
                    .apply()

                Toast.makeText(context, "Успішний вхід", Toast.LENGTH_SHORT).show()
                Log.d("MyLogin", "UserId is : $id")

                // Переходимо на MainActivity
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
            } else {
                Toast.makeText(context, "Невірний логін або пароль", Toast.LENGTH_SHORT).show()
            }
        }
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
