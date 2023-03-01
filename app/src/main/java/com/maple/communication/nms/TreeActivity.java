package com.maple.communication.nms;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import com.maple.communication.nms.Adapter.ExAdapter;


public class TreeActivity extends AppCompatActivity {
    ExAdapter adapter;
    ExpandableListView exList;
    TreeActivity treeActi =this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.tree_fragment);
        adapter=new ExAdapter(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

      ExpandableListView exList = (ExpandableListView) findViewById(R.id.list);
        exList.setAdapter(adapter);
        exList.setGroupIndicator(null);
        exList.setDivider(null);
        exList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,int groupPosition, int childPosition, long id) {

      /* You must make use of the View v, find the view by id and extract the text as below*/
                Intent intent = new Intent(TreeActivity.this, PReading.class);
                intent.putExtra("siteno",3);
                startActivity(intent);


                return true;  // i missed this
            }
        });

        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TreeActivity.this,SiteActivity.class);
                startActivity(intent);
            }
        });


        }
        //Intent intnt = getIntent().getExtras().getString("arg");();


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
}
