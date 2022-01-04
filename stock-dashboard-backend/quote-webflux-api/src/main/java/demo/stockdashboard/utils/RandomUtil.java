package demo.stockdashboard.utils;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomUtil {
    public static int getInt(int max) {
        return getInt(0, max);
    }

    public static long getLong(long max) {
        return getLong(0, max);
    }

    public static float getFloat(float max) {
        return getFloat(0, max);
    }

    public static int getInt(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static long getLong(long min, long max) {
        return ThreadLocalRandom.current().nextLong(min, max);
    }

    public static float getFloat(float min, float max) {
        return new Random().nextFloat() * (max - min) + min;
    }
}
