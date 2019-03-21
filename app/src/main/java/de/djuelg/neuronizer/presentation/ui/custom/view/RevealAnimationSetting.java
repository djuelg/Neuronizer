package de.djuelg.neuronizer.presentation.ui.custom.view;

import java.util.Objects;

public class RevealAnimationSetting {

    private int centerX;
    private int centerY;
    private int width;
    private int height;

    public RevealAnimationSetting(int centerX, int centerY, int width, int height) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.width = width;
        this.height = height;
    }

    public int getCenterX() {
        return centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RevealAnimationSetting that = (RevealAnimationSetting) o;
        return centerX == that.centerX &&
                centerY == that.centerY &&
                width == that.width &&
                height == that.height;
    }

    @Override
    public int hashCode() {
        return Objects.hash(centerX, centerY, width, height);
    }

    @Override
    public String toString() {
        return "RevealAnimationSetting{" +
                "centerX=" + centerX +
                ", centerY=" + centerY +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
