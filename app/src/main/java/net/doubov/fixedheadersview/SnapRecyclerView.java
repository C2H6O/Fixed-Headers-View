package net.doubov.fixedheadersview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class SnapRecyclerView extends RecyclerView {

    public static final boolean DEBUG = false;

    private float mDownX;
    private float mDownY;

    public static final int VELOCITY_SNAP_THRESHOLD = 9000;

    public static final String TAG = SnapRecyclerView.class.getSimpleName();

    public SnapRecyclerView(Context context) {
        super(context);
    }

    public SnapRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SnapRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
//        final LayoutManager lm = getLayoutManager();
//        /*
//        If the LayoutManager has a snap behaviour, calculate the final position based on the
//        X/Y velocities and scroll to that position
//         */
//        if (lm instanceof ISnappyLayoutManager) {
//
//            if (velocityX != 0 && Math.abs(velocityX) < VELOCITY_SNAP_THRESHOLD) {
//                int position = ((ISnappyLayoutManager) lm).getPositionForVelocity(velocityX);
//                super.smoothScrollToPosition(position);
//                return true;
//            }
//        }
        return super.fling(velocityX, velocityY);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {

//        Log.d(TAG, "onInterceptTouchEvent");
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                Log.d(TAG, "onInterceptTouchEvent ACTION_DOWN");
                onPointerDown(e);
                break;
            case MotionEvent.ACTION_MOVE:
//                Log.d(TAG, "onInterceptTouchEvent ACTION_MOVE");
                onPointerMove(e);
                break;

        }

        boolean ret = super.onInterceptTouchEvent(e);

        switch (e.getAction()) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
//                Log.d(TAG, "INTERCEPT CANCEL, UP");
                onPointerUp(e);
        }

        return ret;
    }

    private void onPointerMove(MotionEvent e) {
        final LayoutManager lm = getLayoutManager();

        if (lm instanceof FixedGridLayoutManager) {
            FixedGridLayoutManager fglm = (FixedGridLayoutManager) lm;
//            Log.d(TAG, "getScrollState: " + getScrollState());
            float deltaX = e.getX() - mDownX;
            float deltaY = e.getY() - mDownY;
            if (Math.abs(deltaX) > Math.abs(deltaY)) {
                fglm.setCanScrollHorizontally(true);
                fglm.setCanScrollVertically(false);
            } else {
                fglm.setCanScrollHorizontally(false);
                fglm.setCanScrollVertically(true);
            }
        }
    }

    private void onPointerDown(MotionEvent e) {
        final LayoutManager lm = getLayoutManager();

        if (lm instanceof FixedGridLayoutManager) {

            FixedGridLayoutManager fglm = (FixedGridLayoutManager) lm;

            mDownX = e.getX();
            mDownY = e.getY();
            // disallow scrolling until we gather more data
            fglm.setCanScrollHorizontally(false);
            fglm.setCanScrollVertically(false);
        }
    }

    private void onPointerUp(MotionEvent e) {
        final LayoutManager lm = getLayoutManager();

        if (lm instanceof FixedGridLayoutManager) {
            FixedGridLayoutManager fglm = (FixedGridLayoutManager) lm;

            fglm.setCanScrollHorizontally(true);
            fglm.setCanScrollVertically(true);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

//            Log.d(TAG, "FixedGridLayoutManager");

        switch (e.getAction()) {
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_DOWN:
                if (DEBUG) {
                    Log.d(TAG, "ACTION_DOWN");
                }
                onPointerDown(e);
                break;
            case MotionEvent.ACTION_MOVE:
                if (DEBUG) {
                    Log.d(TAG, "MOVE: " + getScrollState());
                }
                // if we aren't already dragging/scrolling, we can set the scroll direction
                onPointerMove(e);
                break;
        }


        // We want the parent to handle all touch events--there's a lot going on there,
        // and there is no reason to overwrite that functionality--bad things will happen.
        final boolean ret = super.onTouchEvent(e);

//        final LayoutManager lm = getLayoutManager();
//
//        if (lm instanceof ISnappyLayoutManager
//                && (e.getAction() == MotionEvent.ACTION_UP ||
//                e.getAction() == MotionEvent.ACTION_CANCEL)
//                && getScrollState() == SCROLL_STATE_IDLE) {
//            // The layout manager is a SnappyLayoutManager, which means that the
//            // children should be snapped to a grid at the end of a drag or
//            // fling. The motion event is either a user lifting their finger or
//            // the cancellation of a motion events, so this is the time to take
//            // over the scrolling to perform our own functionality.
//            // Finally, the scroll state is idle--meaning that the resultant
//            // velocity after the user's gesture was below the threshold, and
//            // no fling was performed, so the view may be in an unaligned state
//            // and will not be flung to a proper state.
//            smoothScrollToPosition(((ISnappyLayoutManager) lm).getFixScrollPos());
//        }

        //
        switch (e.getAction()) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (DEBUG) {
                    Log.d(TAG, "CANCEL, UP");
                }
                onPointerUp(e);
        }

        return ret;
    }
}
