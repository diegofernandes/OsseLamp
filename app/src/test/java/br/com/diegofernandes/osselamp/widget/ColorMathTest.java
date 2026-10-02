package br.com.diegofernandes.osselamp.widget;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ColorMathTest {

    private static final float DELTA = 1e-5f;

    private static final int[] COLORS = {
            0xFFFF0000, 0xFFFF00FF, 0xFF0000FF, 0xFF00FFFF, 0xFF00FF00,
            0xFFFFFF00, 0xFFFF0000
    };

    @Test
    public void angleToUnit_startsAtThreeOClockAndGrowsClockwise() {
        assertEquals(0f, ColorMath.angleToUnit(1, 0), DELTA);
        // y grows downwards on screen, so (0, 1) is 6 o'clock: a quarter turn clockwise
        assertEquals(0.25f, ColorMath.angleToUnit(0, 1), DELTA);
        assertEquals(0.5f, ColorMath.angleToUnit(-1, 0), DELTA);
        assertEquals(0.75f, ColorMath.angleToUnit(0, -1), DELTA);
    }

    @Test
    public void angleToUnit_isAlwaysInZeroToOneRange() {
        for (int deg = 0; deg < 360; deg += 5) {
            double rad = Math.toRadians(deg);
            float unit = ColorMath.angleToUnit((float) Math.cos(rad), (float) Math.sin(rad));
            assertEquals(true, unit >= 0f && unit < 1f);
        }
    }

    @Test
    public void interpColor_clampsOutOfRangeValues() {
        int[] colors = {0xFF000000, 0xFFFFFFFF};
        assertEquals(0xFF000000, ColorMath.interpColor(colors, -0.5f));
        assertEquals(0xFFFFFFFF, ColorMath.interpColor(colors, 1.5f));
    }

    @Test
    public void interpColor_returnsStopsExactly() {
        for (int i = 0; i < COLORS.length; i++) {
            float unit = (float) i / (COLORS.length - 1);
            assertEquals(COLORS[i], ColorMath.interpColor(COLORS, unit));
        }
    }

    @Test
    public void interpColor_blendsEveryChannelIncludingAlpha() {
        int[] colors = {0x00000000, 0xFF8040C0};
        assertEquals(0x80402060, ColorMath.interpColor(colors, 0.5f));
    }

    @Test
    public void interpColor_halfwayBetweenRedAndMagenta() {
        float unit = 0.5f / (COLORS.length - 1);
        assertEquals(0xFFFF0080, ColorMath.interpColor(COLORS, unit));
    }
}
