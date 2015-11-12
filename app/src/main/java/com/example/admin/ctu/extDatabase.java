package com.example.admin.ctu;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public class extDatabase {

    database dB;
    public extDatabase(database d)
    {
        dB = d;
    }
    void updateDatabase() throws JSONException {
        JSONParser jParser = new JSONParser();
        JSONObject object = null;
        Object json = jParser.makeHttpRequest("http://nodejs-wirelessnetwork.rhcloud.com", "GET", null);
        JSONArray array = (JSONArray) json;
        for(int i = 0; i < array.length(); i++)
        {
             object = array.getJSONObject(i);
             dB.insert(object, dB);
        }
    }
}
