package com.happycoderz.cryptfolio.utils;

public class MyBounceInterpolator implements android.view.animation.Interpolator {
    /**
     * The amplitude of the bounces. The higher value (10, for example) produces more pronounced bounces.
     * The lower values (0.1, for example) produce less noticeable wobbles.
     */
    double mAmplitude = 1;

    /**
     * The frequency of the bounces. The higher value produces more wobbles during the animation time period.
     */
    double mFrequency = 10;

    /**
     * Initialize a new interpolator.
     *
     * @param      amplitude   The amplitude of the bounces. The higher value produces more pronounced bounces. The lower values (0.1, for example) produce less noticeable wobbles.
     * @param      frequency   The frequency of the bounces. The higher value produces more wobbles during the animation time period.
     *
     */
    public MyBounceInterpolator(double amplitude, double frequency) {
        mAmplitude = amplitude;
        mFrequency = frequency;
    }

    public float getInterpolation(float time) {
        double amplitude = mAmplitude;
        if (amplitude == 0) { amplitude = 0.05; }

        // The interpolation curve equation:
        //    -e^(-time / amplitude) * cos(frequency * time) + 1
        //
        // View the graph live: https://www.desmos.com/calculator/6gbvrm5i0s
        return (float) (-1 * Math.pow(Math.E, -time/ mAmplitude) * Math.cos(mFrequency * time) + 1);
    }
}
