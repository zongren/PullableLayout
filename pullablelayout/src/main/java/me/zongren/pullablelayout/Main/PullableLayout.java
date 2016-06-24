package me.zongren.pullablelayout.Main;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import me.zongren.pullablelayout.Constant.Direction;
import me.zongren.pullablelayout.Constant.Result;
import me.zongren.pullablelayout.Constant.Side;
import me.zongren.pullablelayout.Inteface.OnPullListener;
import me.zongren.pullablelayout.Inteface.Pullable;
import me.zongren.pullablelayout.R;

/**
 * Created by 宗仁 on 16/6/13.
 * All Rights Reserved By 宗仁.
 */
public class PullableLayout extends RelativeLayout {
    private static final String TAG = "PullableLayout";
    public PullableComponent topComponent;
    public PullableComponent bottomComponent;
    public PullableComponent leftComponent;
    public PullableComponent rightComponent;
    private int mSide;
    private float mDistanceRatio;
    private float mVerticalDirectionRatio;
    private float mHorizontalDirectionRatio;
    private int mComponentCount = 0;
    private View mPullableView;
    private Direction mCurrentDirection = Direction.NONE;
    private float mTouchDownX;
    private float mTouchDownY;
    private OnPullListener mOnPullListener;
    private Handler mHandler;
    private Runnable mRunnable;
    //Only one component will show up when I touch the screen,which makes sense to me.
    private PullableComponent mCurrentComponent;

    public PullableLayout(Context context) {
        this(context, null);
    }

