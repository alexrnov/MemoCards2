package alexrnov.animememo.render

import android.opengl.GLES20

fun setBackgroundColor(color: String) {
	when (color) {
		"black" -> GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
		"white" -> GLES20.glClearColor(1.0f, 1.0f, 1.0f, 0.0f)
		else -> GLES20.glClearColor(0.8f, 0.8f, 0.8f, 0.0f)
	}
}



