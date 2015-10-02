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
    /*ArrayList searchRoute(String source, String dest) throws JSONException {
        JSONParser jParser = new JSONParser();
        JSONObject object = null;
        String route;
        ArrayList _searchRoute = new ArrayList();
        Object json = jParser.makeHttpRequest("http://nodejs-wirelessnetwork.rhcloud.com", "GET", null);
        JSONArray array = (JSONArray) json;
        for (int i = 0; i < array.length(); i++) {
            object = array.getJSONObject(i);
            route = object.getString("route");
            if (route.contains(source) && route.contains(dest)) {
                _searchRoute.add("Route No :" + object.getString("route_no") +
                        "\nTimings : " + object.getString("timings") +
                        "\nFrequency : " + object.getString("frequency"));
            }
        }
        return _searchRoute;
    }

    ArrayList searchSource() throws JSONException {
        JSONParser jParser = new JSONParser();
        JSONObject object = null;
        String route;
        ArrayList _searchSource = new ArrayList();
        Object json = jParser.makeHttpRequest("http://nodejs-wirelessnetwork.rhcloud.com", "GET", null);
        JSONArray array = (JSONArray) json;
        int count = 0;
        for (int i = 0; i < array.length(); i++) {
            object = array.getJSONObject(i);
            route = object.getString("route");
            List<String> list = Arrays.asList(route.split(","));
            while (count < list.size()) {
                if(!(_searchSource.contains(list.get(count))))
                {
                    _searchSource.add(list.get(count));
                }
                count++;
            }
            count = 0;
        }
        return _searchSource;
    }

    ArrayList getCoordinates(String route_no) throws JSONException {

        JSONParser jParser = new JSONParser();
        JSONObject object = null;
        String routeNo, coordinates = "", route;
        List<String> list = null;
        List<String> routeList = null;
        ArrayList _searchResult = new ArrayList();
        Object json = jParser.makeHttpRequest("http://nodejs-wirelessnetwork.rhcloud.com", "GET", null);
        JSONArray array = (JSONArray) json;
        int count = 0, count1 = 0;
        for(int i = 0; i< array.length(); i++)
        {
            object = array.getJSONObject(i);
            routeNo = object.getString("route_no");
            if(route_no.equals(routeNo))
            {
                coordinates = object.getString("coordinates");
                route = object.getString("route");
                list = Arrays.asList(coordinates.split(","));
                routeList = Arrays.asList(route.split(","));
                break;
            }
        }
        while (count < list.size()) {
            _searchResult.add(list.get(count));
            _searchResult.add(list.get(count+1));
            _searchResult.add(routeList.get(count1));
            count1++;
            count = count + 2;
        }
        return _searchResult;

    }*/
}
