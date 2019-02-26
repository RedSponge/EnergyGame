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

    public static float[] transformAll(float[] vertices, float x, float y) {
        float[] n = new float[vertices.length];
        for(int i = 0; i < n.length; i+=2) {
            n[i] = vertices[i] + x;
            n[i + 1] = vertices[i+1] + y;
        }
        return n;
    }
}
