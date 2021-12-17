package com.esigelec.agendaapp.model;


import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class DataModel {
    private static DataModel instance = new DataModel();
    private DataModel(){
        contacts = new ArrayList<>();
    }
    public static DataModel getInstance(){
        return instance;
    }
    private ArrayList<ContactDetail> contacts;
    private ContactDatabase database;

    public void setContext(Context context){
        database = new ContactDatabase(context);
        contacts = database.retrieveContactsFromDB();
    }
    public ArrayList<ContactDetail> getContacts(){
        return contacts;
    }
    public ContactDetail getContact(int position){
        return contacts.get(position);
    }
    public boolean insertContact(ContactDetail c,int position){
        long id = database.insertContactInDB(c);
        if(id > 0){
            contacts.add(position,c);
            return true;
        }else{
            Log.wtf("Agenda","error saving contact in DB");
            return false;
        }
    }
    public boolean addContact(ContactDetail c){
        long id = database.createContactInDB(c);
        if(id > 0){
            c.setId(id);
            contacts.add(c);
            return true;
        }else{
            Log.wtf("Agenda","error saving contact in DB");
            return false;
        }
    }
    public boolean updateContact(ContactDetail c,int position){
        int count = database.updateContactInDB(c);
        if(count == 1){
            contacts.set(position,c);
            return true;
        }
        Log.wtf("Agenda","error updating contact in DB");

        return false;
    }
    public boolean removeContact(int position){
        int count  = database.removeContactFromDB(
                contacts.get(position)
        );
        if(count == 1){
            contacts.remove(position);
            return true;
        }
        Log.wtf("Agenda","error removing contact in DB");
        return false;
    }
}
