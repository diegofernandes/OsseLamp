package br.com.diegofernandes.osselamp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import br.com.diegofernandes.osselamp.widget.ColorCircle;
import br.com.diegofernandes.osselamp.widget.OnColorChangedListener;


public class MainActivity extends ActionBarActivity implements OnColorChangedListener {

    private ColorCircle mColorCircle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mColorCircle = (ColorCircle) findViewById(R.id.colorPicker);
        mColorCircle.setOnColorChangedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onColorChanged(View view, int newColor) {

    }

    @Override
    public void onColorPicked(View view, int newColor) {

    }
}
