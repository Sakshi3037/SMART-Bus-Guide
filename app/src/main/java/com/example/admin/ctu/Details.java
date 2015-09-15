package com.example.admin.ctu;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Vector;

public class Details extends Activity{
    android.widget.ListView listView;
    String item, source, dest;
    private ArrayList _searchResult = new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        database d = new database(getBaseContext());
        d.open();
        Intent i = getIntent();
        final String data_type = i.getStringExtra("search");
        if(data_type.equals("route"))
        {
            source = i.getStringExtra("valSource");
            dest = i.getStringExtra("valDest");
            _searchResult = d.searchRoute(source, dest);
        }
        else if(data_type.equals("source"))
        {
            _searchResult = d.searchSource();
            dest = i.getStringExtra("valDest");
            if(!(dest.isEmpty())) {
                _searchResult.remove(dest);
            }
        }
        else if(data_type.equals("dest"))
        {
            _searchResult = d.searchSource();
            source = i.getStringExtra("valSource");
            if(!(source.isEmpty())) {
                _searchResult.remove(source);
            }
        }
        listView = (android.widget.ListView) findViewById(R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, _searchResult);
        listView.setAdapter(adapter);
        ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                item = ((TextView) view).getText().toString();
                if(data_type.equals("route")){
                    Intent intent = new Intent(getBaseContext(), routeOnMap.class);
                    intent.putExtra("route", (item.split("\n")[0]).substring(10));
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent();
                    intent.putExtra("result", item);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id == android.R.id.home)
        {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
