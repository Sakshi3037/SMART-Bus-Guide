package com.example.admin.ctu;

/**
 * Created by pawan on 10/26/2015.
 */

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class SearchRoute extends Activity implements OnClickListener,
        OnEditorActionListener, OnItemClickListener {
    ListView mListView;
    SearchAdapter mAdapter;
    Button btnSearch, btnLeft;
    EditText mtxt;
    String item, source, dest;
    private ArrayList<String> mAllData = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mListView = (ListView) findViewById(R.id.mListView);
        mAdapter = new SearchAdapter(this);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnLeft = (Button) findViewById(R.id.btnLeft);
        mtxt = (EditText) findViewById(R.id.edSearch);



        database d = new database(getBaseContext());
        d.open();
        Intent i = getIntent();
        final String data_type = i.getStringExtra("search");

        mAllData = d.searchSource();
            if(data_type.equals("source"))
            {
                dest = i.getStringExtra("valDest");
                if(!(dest.isEmpty())) {
                    mAllData.remove(dest);
                }
            }
            else
            {
                source = i.getStringExtra("valSource");
                if(!(source.isEmpty())) {
                    mAllData.remove(source);
                }
            }




        mtxt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (0 != mtxt.getText().length()) {
                    String spnId = mtxt.getText().toString();
                    setSearchResult(spnId);
                } else {
                    setData();
                }
            }
        });
        btnLeft.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        setData();
    }


    String[] str = { "Hit me Hard", "GIJ, Rise Of Cobra", "Troy",
            "A walk To remember", "DDLJ", "Tom Peter Nmae", "David Miller",
            "Kings Eleven Punjab", "Kolkata Knight Rider", "Rest of Piece" };

    public void setData() {

        mAdapter = new SearchAdapter(this);
        for (String temp : mAllData){
            mAdapter.addItem(temp);

        }
        mListView.setOnItemClickListener(this);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSearch:
                mtxt.setText("");
                setData();
                break;
            case R.id.btnLeft:

                break;
        }
    }

    public void setSearchResult(String str) {
        mAdapter = new SearchAdapter(this);
        for (String temp : mAllData) {
            if (temp.toLowerCase().contains(str.toLowerCase())) {
                mAdapter.addItem(temp);
            }
        }
        mListView.setAdapter(mAdapter);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return false;
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                            long arg3) {
        String str = mAdapter.getItem(position);

        Intent intent = new Intent();
        intent.putExtra("result", str);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}