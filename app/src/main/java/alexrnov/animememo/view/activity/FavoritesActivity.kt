package alexrnov.animememo.view.activity

import alexrnov.animememo.Initialization.FAVORITES_DB
import alexrnov.animememo.R
import alexrnov.animememo.database.favorites.FavoritesDatabase
import alexrnov.animememo.render.favorites.FavoritesSurfaceView
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.room.Room.databaseBuilder

class FavoritesActivity : AppCompatActivity() {
	private var favoritesSurfaceView: FavoritesSurfaceView? = null
	private var confirmRemoveCardDialog: ConstraintLayout? = null

	private var cardPath: String? = null
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		setContentView(R.layout.activity_favorites)

		favoritesSurfaceView = findViewById(R.id.favoritesOglView)
		favoritesSurfaceView?.init(applicationContext)
		favoritesSurfaceView?.setFavoritesActivity(this)

		confirmRemoveCardDialog = findViewById(R.id.confirmRemoveCardDialogBackground)

		onBackPressedDispatcher.addCallback(this, callback)

		// добавить прозрачность для статусбара и меню навигации
		window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
	}

	fun openConfirmRemoveCardDialog(path: String) {
		cardPath = path
		confirmRemoveCardDialog?.visibility = View.VISIBLE
	}

	fun removeCard(view: View) {
		val favoritesDatabase = databaseBuilder(
			applicationContext,
			FavoritesDatabase::class.java, FAVORITES_DB
		).allowMainThreadQueries().build()
		val requests = favoritesDatabase.requests()
		cardPath.let {
			requests.deleteByPath(it)
		}
		confirmRemoveCardDialog?.visibility = View.GONE
		favoritesSurfaceView?.update()
	}

	fun backToGame(view: View) {
		confirmRemoveCardDialog?.visibility = View.GONE
	}

	val callback = object : OnBackPressedCallback(true) {
		override fun handleOnBackPressed() {
			if (favoritesSurfaceView?.isOpenLargeCard == true) {
				favoritesSurfaceView?.closeLargeCard()
			} else {
				if (confirmRemoveCardDialog?.visibility == View.VISIBLE) {
					confirmRemoveCardDialog?.visibility = View.GONE
				} else {
					toMainMenu()
				}
			}
		}
	}

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)

		outState.putBoolean("removeCardDialogVisibility", confirmRemoveCardDialog?.visibility == View.VISIBLE)

		if (cardPath != null) {
			outState.putString("cardPath", cardPath)
		}
	}

	override fun onRestoreInstanceState(savedInstanceState: Bundle) {
		super.onRestoreInstanceState(savedInstanceState)

		val isVisible = savedInstanceState.getBoolean("removeCardDialogVisibility")
		confirmRemoveCardDialog?.visibility = if (isVisible) View.VISIBLE else View.GONE

		val restoreCardPath = savedInstanceState.getString("cardPath")
		cardPath = restoreCardPath
	}

	private fun toMainMenu() {
		val intent = Intent(this, MainActivity::class.java)
		// при возврате в главное меню - отчистить стек переходов
		intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
		startActivity(intent)
	}
}