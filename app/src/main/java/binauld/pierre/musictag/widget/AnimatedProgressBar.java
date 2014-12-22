package binauld.pierre.musictag.widget;


import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ProgressBar;

/**
 * An animated progress bar. It allow to increment the progress bar smoother.
 */
public class AnimatedProgressBar extends ProgressBar {

    private static final Interpolator DEFAULT_INTERPOLATOR = new AccelerateDecelerateInterpolator();

    private ValueAnimator animator;
    private ValueAnimator animatorSecondary;
    private boolean animate = true;

    public AnimatedProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public AnimatedProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimatedProgressBar(Context context) {
        super(context);
    }

    public boolean isAnimate() {
        return animate;
    }

    public void setAnimate(boolean animate) {
        this.animate = animate;
    }

    @Override
    public synchronized void setProgress(int progress) {
        if (!animate && android.os.Build.VERSION.SDK_INT < 11) {
            super.setProgress(progress);
            return;
        }
        if (animator != null)
            animator.cancel();
        if (animator == null) {
            animator = ValueAnimator.ofInt(getProgress(), progress);
            animator.setInterpolator(DEFAULT_INTERPOLATOR);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    AnimatedProgressBar.super.setProgress((Integer) animation.getAnimatedValue());
                }
            });
        } else
            animator.setIntValues(getProgress(), progress);
        animator.start();

    }

    @Override
    public synchronized void setSecondaryProgress(int secondaryProgress) {
        if (!animate && android.os.Build.VERSION.SDK_INT < 11) {
            super.setSecondaryProgress(secondaryProgress);
            return;
        }
        if (animatorSecondary != null)
            animatorSecondary.cancel();
        if (animatorSecondary == null) {
            animatorSecondary = ValueAnimator.ofInt(getProgress(), secondaryProgress);
            animatorSecondary.setInterpolator(DEFAULT_INTERPOLATOR);
            animatorSecondary.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    AnimatedProgressBar.super.setSecondaryProgress((Integer) animation
                            .getAnimatedValue());
                }
            });
        } else
            animatorSecondary.setIntValues(getProgress(), secondaryProgress);
        animatorSecondary.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (animator != null)
            animator.cancel();
        if (animatorSecondary != null)
            animatorSecondary.cancel();
    }

}