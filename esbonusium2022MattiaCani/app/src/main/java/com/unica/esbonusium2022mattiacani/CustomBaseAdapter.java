package com.unica.esbonusium2022mattiacani;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomBaseAdapter extends BaseAdapter {
    Context context;
    //String[] usernameList;
    ArrayList<Utente> oggettiLista;
    //int[] profilePictures;
    LayoutInflater inflater;


    public CustomBaseAdapter(Context ctx, ArrayList<Utente> oggettiLista){
        this.context = ctx;
        this.oggettiLista = oggettiLista;
        inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return oggettiLista.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.activity_list_view, null);
        ImageView imgIcon = convertView.findViewById(R.id.imageIcon);
        TextView txtView = convertView.findViewById(R.id.textView);
        Switch switchButton = convertView.findViewById(R.id.switchButton);

        //aggiorno la lista dei riferimenti al DB
        HashMap<String, Integer> references = new HashMap<>();
        for(Utente u : ListaUtenti.dbUtenti){
            references.put(u.getUsername(), u.getUserId());
        }

        Log.d("TEXTVIEW TEXT", "testo letto dalla textview numero " + position + ": " + txtView.getText().toString());

        //leggo i permessi e setto gli switch di conseguenza
        if(oggettiLista.get(position).getAdmin()){
            switchButton.setChecked(true);
        }

        if(oggettiLista.get(position).getPicId() == 1){
            imgIcon.setImageResource(R.drawable.avatar1);
        } else if(oggettiLista.get(position).getPicId() == 2){
            imgIcon.setImageResource(R.drawable.avatar2);
        } else if(oggettiLista.get(position).getPicId() == 3){
            imgIcon.setImageResource(R.drawable.avatar3);
        }

        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d("", "Hai cliccato lo switch di " + ListaUtenti.dbUtenti.get(position+1).getUsername());
                //creo un'hashmap che contiene i riferimenti degli indici username/userId del DButenti


                Log.d("HASHMAP", "hai cliccato su " + txtView.getText().toString() + " e il suo indice nel DB è: " + references.get(txtView.getText().toString()));
                Log.d("TEXTVIEW in button", "testo letto dalla textview numero " + position + ": " + txtView.getText().toString());
                //controllo se sto cliccando l'admin, altrimenti è un utente registrato
                if(txtView.getText().toString().equals("admin")){
                    switchButton.setEnabled(true);
                    switchButton.setClickable(false);
                    Toast.makeText(context.getApplicationContext(), "Azione non permessa!", Toast.LENGTH_SHORT).show();
                } else if(!(ListaUtenti.dbUtenti.get(references.get(txtView.getText().toString())-1).getAdmin())){
                    ListaUtenti.dbUtenti.get(references.get(txtView.getText().toString())-1).setAdmin(true);
                    //Log.d("IS_ADMIN", ListaUtenti.dbUtenti.get(position+1).getUsername() + " ora è admin. Variabile: " + ListaUtenti.dbUtenti.get(position+1).getAdmin());
                    Toast.makeText(context.getApplicationContext(), ListaUtenti.dbUtenti.get(references.get(txtView.getText().toString())-1).getUsername() + " È ORA ADMIN", Toast.LENGTH_SHORT).show();
                } else {
                    ListaUtenti.dbUtenti.get(references.get(txtView.getText().toString())-1).setAdmin(false);
                    //Log.d("IS_ADMIN", ListaUtenti.dbUtenti.get(position+1).getUsername() + " non è più admin. Variabile: " + ListaUtenti.dbUtenti.get(position+1).getAdmin());
                    Toast.makeText(context.getApplicationContext(), ListaUtenti.dbUtenti.get(references.get(txtView.getText().toString())-1).getUsername() + " NON È PIÙ ADMIN", Toast.LENGTH_SHORT).show();
                }
            }
        });

        txtView.setText(oggettiLista.get(position).getUsername());

        return convertView;
    }
}
