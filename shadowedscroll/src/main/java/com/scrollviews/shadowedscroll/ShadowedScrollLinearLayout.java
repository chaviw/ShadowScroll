package com.scrollviews.shadowedscroll;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.scollviews.shadowedscroll.R;

import static android.R.attr.scrollY;

/**
 * Created by chavi on 1/24/17.
 */
public class ShadowedScrollLinearLayout extends LinearLayout {
    private static final int DEFAULT_ELEVATION_HEIGHT_DP = 10;
    private static final String DEFAULT_GRADIENT_SHADOW_COLOR = "#1A000000";

    private float elevationHeight;
    private Drawable shadowBg;

    private View shadowView;
    private FrameLayout bodyWithShadow;
    int orientation = VERTICAL;
    ColorStateList shadowColor;

    private ObjectAnimator hideShadowAnimator;
    private ObjectAnimator showShadowAnimator;

    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            prepareScrollView();
            getViewTreeObserver().removeOnGlobalLayoutListener(globalLayoutListener);
        }
    };

    public ShadowedScrollLinearLayout(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public ShadowedScrollLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public ShadowedScrollLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    public ShadowedScrollLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    public Drawable getShadowBg() {
        return shadowBg;
    }

    public void setShadowBg(Drawable shadowBg) {
        this.shadowBg = shadowBg;
        shadowView.setBackground(this.shadowBg);
    }

    public void setShadowBg(@DrawableRes int shadowBg) {
        this.shadowBg = getContext().getDrawable(shadowBg);
        shadowView.setBackground(this.shadowBg);
    }

    public float getElevationHeight() {
        return elevationHeight;
    }

    public void setElevationHeight(float elevationHeight) {
        this.elevationHeight = elevationHeight;
        shadowView.getLayoutParams().height = (int) elevationHeight;
        shadowView.requestLayout();
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        orientation = VERTICAL;
        elevationHeight = dpToPixel(context, DEFAULT_ELEVATION_HEIGHT_DP);

        if (attrs != null) {
            TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.ShadowedScrollLinearLayout,
                    defStyleAttr, defStyleRes);

            elevationHeight = typedArray.getDimension(R.styleable.ShadowedScrollLinearLayout_shadowElevation, elevationHeight);
            shadowBg = typedArray.getDrawable(R.styleable.ShadowedScrollLinearLayout_shadowDrawable);
            orientation = typedArray.getInt(R.styleable.ShadowedScrollLinearLayout_android_orientation, orientation);
            shadowColor = typedArray.getColorStateList(R.styleable.ShadowedScrollLinearLayout_shadowColor);
        }

        if (shadowBg == null) {
            if (shadowColor != null) {
                shadowBg = createDefaultShadowBgWithColor(shadowColor.getDefaultColor());
            } else {
                shadowBg = createDefaultShadowBg();
            }
        }

        bodyWithShadow = new FrameLayout(context);
        bodyWithShadow.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        shadowView = new View(context);

        switch (orientation) {
            case VERTICAL:
            default:
                shadowView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) elevationHeight));
                setOrientation(VERTICAL);
                break;
            case HORIZONTAL:
                shadowView.setLayoutParams(new LinearLayout.LayoutParams((int) elevationHeight, ViewGroup.LayoutParams.MATCH_PARENT));
                setOrientation(HORIZONTAL);
                break;
        }

        shadowView.setBackground(shadowBg);
        bodyWithShadow.addView(shadowView);
        shadowView.setAlpha(0);

        createHideShadowAnimator();
        createShowShadowAnimator();
        getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);

    }

    private void prepareScrollView() {
        int childWithScrollIndex = findChildWithScrollable();

        if (childWithScrollIndex == -1) {
            childWithScrollIndex = 1;
        }

        View childWithScroll = getChildAt(childWithScrollIndex);

        if (childWithScroll == null) {
            return;
        }

        removeView(childWithScroll);
        bodyWithShadow.addView(childWithScroll, 0);
        addView(bodyWithShadow, childWithScrollIndex);

        if (childWithScroll instanceof RecyclerView) {
            setUpRecyclerView((RecyclerView) childWithScroll);
        } else if (childWithScroll instanceof AbsListView) {
            setUpAbsListView((AbsListView) childWithScroll);
        } else {
            setUpScrollView(childWithScroll);
        }
    }

    private void setUpRecyclerView(@NonNull RecyclerView recyclerView) {

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int scrollY = recyclerView.computeVerticalScrollOffset();
                int scrollX = recyclerView.computeHorizontalScrollOffset();

                switch (orientation) {
                    case VERTICAL:
                    default:
                        addShadowIfScrolledVertically(scrollY);
                        break;
                    case HORIZONTAL:
                        addShadowIfScrolledHorizontally(scrollX);
                        break;
                }
            }
        });
    }

    private void setUpAbsListView(@NonNull AbsListView absListView) {
        absListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                View v = absListView.getChildAt(0);
                int scrollY = (v == null) ? 0 : (v.getTop() - absListView.getPaddingTop());
                int scrollX = (v == null) ? 0 : (v.getLeft() - absListView.getPaddingLeft());

                switch (orientation) {
                    case VERTICAL:
                    default:
                        addShadowIfScrolledVertically(scrollY);
                        break;
                    case HORIZONTAL:
                        addShadowIfScrolledHorizontally(scrollX);
                        break;
                }
            }
        });
    }

    private void setUpScrollView(@NonNull final View view) {
        view.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = view.getScrollY();
                int scrollX = view.getScrollX();

                switch (orientation) {
                    case VERTICAL:
                    default:
                        addShadowIfScrolledVertically(scrollY);
                        break;
                    case HORIZONTAL:
                        addShadowIfScrolledHorizontally(scrollX);
                        break;
                }
            }
        });
    }

    private void addShadowIfScrolledVertically(int scrollY) {
        if (scrollY == 0) {
            hideShadow();
        } else {
            showShadow();
        }
    }

    private void addShadowIfScrolledHorizontally(int scrollX) {
        if (scrollX == 0) {
            hideShadow();
        } else {
            showShadow();
        }
    }

    private void hideShadow() {
        if (shadowView.getAlpha() == 0) {
            return;
        }

        if (isAnimatorRunning(hideShadowAnimator)) {
            return;
        }

        if (isAnimatorRunning(showShadowAnimator)) {
            showShadowAnimator.cancel();
        }

        hideShadowAnimator.setFloatValues(shadowView.getAlpha(), 0);
        hideShadowAnimator.start();
    }

    private void showShadow() {
        if (shadowView.getAlpha() == 1) {
            return;
        }

        if (isAnimatorRunning(showShadowAnimator)) {
            return;
        }

        if (isAnimatorRunning(hideShadowAnimator)) {
            hideShadowAnimator.cancel();
        }

        showShadowAnimator.setFloatValues(shadowView.getAlpha(), 1);
        showShadowAnimator.start();
    }

    private Drawable createDefaultShadowBgWithColor(int startColor) {
        int endColor = Color.TRANSPARENT;

        GradientDrawable.Orientation gradientOrientation;
        switch (orientation) {
            case VERTICAL:
            default:
                gradientOrientation = GradientDrawable.Orientation.BOTTOM_TOP;
                break;
            case HORIZONTAL:
                gradientOrientation = GradientDrawable.Orientation.RIGHT_LEFT;
                break;
        }
        return new GradientDrawable(gradientOrientation, new int[]{endColor, startColor});
    }

    private Drawable createDefaultShadowBg() {
        int startColor = Color.parseColor(DEFAULT_GRADIENT_SHADOW_COLOR);
        return createDefaultShadowBgWithColor(startColor);
    }

    private void createHideShadowAnimator() {
        hideShadowAnimator = ObjectAnimator.ofFloat(shadowView, "alpha", 1, 0);
        hideShadowAnimator.setDuration(200);
    }

    private void createShowShadowAnimator() {
        showShadowAnimator = ObjectAnimator.ofFloat(shadowView, "alpha", 0, 1);
        showShadowAnimator.setDuration(200);
    }

    private static int dpToPixel(@NonNull Context context, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    private static boolean isAnimatorRunning(Animator animator) {
        return animator != null && (animator.isStarted() || animator.isRunning());
    }

    private int findChildWithScrollable() {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            ViewGroup.LayoutParams childParams = child.getLayoutParams();
            if (childParams instanceof LayoutParams) {
                if (((LayoutParams) childParams).scrollable) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    public static class LayoutParams extends LinearLayout.LayoutParams {
        private boolean scrollable;

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(int width, int height, float weight) {
            super(width, height, weight);
        }

        public LayoutParams(ViewGroup.LayoutParams p) {
            super(p);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(LinearLayout.LayoutParams source) {
            super(source);
        }

        public LayoutParams(Context context, AttributeSet attrs) {
            super(context, attrs);

            if (attrs != null) {
                TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                        attrs,
                        R.styleable.ShadowedScrollLinearLayout_LayoutParams,
                        0, 0);

                scrollable = typedArray.getBoolean(R.styleable.ShadowedScrollLinearLayout_LayoutParams_scrollable, false);
            }
        }
    }
}