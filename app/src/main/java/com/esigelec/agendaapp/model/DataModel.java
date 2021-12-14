package com.esigelec.agendaapp.model;


import android.content.Context;

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
    public ArrayList<ContactDetail> contacts;

    public void loadFromFile(Context context){
        try{
            InputStream stream = context.openFileInput("contacts.txt");
            InputStreamReader streamReader = new InputStreamReader(stream);
            BufferedReader reader = new BufferedReader(streamReader);
            contacts.clear();
            String line;
            while ((line = reader.readLine())!=null){
                String []aux = line.split(";");
                contacts.add(
                        new ContactDetail(aux[0],aux[1],aux[2])
                );
            }
            reader.close();
            streamReader.close();
            stream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void saveToFile(Context context){
        try{
            OutputStream stream = context.openFileOutput(
                    "contacts.txt",
                    Context.MODE_PRIVATE
            );
            OutputStreamWriter writer = new OutputStreamWriter(stream);
            for (ContactDetail c:contacts) {
                writer.write(c.getName()+ ";"+c.getAddress()+";"+c.getPhone()+"\n");
            }
            writer.flush();
            writer.close();
            stream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<String> getStringContacts(){
        ArrayList<String>stringContacts = new ArrayList<>();
        for (ContactDetail c:contacts) {
            stringContacts.add(c.getName());
        }
        return stringContacts;
    }
}
