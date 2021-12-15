package com.esigelec.agendaapp.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.esigelec.agendaapp.R;
import com.esigelec.agendaapp.model.ContactDetail;
import com.esigelec.agendaapp.model.DataModel;
import com.google.android.material.snackbar.Snackbar;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {


    RecyclerView mainRecyclerView;
    ContactAdapter adapter = new ContactAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainRecyclerView = findViewById(R.id.mainRecyclerView);
        mainRecyclerView.setAdapter(adapter);
        mainRecyclerView.setLayoutManager(
                new LinearLayoutManager(MainActivity.this)
        );
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(
                MainActivity.this,DividerItemDecoration.VERTICAL
        );
        mainRecyclerView.addItemDecoration(itemDecoration);

        adapter.setOnItemClickListener(new ContactAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                goToDetailActivity(position);
            }
        });
        adapter.setOnItemLongClickListener(new ContactAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                ContactDetail c = DataModel.getInstance().contacts.remove(position);
                DataModel.getInstance().saveToFile(MainActivity.this);
                adapter.notifyItemRemoved(position);
                View contextView = findViewById(android.R.id.content);
                Snackbar.make(contextView,"Contact Removed",Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.undo_msg), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DataModel.getInstance().contacts.add(position,c);
                                DataModel.getInstance().saveToFile(MainActivity.this);
                                adapter.notifyItemInserted(position);
                            }
                        })
                        .show();
            }
        });



        //TODO implement click listeners

        DataModel.getInstance().loadFromFile(MainActivity.this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_add_item){
            goToDetailActivity(-1);
        }
        return super.onOptionsItemSelected(item);
    }

    protected void goToDetailActivity(int index){
        Intent intent = new Intent(MainActivity.this,
                DetailActivity.class);
        intent.putExtra("index",index);
        startActivity(intent);
    }
}