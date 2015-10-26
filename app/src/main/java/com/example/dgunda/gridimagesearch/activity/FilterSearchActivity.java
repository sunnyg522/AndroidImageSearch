package com.example.dgunda.gridimagesearch.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.dgunda.gridimagesearch.R;

public class FilterSearchActivity extends AppCompatActivity {

    Spinner spImageSize;
    Spinner spColorFilter;
    Spinner spImageType;
    EditText etSiteFilter;
    int RESULTS_OK = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_search);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_filter_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void saveSetting(View view) {
        String imageSize = spImageSize.getSelectedItem().toString();
        String colorFilter = spColorFilter.getSelectedItem().toString();
        String imageType = spImageType.getSelectedItem().toString();
        String siteFilter = etSiteFilter.getText().toString();
        Intent data = new Intent();
        data.putExtra("imageSize", imageSize);
        data.putExtra("colorFilter", colorFilter);
        data.putExtra("imageType", imageType);
        data.putExtra("siteFilter", siteFilter);
        setResult(RESULTS_OK, data);
        finish();
    }
}
