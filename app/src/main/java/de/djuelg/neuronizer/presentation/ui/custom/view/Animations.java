package de.djuelg.neuronizer.presentation.ui.custom.view;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/**
 * Created by Domi on 10.08.2017.
 */

public class Animations {

    public static Animation fadeIn() {
        AlphaAnimation animation = new AlphaAnimation(0, 1);
        animation.setDuration(300);
        animation.setStartOffset(50);
        return animation;
    }

    public static Animation fadeOut() {
        AlphaAnimation animation = new AlphaAnimation(1, 0);
        animation.setDuration(300);
        animation.setStartOffset(50);
        return animation;
    }

    public static Animation slideIn() {
        TranslateAnimation animation = new TranslateAnimation(500, 0, 0, 0);
        animation.setDuration(400);
        animation.setStartOffset(50);
        return animation;
    }

    public static Animation slideOut() {
        TranslateAnimation animation = new TranslateAnimation(0, 500, 0, 0);
        animation.setDuration(400);
        animation.setStartOffset(50);
        return animation;
    }

    public static void registerCircularRevealAnimation(final Context context, final View view, final RevealAnimationSetting revealSettings, final int startColor, final int endColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    v.removeOnLayoutChangeListener(this);
                    int cx = revealSettings.getCenterX();
                    int cy = revealSettings.getCenterY();
                    int width = revealSettings.getWidth();
                    int height = revealSettings.getHeight();
                    int duration = context.getResources().getInteger(android.R.integer.config_mediumAnimTime);

                    //Simply use the diagonal of the view
                    float finalRadius = (float) Math.sqrt(width * width + height * height);
                    Animator anim = ViewAnimationUtils.createCircularReveal(v, cx, cy, 0, finalRadius).setDuration(duration);
                    anim.setInterpolator(new FastOutSlowInInterpolator());
                    anim.start();
                    startColorAnimation(view, startColor, endColor, duration);
                }
            });
        }
    }

    static void startColorAnimation(final View view, final int startColor, final int endColor, int duration) {
        ValueAnimator anim = new ValueAnimator();
        anim.setIntValues(startColor, endColor);
        anim.setEvaluator(new ArgbEvaluator());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view.setBackgroundColor((Integer) valueAnimator.getAnimatedValue());
            }
        });
        anim.setDuration(duration);
        anim.start();
    }
}
