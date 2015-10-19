package com.example.dgunda.gridimagesearch.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;


import android.widget.EditText;
import android.widget.GridView;

import com.example.dgunda.gridimagesearch.R;
import com.example.dgunda.gridimagesearch.adapters.ImageResutsAdapter;
import com.example.dgunda.gridimagesearch.models.EndlessScrollListener;
import com.example.dgunda.gridimagesearch.models.ImageResult;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

    private EditText etQueary;
    private GridView gvResults;
    private ArrayList<ImageResult> imageResults;
    private ImageResutsAdapter aImageResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setUpViews();

        //Creating Data Source
        imageResults = new ArrayList<>();
        //Attaching Data Source to an adapter
        aImageResult = new ImageResutsAdapter(this, imageResults);
        //linking adapter to gridview
        gvResults.setAdapter(aImageResult);
        gvResults.setOnScrollListener(new EndlessScrollListener(5,1) {

            @Override
            public boolean getImage(int page, int totalItemCount) {
                return false;
            }
        });


    }

    private void setUpViews()
    {
        etQueary = (EditText)findViewById(R.id.etQueary);
        gvResults = (GridView)findViewById(R.id.gvResults);
        gvResults.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //laund secound activiy
                // Create an intent
                Intent i = new Intent(SearchActivity.this, ImageDisplayActivity.class);
                // get image result
                ImageResult result = imageResults.get(position);
                // pass image result
                i.putExtra("result", result);
                // laund new activity]
                startActivity(i);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
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

    public void getImage(int page)
    {
        String query = etQueary.getText().toString();
        //int page = 1;
        int size = 8;
        String searchUrl = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q="+query+"&start="+page+"&rsz="+size;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(searchUrl, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                JSONArray imageResultJson = null;
                try {
                    imageResultJson = response.getJSONObject("responseData").getJSONArray("results");
                    imageResults.clear();//clearing existing arrayList
                    aImageResult.addAll(ImageResult.fromJsonArray(imageResultJson));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.i("INFO", imageResults.toString());
            }
        });

    }

    public void onImageSearch(View view) {
        getImage(2);
    }
}
