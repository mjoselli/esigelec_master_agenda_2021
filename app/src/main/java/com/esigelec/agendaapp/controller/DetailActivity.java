package com.esigelec.agendaapp.controller;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;

import com.esigelec.agendaapp.R;
import com.esigelec.agendaapp.model.ContactDetail;
import com.esigelec.agendaapp.model.DataModel;


public class DetailActivity extends AppCompatActivity {

    EditText nameEditText;
    EditText addressEditText;
    EditText phoneEditText;
    int index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        nameEditText = findViewById(R.id.nameEditText);
        addressEditText = findViewById(R.id.addressEditText);
        phoneEditText = findViewById(R.id.phoneEditText);

        Bundle extras = getIntent().getExtras();
        index = extras.getInt("index");
        if(index != -1){
            ContactDetail details = DataModel.getInstance().getContact(index);
            nameEditText.setText(details.getName());
            addressEditText.setText(details.getAddress());
            phoneEditText.setText(details.getPhone());
        }
    }

    @Override
    public void onBackPressed() {
        String name = nameEditText.getText().toString();
        String address = addressEditText.getText().toString();
        String phone = phoneEditText.getText().toString();

        if(name.length() > 1 && (address.length() > 1 || phone.length() > 1)){
            if(index == -1) {
                DataModel.getInstance().addContact(
                        new ContactDetail(name, address, phone)
                );
            }else{
                ContactDetail c = DataModel.getInstance().getContact(index);
                c.setName(name);
                c.setAddress(address);
                c.setPhone(phone);
                DataModel.getInstance().updateContact(c,index);
            }

            finish();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    DetailActivity.this
            );
            builder.setTitle(getString(R.string.alert_edit_title));
            builder.setMessage(getString(R.string.alert_edit_msg));
            builder.setPositiveButton(android.R.string.yes,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
            builder.setNegativeButton(android.R.string.no,null);
            builder.create().show();
        }


    }
}