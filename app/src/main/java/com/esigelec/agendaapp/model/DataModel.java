package com.esigelec.agendaapp.model;


import java.util.ArrayList;

public class DataModel {
    private static DataModel instance = new DataModel();
    private DataModel(){
        contacts = new ArrayList<>();
        contacts.add(new ContactDetail("Mark",
                "Curitiba, Brazi","223344"));
        contacts.add(new ContactDetail("Neymar",
                "Paris, France","10101010"
                ));
        contacts.add(new ContactDetail("Messi",
                "Paris, France",
                "3131313131"));
        contacts.add(new ContactDetail("C. Ronaldo",
                "Manchester, UK","07070707"));
        contacts.add(new ContactDetail("Viny Jr.",
                "Madri, Spain","80808080"));
    }
    public static DataModel getInstance(){
        return instance;
    }
    public ArrayList<ContactDetail> contacts;

    public ArrayList<String> getStringContacts(){
        ArrayList<String>stringContacts = new ArrayList<>();
        for (ContactDetail c:contacts) {
            stringContacts.add(c.getName());
        }
        return stringContacts;
    }
}
