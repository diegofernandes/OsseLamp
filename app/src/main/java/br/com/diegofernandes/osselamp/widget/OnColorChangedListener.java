package br.com.diegofernandes.osselamp.widget;

import android.view.View;

/**
 * Created by diegoossefernandes on 10/06/15.
 */
public interface OnColorChangedListener {
    /**
     * This method is called when the user changed the color.
     *
     * This works in touch mode, by dragging the finger along the
     * color circle.
     *
     * @param view the {@link ColorCircle} that changed
     * @param newColor the new ARGB color
     */
    void onColorChanged(View view, int newColor);

    /**
     * This method is called when the user clicks the center button.
     *
     * @param view the {@link ColorCircle} that was clicked
     * @param newColor the selected ARGB color
     */
    void onColorPicked(View view, int newColor);
}

