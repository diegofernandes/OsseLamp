package br.com.diegofernandes.osselamp.widget;

        import android.view.View;

/**
 * Created by diegoossefernandes on 10/06/15.
 */
public interface OnColorChangedListener {
    /**
     * This method is called when the user changed the color.
     *
     * This works in touch mode, by dragging the along the
     * color circle with the finger.
     */
    void onColorChanged(View view, int newColor);

    /**
     * This method is called when the user clicks the center button.
     *
     * @param view
     * @param newColor
     */
    void onColorPicked(View view, int newColor);
}

