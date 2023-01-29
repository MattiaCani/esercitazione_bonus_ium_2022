package com.unica.esbonusium2022mattiacani;

import java.util.ArrayList;
import java.util.HashMap;

public class ListaUtenti {
    //-------------- attributi --------------
    //lista di utenti dove contenere il database
    private static final Utente administrator = new Utente("admin", "admin");
    public static ArrayList<Utente> dbUtenti = new ArrayList<Utente>();
    public static ArrayList<Utente> arrayResult = new ArrayList<Utente>();
    public static boolean initialized = false;

    public static ArrayList<String> getUsernameArrayList(){
        ArrayList<String> usernameArrayList = new ArrayList<>();

        for(int i=0; i< dbUtenti.size(); i++){
            usernameArrayList.add(dbUtenti.get(i).getUsername());
        }

        return usernameArrayList;
    }

    public static void initializeDB(){
        administrator.setAdmin(true);
        administrator.setRootFlag(true);
        administrator.setUserId(ListaUtenti.dbUtenti.size());
        administrator.setPicId(1);
        dbUtenti.add(administrator);
        arrayResult.add(administrator);
        initialized = true;
    }
}
