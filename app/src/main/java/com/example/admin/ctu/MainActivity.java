package com.example.admin.ctu;

import android.app.Activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends ListActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;
    private Button Search_Button;
    private FrameLayout sourceL, destL;
    private TextView source, sourceR, dest, destR;
    private ArrayList _searchResult = new ArrayList();
    Intent myintent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        source = (TextView) findViewById(R.id.sourceLeft);
        dest = (TextView) findViewById(R.id.destLeft);
        sourceR = (TextView) findViewById(R.id.sourceRight);
        destR = (TextView) findViewById(R.id.destRight);
        sourceL = (FrameLayout) findViewById(R.id.sourceLayout);
        destL = (FrameLayout) findViewById(R.id.destinationLayout);
        Search_Button= (Button) findViewById(R.id.search);
        database d = new database(getBaseContext());
        d.open();
        _searchResult = d.searchAllRoute();
        setListAdapter(new MyAdapter(getBaseContext(), android.R.layout.simple_list_item_1, R.id.textView2, _searchResult));
        MainActivity.this.registerForContextMenu(getListView());
        sourceL.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                String de = dest.getText().toString();
                myintent = new Intent(MainActivity.this, SearchRoute.class);
                myintent.putExtra("search", "source");
                myintent.putExtra("valDest", de);
                startActivityForResult(myintent, 0);
            }
        });
        destL.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                String so = source.getText().toString();
                myintent = new Intent(MainActivity.this, SearchRoute.class);
                myintent.putExtra("search", "dest");
                myintent.putExtra("valSource", so);
                startActivityForResult(myintent, 1);
            }
        });
        Search_Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                String s = source.getText().toString();
                String de = dest.getText().toString();
                if(s.isEmpty() || de.isEmpty())
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("Mention both source and destination");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                else
                {
                    database d = new database(getBaseContext());
                    d.open();
                    _searchResult = d.searchRoute(source.getText().toString(), dest.getText().toString());

                    setListAdapter(new MyAdapter(getBaseContext(), android.R.layout.simple_list_item_1, R.id.textView2, _searchResult));
                    MainActivity.this.registerForContextMenu(getListView());
                }
            }
        });
    }
    class MyAdapter extends ArrayAdapter<String> {

        public MyAdapter(Context context, int resource, int textview1, ArrayList<String> names) {
            super(context, resource, textview1, names);
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater myinflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = myinflater.inflate(R.layout.listview, parent, false);
            TextView img1 = (TextView) row.findViewById(R.id.textView);
            TextView t1 = (TextView) row.findViewById(R.id.textView2);
            t1.setText(_searchResult.get(position).toString());
            String route_no = _searchResult.get(position).toString().split("\n")[0].split(":")[1];
            img1.setText(route_no);
            return row;
        }
    }
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String item = ((TextView)v.findViewById(R.id.textView)).getText().toString();
        Intent intent = new Intent(getBaseContext(), routeOnMap.class);
        intent.putExtra("route", item);
        startActivity(intent);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 0 : {
                if (resultCode == Activity.RESULT_OK) {
                    source.setText(data.getStringExtra("result"));
                    sourceR.setHint("Source");
                }
                break;
            }
            case 1: {
                if (resultCode == Activity.RESULT_OK) {
                    dest.setText(data.getStringExtra("result"));
                    destR.setHint("Destination");
                }
            }
        }
    }
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        switch(position)
        {
            case 1:break;
            case 2: startActivity(new Intent(this, map.class));
                break;
            case 3: startActivity(new Intent(this, emergency.class));
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }
}