    public PullableLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullableLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.pullablelayout_config, 0, 0);
        mSide = array.getInt(R.styleable.pullablelayout_config_pullablelayout_side, Side.TOP.getValue());
        mDistanceRatio = array.getFloat(R.styleable.pullablelayout_config_pullablelayout_distanceRatio, 2);
        mVerticalDirectionRatio = array.getFloat(R.styleable.pullablelayout_config_pullablelayout_verticalAngle, 3);
        mHorizontalDirectionRatio = array.getFloat(R.styleable.pullablelayout_config_pullablelayout_horizontalAngle, 3);
        array.recycle();
        mHandler = new Handler(Looper.getMainLooper());
        mRunnable = new Runnable() {
            @Override
            public void run() {
                mCurrentComponent.finish(Result.FAILED);
            }
        };
        mOnPullListener = new OnPullListener() {
            @Override
            public void onCanceled(final PullableComponent pullableComponent) {
                mHandler.removeCallbacks(mRunnable);
            }

            @Override
            public void onLoading(final PullableComponent pullableComponent) {
                mHandler.postDelayed(mRunnable, 1000);
            }

            @Override
            public void onSizeChanged(final PullableComponent pullableComponent, int mSize) {

            }
        };
        if ((mSide & Side.TOP.getValue()) > 0) {
            topComponent = new PullableComponent(this, R.layout.pullablelayout_top_component, Side.TOP);
            topComponent.setOnPullListener(mOnPullListener);
            addView(topComponent.getView());
            mComponentCount++;
        }
        if ((mSide & Side.LEFT.getValue()) > 0) {
            leftComponent = new PullableComponent(this, R.layout.pullablelayout_left_component, Side.LEFT);
            addView(leftComponent.getView());
            leftComponent.setOnPullListener(mOnPullListener);
            mComponentCount++;
        }
        if ((mSide & Side.BOTTOM.getValue()) > 0) {
            bottomComponent = new PullableComponent(this, R.layout.pullablelayout_bottom_component, Side.BOTTOM);
            addView(bottomComponent.getView());
            bottomComponent.setOnPullListener(mOnPullListener);
            mComponentCount++;
        }
        if ((mSide & Side.RIGHT.getValue()) > 0) {
            rightComponent = new PullableComponent(this, R.layout.pullablelayout_right_component, Side.RIGHT);
            addView(rightComponent.getView());
            rightComponent.setOnPullListener(mOnPullListener);
            mComponentCount++;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if (mCurrentComponent != null) {
                    mCurrentComponent.recordTouchDownSize();
                    mCurrentComponent.setTouching(true);
                    if (mCurrentComponent.getSize() <= 0) {
                        mCurrentComponent = null;
                    }
                }
                mTouchDownX = ev.getX();
                mTouchDownY = ev.getY();
                super.dispatchTouchEvent(ev);
                return true;
            case MotionEvent.ACTION_MOVE:
                if (handleTouchMove(ev)) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mCurrentComponent != null) {
                    mCurrentComponent.release();
                    mCurrentComponent.setTouching(false);
                    return true;
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mPullableView == null) {
            for (int i = 0; i < getChildCount(); i++) {
                View view = getChildAt(i);
                if (view instanceof Pullable) {
                    mPullableView = view;
                }
            }
            if (getChildCount() != (mComponentCount + 1)) {
                throw new IllegalStateException("PullableLayout has to hold only exactly one child view!");
            }
            if (!(mPullableView instanceof Pullable)) {
                throw new IllegalStateException("Child View must implement Pullable interface!");
            }
        }

        if (mCurrentComponent == null && topComponent != null && topComponent.getSize() > 0) {
            mCurrentComponent = topComponent;
        } else if (mCurrentComponent == null && leftComponent != null && leftComponent.getSize() > 0) {
            mCurrentComponent = leftComponent;
        } else if (mCurrentComponent == null && bottomComponent != null && bottomComponent.getSize() > 0) {
            mCurrentComponent = bottomComponent;
        } else if (mCurrentComponent == null && rightComponent != null && rightComponent.getSize() > 0) {
            mCurrentComponent = rightComponent;
        }
        int width = r - l;
        int height = b - t;

        int pullableViewLeft = getPaddingLeft();
        int pullableViewTop = getPaddingLeft();
        int pullableViewWidth = mPullableView.getMeasuredWidth();
        int pullableViewHeight = mPullableView.getMeasuredHeight();

        if (mCurrentComponent != null) {
            int pullableComponentLeft = getPaddingLeft();
            int pullableComponentTop = getPaddingTop();
            int pullableComponentWidth = mCurrentComponent.getView().getMeasuredWidth();
            int pullableComponentHeight = mCurrentComponent.getView().getMeasuredHeight();
            int pullableComponentSize = mCurrentComponent.getSize();
            switch (mCurrentComponent.getSide()) {
                case TOP:
                    pullableComponentTop += pullableComponentSize - pullableComponentHeight;
                    pullableViewTop += pullableComponentSize;
                    //layout other components on other sides
                    if (leftComponent != null) {
                        leftComponent.getView().layout(-leftComponent.getView().getMeasuredWidth(), 0, 0, leftComponent.getView().getMeasuredHeight());
                    }
                    if (bottomComponent != null) {
                        bottomComponent.getView().layout(0, height, bottomComponent.getView().getMeasuredWidth(), height + bottomComponent.getView().getMeasuredHeight());
                    }
                    if (rightComponent != null) {
                        rightComponent.getView().layout(width, 0, width + rightComponent.getView().getMeasuredWidth(), rightComponent.getView().getMeasuredHeight());
                    }
                    break;
                case LEFT:
                    pullableComponentLeft += pullableComponentSize - pullableComponentWidth;
                    pullableViewLeft += pullableComponentSize;
                    //layout other components on other sides
                    if (topComponent != null) {
                        topComponent.getView().layout(0, -topComponent.getView().getMeasuredHeight(), topComponent.getView().getMeasuredWidth(), 0);
                    }
                    if (bottomComponent != null) {
                        bottomComponent.getView().layout(0, height, bottomComponent.getView().getMeasuredWidth(), height + bottomComponent.getView().getMeasuredHeight());
                    }
                    if (rightComponent != null) {
                        rightComponent.getView().layout(width, 0, width + rightComponent.getView().getMeasuredWidth(), rightComponent.getView().getMeasuredHeight());
                    }
                    break;
                case BOTTOM:
                    pullableComponentTop += height - pullableComponentSize;
                    pullableViewTop -= pullableComponentSize;
                    //layout other components on other sides
                    if (topComponent != null) {
                        topComponent.getView().layout(0, -topComponent.getView().getMeasuredHeight(), topComponent.getView().getMeasuredWidth(), 0);
                    }
                    if (leftComponent != null) {
                        leftComponent.getView().layout(-leftComponent.getView().getMeasuredWidth(), 0, 0, leftComponent.getView().getMeasuredHeight());
                    }
                    if (rightComponent != null) {
                        rightComponent.getView().layout(width, 0, width + rightComponent.getView().getMeasuredWidth(), rightComponent.getView().getMeasuredHeight());
                    }
                    break;
                case RIGHT:
                    pullableComponentLeft += width - pullableComponentSize;
                    pullableViewLeft -= pullableComponentSize;
                    //layout other components on other sides
                    if (topComponent != null) {
                        topComponent.getView().layout(0, -topComponent.getView().getMeasuredHeight(), topComponent.getView().getMeasuredWidth(), 0);
                    }
                    if (leftComponent != null) {
                        leftComponent.getView().layout(-leftComponent.getView().getMeasuredWidth(), 0, 0, leftComponent.getView().getMeasuredHeight());
                    }
                    if (bottomComponent != null) {
                        bottomComponent.getView().layout(0, height, bottomComponent.getView().getMeasuredWidth(), height + bottomComponent.getView().getMeasuredHeight());
                    }
                    break;
            }

            int pullableComponentRight = pullableComponentLeft + pullableComponentWidth;
            int pullableComponentBottom = pullableComponentTop + pullableComponentHeight;

            mCurrentComponent.getView().layout(pullableComponentLeft, pullableComponentTop, pullableComponentRight, pullableComponentBottom);
        } else {
            if (topComponent != null) {
                topComponent.getView().layout(0, -topComponent.getView().getMeasuredHeight(), topComponent.getView().getMeasuredWidth(), 0);
            }
            if (leftComponent != null) {
                leftComponent.getView().layout(-leftComponent.getView().getMeasuredWidth(), 0, 0, leftComponent.getView().getMeasuredHeight());
            }
            if (bottomComponent != null) {
                bottomComponent.getView().layout(0, height, bottomComponent.getView().getMeasuredWidth(), height + bottomComponent.getView().getMeasuredHeight());
            }
            if (rightComponent != null) {
                rightComponent.getView().layout(width, 0, width + rightComponent.getView().getMeasuredWidth(), rightComponent.getView().getMeasuredHeight());
            }
        }

        int pullableViewBottom = pullableViewTop + pullableViewHeight;
        int pullableViewRight = pullableViewLeft + pullableViewWidth;

        mPullableView.layout(pullableViewLeft, pullableViewTop, pullableViewRight, pullableViewBottom);
    }

    private boolean handleTouchMove(MotionEvent ev) {
        boolean intercept = true;
        float currentX = ev.getX();
        float currentY = ev.getY();
        float offsetX = currentX - mTouchDownX;
        float offsetY = currentY - mTouchDownY;
        float absOffsetX = Math.abs(offsetX);
        float absOffsetY = Math.abs(offsetY);
        //If a pullable component already appears on the screen,
        //intercept this event and add offset to its size
        if (mCurrentComponent != null) {
            switch (mCurrentComponent.getSide()) {
                case TOP:
                    mCurrentComponent.addSize((int) (offsetY / mDistanceRatio));
                    break;
                case BOTTOM:
                    mCurrentComponent.addSize(-(int) (offsetY / mDistanceRatio));
                    break;
                case LEFT:
                    mCurrentComponent.addSize((int) (offsetX / mDistanceRatio));
                    break;
                case RIGHT:
                    mCurrentComponent.addSize(-(int) (offsetX / mDistanceRatio));
                    break;
            }
            //Check if component's size become zero,if so,stop intercepting the event.
            if (mCurrentComponent.getSize() <= 0) {
                mTouchDownX = ev.getX();
                mTouchDownY = ev.getY();
                mCurrentComponent.clearTouchDownSize();
                mCurrentComponent = null;
                intercept = false;
            } else {
                intercept = true;
            }
        }
        //If no pullable component on the screen,pick one.
        if (mCurrentComponent == null) {
            if (offsetX == 0) {
                if (offsetY > 0) {
                    mCurrentDirection = Direction.FROM_TOP_TO_BOTTOM;
                } else if (offsetY < 0) {
                    mCurrentDirection = Direction.FROM_BOTTOM_TO_TOP;
                }
            } else if (offsetY == 0) {
                if (offsetX > 0) {
                    mCurrentDirection = Direction.FROM_LEFT_TO_RIGHT;
                } else if (offsetX < 0) {
                    mCurrentDirection = Direction.FROM_RIGHT_TO_LEFT;
                }
            } else {
                if (absOffsetX >= absOffsetY) {
                    if (absOffsetX / absOffsetY >= mHorizontalDirectionRatio) {
                        if (offsetX > 0) {
                            mCurrentDirection = Direction.FROM_LEFT_TO_RIGHT;
                        } else if (offsetX < 0) {
                            mCurrentDirection = Direction.FROM_RIGHT_TO_LEFT;
                        }
                    } else {
                        mCurrentDirection = Direction.NONE;
                    }
                } else {
                    if (absOffsetY / absOffsetX >= mVerticalDirectionRatio) {
                        if (offsetY > 0) {
                            mCurrentDirection = Direction.FROM_TOP_TO_BOTTOM;
                        } else if (offsetY < 0) {
                            mCurrentDirection = Direction.FROM_BOTTOM_TO_TOP;
                        }
                    } else {
                        mCurrentDirection = Direction.NONE;
                    }
                }
            }
            switch (mCurrentDirection) {
                case FROM_TOP_TO_BOTTOM:
                    if (topComponent != null && ((Pullable) mPullableView).reachEdgeOfSide(Side.TOP)) {
                        mCurrentComponent = topComponent;
                        intercept = true;
                    } else {
                        intercept = false;
                    }
                    break;
                case FROM_BOTTOM_TO_TOP:
                    if (bottomComponent != null && ((Pullable) mPullableView).reachEdgeOfSide(Side.BOTTOM)) {
                        mCurrentComponent = bottomComponent;
                        intercept = true;
                    } else {
                        intercept = false;
                    }
                    break;
                case FROM_LEFT_TO_RIGHT:
                    if (leftComponent != null && ((Pullable) mPullableView).reachEdgeOfSide(Side.LEFT)) {
                        mCurrentComponent = leftComponent;
                        intercept = true;
                    } else {
                        intercept = false;
                    }
                    break;
                case FROM_RIGHT_TO_LEFT:
                    if (rightComponent != null && ((Pullable) mPullableView).reachEdgeOfSide(Side.RIGHT)) {
                        mCurrentComponent = rightComponent;
                        intercept = true;
                    } else {
                        intercept = false;
                    }
                    break;
                case NONE:
                default:
                    intercept = false;
                    break;
            }
            if (intercept) {
                if (mCurrentComponent != null) {
                    mTouchDownX = ev.getX();
                    mTouchDownY = ev.getY();
                    offsetX = currentX - mTouchDownX;
                    offsetY = currentY - mTouchDownY;
                    absOffsetX = Math.abs(offsetX);
                    absOffsetY = Math.abs(offsetY);
                    switch (mCurrentComponent.getSide()) {
                        case TOP:
                            mCurrentComponent.setSize((int) (absOffsetY / mDistanceRatio));
                            break;
                        case LEFT:
                            mCurrentComponent.setSize((int) (absOffsetX / mDistanceRatio));
                            break;
                        case BOTTOM:
                            mCurrentComponent.setSize((int) (absOffsetY / mDistanceRatio));
                            break;
                        case RIGHT:
                            mCurrentComponent.setSize((int) (absOffsetX / mDistanceRatio));
                            break;
                    }
                }
            }
        }
        return intercept;
    }

}