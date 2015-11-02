package com.example.admin.ctu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

public class Home extends ListActivity {
    private Button Search_Button;
    private TextView source, dest;
    private SharedPreferences sp;
    private ArrayList _searchResult = new ArrayList();
    Intent myintent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);

        source = (TextView) findViewById(R.id.source);
        dest = (TextView) findViewById(R.id.destination);
        Search_Button= (Button) findViewById(R.id.search);

        if(!isNetworkAvailable())
        {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Internet is not working, not able to check for updates");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int value = sp.getInt("Version", 1);
        if(value == 1) {
            {
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("Version", 1);
                editor.commit();
            }
        }
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("http://nodejs-wirelessnetwork.rhcloud.com/isChanged");
        HttpResponse httpResponse;
        try {
            httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            String responseString = EntityUtils.toString(httpEntity, "UTF-8");
            updateDatabase(responseString);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }



        database d = new database(getBaseContext());
        d.open();

        _searchResult = d.searchAllRoute();

        setListAdapter(new MyAdapter(getBaseContext(), android.R.layout.simple_list_item_1, R.id.textView2, _searchResult));
        Home.this.registerForContextMenu(getListView());

        source.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // Click code
                String de = dest.getText().toString();
                myintent = new Intent(Home.this, SearchRoute.class);
                myintent.putExtra("search", "source");
                myintent.putExtra("valDest", de);
                startActivityForResult(myintent, 0);
            }
        });

        dest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                String so = source.getText().toString();
                myintent = new Intent(Home.this, SearchRoute.class);
                myintent.putExtra("search", "dest");
                myintent.putExtra("valSource", so);
                startActivityForResult(myintent, 1);
            }
        });

        Search_Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // Click code
                database d = new database(getBaseContext());
                d.open();

                _searchResult = d.searchRoute(source.getText().toString(), dest.getText().toString());

                setListAdapter(new MyAdapter(getBaseContext(), android.R.layout.simple_list_item_1, R.id.textView2, _searchResult));
                Home.this.registerForContextMenu(getListView());

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

        @Override
        protected void onListItemClick(ListView l, View v, int position, long id) {

                   String item = ((TextView) v).getText().toString();
            Intent intent = new Intent(getBaseContext(), routeOnMap.class);
            intent.putExtra("route", (item.split("\n")[0]).substring(10));
            startActivity(intent);
        }





    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private class updateDatabase extends AsyncTask<Integer, Void, Integer> {
        ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(Home.this);
            dialog.setTitle("Updating...");
            dialog.setMessage("Please wait...");
            dialog.setIndeterminate(true);
            dialog.show();
        }
        @Override
        protected Integer doInBackground(Integer... params) {
            database d = new database(getBaseContext());
            d.open();
            d.updateDb();
            int ver = params[0];
            extDatabase ed = new extDatabase(d);
            try {
                ed.updateDatabase();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("Version", ver);
            editor.commit();
            return null;
        }
        protected void onPostExecute(Integer result) {
            dialog.dismiss();
        }
    }
    private void updateDatabase(String serverVer) throws JSONException {
        database d = new database(getBaseContext());
        d.open();
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int v = sp.getInt("Version", 0);
        int ver = Integer.parseInt(String.valueOf(serverVer.charAt(0)));
        if(ver != v) {
            new updateDatabase().execute(ver);
        }
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

}
