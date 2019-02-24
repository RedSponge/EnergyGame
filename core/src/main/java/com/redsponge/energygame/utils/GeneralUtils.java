package com.redsponge.energygame.utils;

import com.badlogic.gdx.utils.TimeUtils;

public class GeneralUtils {

    public static float secondsSince(long nanos) {
        return TimeUtils.timeSinceNanos(nanos) / 1000000000f;
    }

    public static float[] divideAll(float[] vertices, float pixelsPerMeter) {
        float[] n = new float[vertices.length];
        for(int i = 0; i < n.length; i++) {
            n[i] = vertices[i] / pixelsPerMeter;
        }
        return n;
    }
}
