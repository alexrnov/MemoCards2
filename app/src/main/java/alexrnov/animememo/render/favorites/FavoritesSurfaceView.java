package alexrnov.animememo.render.favorites;

import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

import alexrnov.animememo.view.activity.FavoritesActivity;

public class FavoritesSurfaceView extends GLSurfaceView {
	FavoritesRenderer renderer;
	private GestureDetector detector;

	public FavoritesSurfaceView(Context context) {
		super(context);
	}
	public FavoritesSurfaceView(Context context, AttributeSet attributes) {
		super(context, attributes);
	}

	public void init(Context context) {
		setPreserveEGLContextOnPause(true); // save context OpenGL
		setEGLContextClientVersion(3);
		renderer = new FavoritesRenderer(context);
		setRenderer(renderer);
		detector = new GestureDetector(context, new FavoritesDetector(renderer));
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		if (detector.onTouchEvent(e)) {
			return true;
		}
		return super.onTouchEvent(e);
	}

	public void setFavoritesActivity(FavoritesActivity favoritesActivity) {
		renderer.setFavoritesActivity(favoritesActivity);
	}

	public void update() {
		renderer.update();
	}

	public boolean isOpenLargeCard() {
		return renderer.isOpenLargeCard();
	}

	public void closeLargeCard() {
		renderer.closeLargeCard();
	}
}
