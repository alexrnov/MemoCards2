package alexrnov.animememo.render.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

import alexrnov.animememo.view.activity.GameActivity;
import alexrnov.animememo.cards.SceneSettings;

public class GameSurfaceView extends GLSurfaceView {
    GameRenderer renderer;
    private GestureDetector detector;

    public GameSurfaceView(Context context) {
        super(context);
    }
    public GameSurfaceView(Context context, AttributeSet attributes) {
        super(context, attributes);
    }

    public void init(Context context, SceneSettings sceneSettings) {
        setPreserveEGLContextOnPause(true); // save context OpenGL
        setEGLContextClientVersion(3);
        renderer = new GameRenderer(context, sceneSettings);
        setRenderer(renderer);
        detector = new GestureDetector(context, new GameDetector(renderer));
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (detector.onTouchEvent(e)) {
            return true;
        }
        return super.onTouchEvent(e);
    }

    public void setGameActivity(GameActivity gameActivity) {
        renderer.setGameActivity(gameActivity);
    }
}