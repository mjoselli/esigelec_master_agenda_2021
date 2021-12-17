package com.esigelec.agendaapp.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Canvas;
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

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends AppCompatActivity {


    RecyclerView mainRecyclerView;
    ContactAdapter adapter = new ContactAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainRecyclerView = findViewById(R.id.mainRecyclerView);

        DataModel.getInstance().setContext(getApplicationContext());

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

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(
                        ItemTouchHelper.UP|ItemTouchHelper.DOWN,
                        ItemTouchHelper.LEFT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        int fromPos = viewHolder.getAdapterPosition();
                        int toPos = target.getAdapterPosition();

                        ContactDetail fromContact = DataModel.getInstance().getContact(fromPos);
                        ContactDetail toContact = DataModel.getInstance().getContact(toPos);

                        long id = fromContact.getId();
                        fromContact.setId(toContact.getId());
                        toContact.setId(id);

                        DataModel.getInstance().updateContact(fromContact,toPos);
                        DataModel.getInstance().updateContact(toContact,fromPos);

                        adapter.notifyItemMoved(fromPos,toPos);

                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getAdapterPosition();
                        DataModel.getInstance().removeContact(position);
                        adapter.notifyItemRemoved(position);
                    }

                    @Override
                    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                        new RecyclerViewSwipeDecorator.Builder(
                                c,recyclerView,viewHolder,dX,dY,actionState,isCurrentlyActive
                        ).addBackgroundColor(
                                ContextCompat.getColor(MainActivity.this,android.R.color.holo_red_light)
                        ).addActionIcon(android.R.drawable.ic_menu_delete)
                                .create()
                                .decorate();

                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                    }
                }
        );
        itemTouchHelper.attachToRecyclerView(mainRecyclerView);

        /*adapter.setOnItemLongClickListener(new ContactAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                ContactDetail c = DataModel.getInstance().getContact(position);
                DataModel.getInstance().removeContact(position);
                adapter.notifyItemRemoved(position);
                View contextView = findViewById(android.R.id.content);
                Snackbar.make(contextView,"Contact Removed",Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.undo_msg), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DataModel.getInstance().insertContact(c,position);
                                adapter.notifyItemInserted(position);
                            }
                        })
                        .show();
            }
        });*/



        //TODO implement click listeners

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