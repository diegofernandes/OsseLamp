package br.com.diegofernandes.osselamp.widget;

/**
 * Pure color/geometry helpers used by {@link ColorCircle}.
 *
 * Kept free of android.* classes so it can be covered by plain JVM unit tests.
 */
final class ColorMath {

    private ColorMath() {
    }

    /**
     * Converts a touch position relative to the circle center into a position
     * along the color sweep, in the range [0, 1).
     *
     * 0 is at 3 o'clock and values grow clockwise (screen coordinates, y down),
     * matching {@link android.graphics.SweepGradient}.
     */
    static float angleToUnit(float x, float y) {
        float angle = (float) Math.atan2(y, x);
        // need to turn angle [-PI ... PI] into unit [0....1]
        float unit = angle / (2 * (float) Math.PI);
        if (unit < 0) {
            unit += 1;
        }
        return unit;
    }

    /**
     * Linearly interpolates between evenly spaced ARGB colors.
     *
     * @param colors the gradient stops, evenly spaced over [0, 1]
     * @param unit   position along the gradient; clamped to [0, 1]
     */
    static int interpColor(int[] colors, float unit) {
        if (unit <= 0) {
            return colors[0];
        }
        if (unit >= 1) {
            return colors[colors.length - 1];
        }

        float p = unit * (colors.length - 1);
        int i = (int) p;
        p -= i;

        // now p is just the fractional part [0...1) and i is the index
        int c0 = colors[i];
        int c1 = colors[i + 1];
        int a = ave(channel(c0, 24), channel(c1, 24), p);
        int r = ave(channel(c0, 16), channel(c1, 16), p);
        int g = ave(channel(c0, 8), channel(c1, 8), p);
        int b = ave(channel(c0, 0), channel(c1, 0), p);

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    private static int channel(int color, int shift) {
        return (color >>> shift) & 0xFF;
    }

    private static int ave(int s, int d, float p) {
        return s + Math.round(p * (d - s));
    }
}
