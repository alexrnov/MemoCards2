package alexrnov.animememo.view.activity

import alexrnov.animememo.Initialization.appStorage
import alexrnov.animememo.R
import alexrnov.animememo.view.statistics.StatisticsActivity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar

class MainActivity : AppCompatActivity() {
    private lateinit var toolbar: MaterialToolbar
	override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.mainAppBar)
        setSupportActionBar(toolbar)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainContainer)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val gameButton = findViewById<Button>(R.id.gameButton)
        gameButton.setOnClickListener {
            it.isEnabled = false
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
            appStorage.edit {
                putStringSet("cards", emptySet<String>())
                putBoolean("newGame", true)
                putBoolean("gameOver", false)
            }
        }

        val statisticsButton = findViewById<Button>(R.id.statisticsButton)
        statisticsButton.setOnClickListener {
            val intent = Intent(this, StatisticsActivity::class.java)
            startActivity(intent)
        }

        val aboutGameButton = findViewById<Button>(R.id.aboutGameButton)
        aboutGameButton.setOnClickListener {
            val intent = Intent(this, AboutGameActivity::class.java)
            startActivity(intent)
        }

        val settingsButton = findViewById<Button>(R.id.settingsButton)
        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        val favoritesButton = findViewById<Button>(R.id.favoritesButton)
        favoritesButton.setOnClickListener {
            it.isEnabled = false
            appStorage.edit {
                putInt("largeCardIndex", -1)
            }
            val intent = Intent(this, FavoritesActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                finishAffinity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun exit(view: View) = finishAffinity()

}