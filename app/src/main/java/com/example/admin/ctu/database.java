package com.example.admin.ctu;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class database{
    public static final String DB_PATH = "/data/data/com.example.admin.ctu/databases/";
    public static final String DB_NAME = "ctu_new";
    public static final String TABLE_NAME = "bus_route";
    public static int VERSION = 1;
    public static DbHelper ourHelper;
    private Context context;
    public SQLiteDatabase ctuNew;
    public ArrayList _searchResult;

    public static class DbHelper extends SQLiteOpenHelper {


        public DbHelper(Context context) {
            super(context, DB_NAME, null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS `bus_route` (" +
                    "`id` integer PRIMARY KEY AUTOINCREMENT," +
                    "`route_no` text NOT NULL," +
                    "`route` text NOT NULL," +
                    "`timings` text NOT NULL," +
                    "`frequency` text NOT NULL," +
                    "`total_distance` text NOT NULL," +
                    "`total_buses` int(2) NOT NULL," +
                    "`bus_type` text NOT NULL," +
                    "`coordinates` text NOT NULL" +
                    ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

    public database(Context context) {
        this.context = context;
    }
    public database open() {
        ourHelper = new DbHelper(context);
        ctuNew = ourHelper.getWritableDatabase();
        return this;
    }

    public ArrayList searchRoute(String s, String d) {
        _searchResult = new ArrayList();

        try {
            //open database to query
            open();
            Cursor cursor = ctuNew.query("bus_route",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null);
            if (cursor.moveToFirst()) {
                do {
                    String route = String.valueOf(cursor.getString(2));
                    //List<String> list = Arrays.asList(route.split(","));
                    if (route.contains(s) && route.contains(d)) {
                        _searchResult.add("Route No :" + String.valueOf(cursor.getString(1)) +
                                "\nTimings : " + String.valueOf(cursor.getString(3)) +
                                "\nFrequency : " + String.valueOf(cursor.getString(4)));
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception ex) {
            System.out.println("DatabaseHelper.search()- : ex " + ex.getClass() + ", " + ex.getMessage());
        }
        return _searchResult;
    }

    public ArrayList searchSource() {
        _searchResult = new ArrayList();
        int count = 0;

        try {
            open();
            Cursor cursor = ctuNew.query("bus_route",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null);
            if (cursor.moveToFirst()) {
                do {
                    String s = String.valueOf(cursor.getString(2));
                    List<String> list = Arrays.asList(s.split(","));
                    while (count < list.size()) {
                        if (!(_searchResult.contains(list.get(count)))) {
                            _searchResult.add(list.get(count));
                        }
                        count++;
                    }
                    count = 0;
                } while (cursor.moveToNext());
            }
            //close cursor
            cursor.close();
        } catch (Exception ex) {
            System.out.println("DatabaseHelper.search()- : ex " + ex.getClass() + ", " + ex.getMessage());
        }
        return _searchResult;
    }

    public ArrayList getCoordinates(String route_no) {
        _searchResult = new ArrayList();
        int count = 0, count1 = 0;

        try {
            open();
            String[] columns = {"route_no", "route", "coordinates"};
            String whereClause = "route_no" + "= '" +  route_no + "'";
            Cursor cursor = ctuNew.query("bus_route",
                    columns,
                    whereClause,
                    null,
                    null,
                    null,
                    null);
            if (cursor.moveToFirst()) {
                        List<String> coordinates = Arrays.asList(cursor.getString(2).split(","));
                        List<String> route = Arrays.asList(cursor.getString(1).split(","));
                        while (count < coordinates.size()) {
                            _searchResult.add(coordinates.get(count));
                            _searchResult.add(coordinates.get(count+1));
                            _searchResult.add(route.get(count1));
                            count1++;
                            count = count + 2;
                        }
            }
            cursor.close();
        } catch (Exception ex) {
            System.out.println("DatabaseHelper.search()- : ex " + ex.getClass() + ", " + ex.getMessage());
        }
        return _searchResult;
    }

    public void insert(JSONObject object, database d) throws JSONException {
        ctuNew.execSQL("INSERT INTO 'bus_route' ('route_no', 'route', 'coordinates', 'timings', 'frequency', 'total_distance', 'total_buses', 'bus_type') VALUES" +
                        "('"+object.getString("route_no")+"','" +
                object.getString("route") + "','" +
                object.getString("coordinates")+ "','" +
                object.getString("timings")+ "','" +
                object.getString("frequency")+ "','" +
                object.getString("total_distance")+ "'," +
                object.getInt("total_buses")+ ",'" +
                object.getString("bus_type") + "')");
    }

    public void updateDb() {
        database.DbHelper dh = new database.DbHelper(context);
        open();
        ctuNew.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        dh.onCreate(ctuNew);
    }
}
