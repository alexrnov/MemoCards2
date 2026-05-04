package alexrnov.animememo.view.activity

import alexrnov.animememo.Initialization.FAVORITES_DB
import alexrnov.animememo.Initialization.STATISTICS_DB
import alexrnov.animememo.Initialization.appStorage
import alexrnov.animememo.R
import alexrnov.animememo.cards.SceneSettings
import alexrnov.animememo.database.favorites.FavoriteEntity
import alexrnov.animememo.database.favorites.FavoritesDatabase
import alexrnov.animememo.render.game.GameSurfaceView
import alexrnov.animememo.database.statistics.StatisticsDatabase
import android.app.ActivityManager
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.edit
import androidx.databinding.DataBindingUtil
import androidx.room.Room.databaseBuilder
import alexrnov.animememo.databinding.ActivityGameBinding
import alexrnov.animememo.database.statistics.StatisticsEntity
import alexrnov.animememo.view.activity.FavoritesMessageType.ADDED
import alexrnov.animememo.view.activity.FavoritesMessageType.ALREADY_ADDED
import alexrnov.animememo.view.activity.FavoritesMessageType.FULL_LIST
import alexrnov.animememo.view.binding.ExitDialogData
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GameActivity : AppCompatActivity() {
    private lateinit var gameSurfaceView: GameSurfaceView
    private var exitDialog: ConstraintLayout? = null

    private lateinit var snackBarContainer: ConstraintLayout
    private var exitDialogData = ExitDialogData()

    private lateinit var favoritesDatabase: FavoritesDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        favoritesDatabase = databaseBuilder(
            applicationContext,
            FavoritesDatabase::class.java, FAVORITES_DB
        ).allowMainThreadQueries().build()

        val frontCardsSize: Int? = getResources().assets.list("front")?.size

        if (frontCardsSize == null) {
            return
        }

        val material = appStorage.getString("material", "pattern") ?: "pattern"
        val backCardsSize: Int? = getResources().assets.list("back/$material")?.size
        if (backCardsSize == null) {
            return
        }

        val sceneSettings = SceneSettings(
            frontCardsSize = frontCardsSize,
            backCardsSize = backCardsSize,
            material = material,
            cardsQuantity = appStorage.getInt("cardsQuantity", 12),
            rotationSpeed = appStorage.getFloat("rotationSpeed", 10f),
            backgroundColor = appStorage.getString("backgroundColor", "black") ?: "black"
        )

        val binding: ActivityGameBinding = DataBindingUtil.setContentView(this, R.layout.activity_game)

        val gameOver = appStorage.getBoolean("gameOver", false)

        if (!gameOver) {
            exitDialogData.update(
                title = getString(R.string.exit_dialog_title_pause),
                dialogText = getString(R.string.exit_dialog_text_pause)
            )
        } else {
            val errors = appStorage.getInt("errors", 0)
            exitDialogData.update(
                title = getString(R.string.statistics),
                dialogText = "${getString(R.string.exit_dialog_text_statistics)} $errors\n${getString(R.string.exit_dialog_text_pause)}"
            )
        }

        binding.exitDialogData = exitDialogData

        if (!isSupportedOpenGLES()) {
            return
        }

        val newGame = appStorage.getBoolean("newGame", true)
        if (newGame) {
            appStorage.edit {
                putInt("firstCardId", -1)
                putInt("firstCardIndex", -1)
                putInt("secondCardIndex", -1)
                putStringSet("openCards", emptySet<String>())
                putInt("errors", 0)
            }
        }

        gameSurfaceView = findViewById(R.id.oglView)
        gameSurfaceView.init(applicationContext, sceneSettings)
        gameSurfaceView.setGameActivity(this)
        snackBarContainer = findViewById(R.id.snackBarContainer)

        exitDialog = findViewById(R.id.exitDialogBackground)
        onBackPressedDispatcher.addCallback(this, callback)

        // добавить прозрачность для статусбара и меню навигации
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onRestart() {
        super.onRestart()
    }

    /**
     * Поскольку minSdk = 24 (Android 7.0), а поддержка OpenGL 3.0 начинается
     * c API level 18 (Jelly Bean), в шейдерах используется версия 3.0
     */
    private fun isSupportedOpenGLES(): Boolean {
        val info = (getSystemService(ACTIVITY_SERVICE) as ActivityManager)
            .deviceConfigurationInfo ?: return false
        return info.reqGlEsVersion >= 0x30000
    }

    val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (exitDialog?.visibility == View.VISIBLE) {
                exitDialog?.visibility = View.GONE
            } else {
                exitDialog?.visibility = View.VISIBLE
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("exitDialogVisibility", exitDialog?.visibility == View.VISIBLE)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val isVisible = savedInstanceState.getBoolean("exitDialogVisibility")
        exitDialog?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun toMainMenu(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        // при возврате в главное меню - отчистить стек переходов
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    fun backToGame(view: View) {
        exitDialog?.visibility = View.GONE
    }

    fun addCardToFavorites(path: String): FavoritesMessageType {
        val requests = favoritesDatabase.requests()
		return when {
			requests.isPathExists(path) -> ALREADY_ADDED
			requests.countFavorites < 6 -> {
				val lastCardId = requests.lastCardId
				val favoriteEntity = FavoriteEntity(lastCardId + 1, path)
				requests.insert(favoriteEntity)
				ADDED
			}
			else -> FULL_LIST
		}
    }

    fun finishGame(errors: Int) {
        appStorage.edit {
            putBoolean("gameOver", true)
        }
        exitDialogData.update(
            title = getString(R.string.statistics),
            dialogText = "${getString(R.string.exit_dialog_text_statistics)} $errors\n${getString(R.string.exit_dialog_text_pause)}"
        )
        exitDialog?.visibility = View.VISIBLE

        val cardsQuantity = appStorage.getInt("cardsQuantity", 12)
        val openCards = appStorage.getStringSet("openCards", emptySet<String>())
        val errors = appStorage.getInt("errors", 0)
        if (cardsQuantity == openCards?.size) {
            val db: StatisticsDatabase = databaseBuilder(
                applicationContext,
                StatisticsDatabase::class.java, STATISTICS_DB
            ).allowMainThreadQueries().build()

            val requests = db.requests()

            val sdf = SimpleDateFormat("yyyy.MM.dd, HH:mm", Locale.getDefault())
            val currentTimeString = sdf.format(Date())

            val lastGameId = requests.lastGameId
            val statisticsEntity =
                StatisticsEntity(lastGameId + 1, currentTimeString, cardsQuantity, errors)
            requests.insertWithLimit(statisticsEntity)
        }
    }

    fun showSnackBar(messageType: FavoritesMessageType) {
        val messageText = getMessageText(messageType)

        val snackBar = Snackbar.make(snackBarContainer, messageText, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        val textView = snackBarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
		textView.textSize = 16F
        textView.setTypeface(null, Typeface.NORMAL)

        val params = snackBarView.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        params.width = FrameLayout.LayoutParams.WRAP_CONTENT // Или фиксированная ширина, например, 800
        params.setMargins(20, 0, 20, 50) // Отступы: слева, сверху, справа, снизу
        snackBarView.layoutParams = params

        snackBar.setTextColor(Color.argb(255, 255, 255, 255))
        snackBar.setBackgroundTint(Color.argb(255, 163, 143, 185))
        snackBar.show()
    }

    private fun getMessageText(messageType: FavoritesMessageType) = when (messageType) {
        ADDED -> getString(R.string.add_card_to_favorites_text)
        ALREADY_ADDED -> getString(R.string.card_is_already_in_favorites_text)
        FULL_LIST -> getString(R.string.full_favorites_list)
    }
}

enum class FavoritesMessageType {
    ADDED,
    ALREADY_ADDED,
    FULL_LIST
}