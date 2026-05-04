package alexrnov.animememo.render.game;

import android.view.GestureDetector;
import android.view.MotionEvent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class GameDetector implements android.view.GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {
    private final GameRenderer renderer;

    public GameDetector(GameRenderer gameRenderer) {
        renderer = gameRenderer;
    }

    @Override
    public boolean onDown(@NonNull MotionEvent e) {
        renderer.openCard(e.getX(), e.getY());
        return true;
    }

    @Override
    public boolean onFling(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public void onLongPress(@NonNull MotionEvent e) {
        renderer.addCardToFavorites(e.getX(), e.getY());
    }

    @Override
    public boolean onDoubleTap(@NonNull MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(@NonNull MotionEvent e) {
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(@NonNull MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
        renderer.cameraPosition(distanceY); // camera rotation
        return true;
    }

    @Override
    public void onShowPress(@NonNull MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(@NonNull MotionEvent e) {
        return false;
    }
}
