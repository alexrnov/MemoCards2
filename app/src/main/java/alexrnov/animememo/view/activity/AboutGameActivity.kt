package alexrnov.animememo.view.activity

import alexrnov.animememo.R
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar

class AboutGameActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		enableEdgeToEdge()
		setContentView(R.layout.activity_about)

		val toolbar = findViewById<MaterialToolbar>(R.id.aboutGameAppBar)
		setSupportActionBar(toolbar)

		supportActionBar?.let {
			it.setDisplayHomeAsUpEnabled(true)
			it.setDisplayShowHomeEnabled(true)
		}

		ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.aboutGameContainer)) { v, insets ->
			val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
			v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
			insets
		}
	}
}