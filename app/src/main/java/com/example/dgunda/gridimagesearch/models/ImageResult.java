package com.example.dgunda.gridimagesearch.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by dgunda on 10/18/15.
 */

public class ImageResult implements Serializable{
    private static final long serialVersionUID = 4125965356358329466L;
    public String fullUrl;
    public String thumbUrl;
    public String title;

    public ImageResult(JSONObject json)
    {

        try{

            this.fullUrl = json.getString("url");
            this.thumbUrl = json.getString("tbUrl");
            this.title = json.getString("title");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
    public static ArrayList<ImageResult> fromJsonArray(JSONArray array)
    {
        ArrayList<ImageResult> results = new ArrayList<ImageResult>();

        for(int i=0;i<array.length();i++)
        {
            try{

                results.add(new ImageResult(array.getJSONObject(i)));
            }catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        return results;
    }
}
