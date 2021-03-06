package com.redsponge.energygame.util;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Random;

public class GeneralUtils {

    private static final Random random = new Random();

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

    public static <T> T randomFromArr(T[] list) {
        return list[random.nextInt(list.length)];
    }

    public static void playSoundRandomlyPitched(Sound sound) {
        playSoundRandomlyPitched(sound, 0.5f);
    }

    public static void playSoundRandomlyPitched(Sound sound, float vol) {
        sound.play(vol, 0.5f + random.nextFloat(), 0);
    }
}
