package de.djuelg.neuronizer.presentation.ui.custom;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

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
}
