package com.esigelec.agendaapp.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.esigelec.agendaapp.R;
import com.esigelec.agendaapp.model.ContactDetail;
import com.esigelec.agendaapp.model.DataModel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    int createCalledXTimes = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int i, long l) {
                goToDetailActivity(i);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                DataModel.getInstance().contacts.remove(i);
                updateListView();
                if(i > 1){
                    listView.requestFocusFromTouch();
                    listView.setSelection(i - 1);
                }
                return true;
            }
        });
        loadCounterFromFile();
        createCalledXTimes++;
        Log.d("COUNTER","---"+createCalledXTimes);
        saveCounterToFile();
    }

    void loadCounterFromFile(){
        try{
            InputStream stream = MainActivity.this.
                    openFileInput("counter.txt");
            InputStreamReader reader = new InputStreamReader(stream);
            createCalledXTimes = reader.read();
            reader.close();
            stream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    void saveCounterToFile(){
        try {
            OutputStream stream = MainActivity.this.openFileOutput(
                    "counter.txt",
                    MODE_PRIVATE
            );
            OutputStreamWriter writer = new OutputStreamWriter(stream);
            writer.write(createCalledXTimes);
            writer.flush();
            writer.close();
            stream.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        updateListView();
    }

    protected void updateListView(){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                MainActivity.this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                DataModel.getInstance().getStringContacts()
        );
        listView.setAdapter(adapter);
    }

    public void addNewContactOnClick(View v){
        goToDetailActivity(-1);
    }
    protected void goToDetailActivity(int index){
        Intent intent = new Intent(MainActivity.this,
                DetailActivity.class);
        intent.putExtra("index",index);
        startActivity(intent);
    }
}