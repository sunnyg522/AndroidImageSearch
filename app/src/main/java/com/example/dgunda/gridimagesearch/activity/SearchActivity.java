package com.example.dgunda.gridimagesearch.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;


import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

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
    private Button btSearch;
    private ArrayList<ImageResult> imageResults;
    private ImageResutsAdapter aImageResult;

    String gFilterImageSize = "";
    String gFilterColorFilter = "";
    String gFilterImageType  = "";
    String gFilterSite  = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setUpViews();
    }

    private void setUpViews()
    {
        btSearch = (Button)findViewById(R.id.btSearch);
        etQueary = (EditText)findViewById(R.id.etQueary);
        gvResults = (GridView)findViewById(R.id.gvResults);
        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                int offset = page * 8;
                getImage(offset, etQueary.getText().toString());
                return true;
            }
        });
        imageResults = new ArrayList<>();
        aImageResult = new ImageResutsAdapter(this, R.id.gvResults ,imageResults);
        gvResults.setAdapter(aImageResult);
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
    private void showFilter_display()
    {
        Intent i = new Intent(SearchActivity.this, FilterDisplay.class);
        // get image result
        String result = "test";
        //ImageResult result = imageResults.get(position);
        // pass image result
        i.putExtra("result", result);
        // laund new activity]
        startActivity(i);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        //showFilter_display();
        return true;
    }
    public void showSettingsScreen()
    {
        Toast.makeText(this, "Compose New Tweet", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(SearchActivity.this, FilterSearchActivity.class);
        startActivityForResult(i, 200);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_settings:
                showSettingsScreen();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        gFilterImageSize = data.getStringExtra("imageSize");
        gFilterColorFilter = data.getStringExtra("colorFilter");
        gFilterImageType = data.getStringExtra("imageType");
        gFilterSite = data.getStringExtra("siteFilter");
    }

    public void getImage(int page, String q)
    {
        int size = 8;
        String query = q+getSearchQueryURL()+"&start=" + page;
        //int page = 1;

        String searchUrl = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q="+query;
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
    public String getSearchQueryURL() {
        String query = "&imgsz=" + gFilterImageSize +
                "&imgcolor=" + gFilterColorFilter +
                "&imgtype=" + gFilterImageType +
                "&as_sitesearch=" + gFilterSite;
        return query;
    }

    public void onImageSearch(View view) {
        String query = etQueary.getText().toString();
        Toast.makeText(this, "Finding images for " + query, Toast.LENGTH_SHORT).show();
        aImageResult.clear();
        getImage(1, query);
    }
}
