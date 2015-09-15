package com.example.admin.ctu;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Home extends Activity{
    TextView source, dest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        getActionBar().setIcon(
                new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.action_home, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        mTitleTextView.setText("CTU");
        mTitleTextView.setTextColor(Color.WHITE);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1753a4")));
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setDisplayShowHomeEnabled(true);
        source = (TextView) findViewById(R.id.source);
        dest = (TextView) findViewById(R.id.dest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }
    public void accident(View v)
    {
        Intent intent = new Intent(getBaseContext(), emergency.class);
        startActivity(intent);
    }
    public void showMap(View view) {
        Intent intent = new Intent(getBaseContext(), map.class);
        startActivity(intent);
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
    public void search(View view)
    {
        String s = source.getText().toString();
        String d = dest.getText().toString();
        if(!(s.isEmpty()) && !(d.isEmpty()))
        {
            Intent intent = new Intent(getBaseContext(), Details.class);
            intent.putExtra("search", "route");
            intent.putExtra("valSource", s);
            intent.putExtra("valDest", d);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(getBaseContext(), "Mention both source and destination first",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void dest(View view)
    {
        String so = source.getText().toString();
        Intent intent = new Intent(getBaseContext(), Details.class);
        intent.putExtra("search", "dest");
        intent.putExtra("valSource", so);
        startActivityForResult(intent, 1);
    }

    public void source(View view) {
        String de = dest.getText().toString();
        Intent intent = new Intent(getBaseContext(), Details.class);
        intent.putExtra("search", "source");
        intent.putExtra("valDest", de);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 0 : {
                if (resultCode == Activity.RESULT_OK) {
                    source.setText(data.getStringExtra("result"));
                }
                break;
            }
            case 1: {
                 if (resultCode == Activity.RESULT_OK) {
                     dest.setText(data.getStringExtra("result"));
                 }
            }
        }
    }

    public void swap(View view)
    {
        String temp = source.getText().toString();
        source.setText(dest.getText().toString());
        dest.setText(temp);
    }
}
