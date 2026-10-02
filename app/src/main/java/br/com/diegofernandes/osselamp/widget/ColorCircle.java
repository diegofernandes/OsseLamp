package br.com.diegofernandes.osselamp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by diegoossefernandes on 10/06/15.
 */
public class ColorCircle extends View {

    private final static float CENTER_RADIUS_SCALE = 0.4f;
    private final static float DEFAULT_SIZE_DP = 200f;
    private final static float CENTER_STROKE_WIDTH_DP = 2f;

    private static final String STATE_SUPER = "superState";
    private static final String STATE_COLOR = "color";

    private Paint mPaint;
    private Paint mCenterPaint;
    private int[] mColors;
    private OnColorChangedListener mListener;

    private float mCenterX;
    private float mCenterY;
    private float mCenterRadius;
    private float mRingInnerRadius;
    private float mRingOuterRadius;

    private boolean mTrackingCenter;
    private boolean mTrackingRing;
    private boolean mHighlightCenter;


    /**
     * Constructor. This version is only needed for instantiating the object
     * manually (not from a layout XML file).
     *
     * @param context
     */
    public ColorCircle(Context context) {
        super(context);
        init();
    }

    /**
     * Construct object from a layout file.
     *
     * @see android.view.View#View(android.content.Context, android.util.AttributeSet)
     */
    public ColorCircle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ColorCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * Initializes variables.
     */
    private void init() {

        mColors = new int[] {
                0xFFFF0000, 0xFFFF00FF, 0xFF0000FF, 0xFF00FFFF, 0xFF00FF00,
                0xFFFFFF00, 0xFFFF0000
        };
        Shader s = new SweepGradient(0, 0, mColors, null);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setShader(s);
        mPaint.setStyle(Paint.Style.STROKE);

        mCenterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCenterPaint.setStrokeWidth(CENTER_STROKE_WIDTH_DP * getResources().getDisplayMetrics().density);
        mCenterPaint.setColor(0xFFFF0000);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(mCenterX, mCenterY);

        // This is the main "color ring"
        canvas.drawCircle(0, 0, (mRingOuterRadius + mRingInnerRadius) / 2, mPaint);

        // This is the center "activation button" circle
        canvas.drawCircle(0, 0, mCenterRadius, mCenterPaint);

        if (mTrackingCenter) {
            int c = mCenterPaint.getColor();
            mCenterPaint.setStyle(Paint.Style.STROKE);

            if (mHighlightCenter) {
                mCenterPaint.setAlpha(0xFF);
            } else {
                mCenterPaint.setAlpha(0x80);
            }

            // The skinny ring around the center to indicate that it is being pressed
            canvas.drawCircle(0, 0,
                    mCenterRadius + mCenterPaint.getStrokeWidth(),
                    mCenterPaint);

            mCenterPaint.setStyle(Paint.Style.FILL);
            mCenterPaint.setColor(c);
        }
    }


    /**
     * @see android.view.View#measure(int, int)
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxWidth = MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.UNSPECIFIED
                ? Integer.MAX_VALUE : MeasureSpec.getSize(widthMeasureSpec);
        int maxHeight = MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.UNSPECIFIED
                ? Integer.MAX_VALUE : MeasureSpec.getSize(heightMeasureSpec);

        int size = Math.min(maxWidth, maxHeight);
        if (size == Integer.MAX_VALUE) {
            // No constraint on either side (e.g. inside a ScrollView): fall back to a default size
            size = Math.round(DEFAULT_SIZE_DP * getResources().getDisplayMetrics().density);
        }

        setMeasuredDimension(resolveSize(size, widthMeasureSpec),
                resolveSize(size, heightMeasureSpec));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int contentWidth = Math.max(0, w - getPaddingLeft() - getPaddingRight());
        int contentHeight = Math.max(0, h - getPaddingTop() - getPaddingBottom());

        mCenterX = getPaddingLeft() + contentWidth / 2f;
        mCenterY = getPaddingTop() + contentHeight / 2f;

        mRingOuterRadius = Math.min(contentWidth, contentHeight) / 2f;
        mCenterRadius = CENTER_RADIUS_SCALE * mRingOuterRadius;
        // Leave room for the touch feedback ring drawn around the center
        mRingInnerRadius = Math.min(mRingOuterRadius,
                mCenterRadius + 2 * mCenterPaint.getStrokeWidth());

        mPaint.setStrokeWidth(mRingOuterRadius - mRingInnerRadius);
    }

    public void setColor(int color) {
        mCenterPaint.setColor(color);
        invalidate();
    }

    public int getColor() {
        return mCenterPaint.getColor();
    }

    public void setOnColorChangedListener(
            OnColorChangedListener colorListener) {
        mListener = colorListener;
    }

    private void updateColor(float x, float y) {
        int newcolor = ColorMath.interpColor(mColors, ColorMath.angleToUnit(x, y));
        mCenterPaint.setColor(newcolor);

        if (mListener != null) {
            mListener.onColorChanged(this, newcolor);
        }
        invalidate();
    }

    private void stopTracking() {
        if (mTrackingCenter) {
            mTrackingCenter = false;    // so we draw w/o halo
            invalidate();
        }
        mTrackingRing = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX() - mCenterX;
        float y = event.getY() - mCenterY;
        float distance = PointF.length(x, y);
        boolean inCenter = distance <= mCenterRadius;

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mTrackingCenter = inCenter;
                mTrackingRing = !inCenter
                        && distance >= mRingInnerRadius
                        && distance <= mRingOuterRadius;
                if (mTrackingCenter) {
                    mHighlightCenter = true;
                    invalidate();
                } else if (mTrackingRing) {
                    updateColor(x, y);
                } else {
                    // Touch outside the center button and the color ring: not ours
                    return false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mTrackingCenter) {
                    if (mHighlightCenter != inCenter) {
                        mHighlightCenter = inCenter;
                        invalidate();
                    }
                } else if (mTrackingRing) {
                    updateColor(x, y);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mTrackingCenter && inCenter) {
                    performClick();
                }
                stopTracking();
                break;
            case MotionEvent.ACTION_CANCEL:
                stopTracking();
                break;
        }
        return true;
    }

    @Override
    public boolean performClick() {
        super.performClick();
        if (mListener != null) {
            mListener.onColorPicked(this, getColor());
        }
        return true;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle state = new Bundle();
        state.putParcelable(STATE_SUPER, super.onSaveInstanceState());
        state.putInt(STATE_COLOR, getColor());
        return state;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            setColor(bundle.getInt(STATE_COLOR, getColor()));
            state = bundle.getParcelable(STATE_SUPER);
        }
        super.onRestoreInstanceState(state);
    }
}
