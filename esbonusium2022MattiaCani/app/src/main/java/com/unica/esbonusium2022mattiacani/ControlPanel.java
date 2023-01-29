package com.unica.esbonusium2022mattiacani;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ControlPanel extends AppCompatActivity {
    Utente utente;
    ListView listView;
    Button backHome, searchButton, resetSearchButton;
    ImageView emptyResultImage;
    TextView emptyResultMessage;
    EditText keywordSearch;

    String[] resultsToShow;
    //HashMap<String, Integer> resultToDBMapper = new HashMap<>();

    boolean resetDefaultListView = true;

    public static final String USER_EXTRA = "com.unica.esbonusium2022mattiacani.Utente";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_panel);

        getSupportActionBar().hide();

        backHome = findViewById(R.id.backHome);
        keywordSearch = findViewById(R.id.keywordSearch);
        searchButton = findViewById(R.id.searchButton);
        resetSearchButton = findViewById(R.id.resetSearchButton);
        emptyResultImage = findViewById(R.id.emptyResultImage);
        emptyResultMessage = findViewById(R.id.emptyResultMessage);

        Intent intent = getIntent();

        Serializable obj = intent.getSerializableExtra(LoginActivity.USER_EXTRA);

        if(obj instanceof Utente){
            utente = (Utente) obj;
        } else {
            utente = new Utente();
        }

        listView = findViewById(R.id.listView);

        switchInterfaceList();

        //un array adapter crea una vista per ogni oggetto della collezione che forniamo
        if(resetDefaultListView){
            CustomBaseAdapter customArrayAdapter = new CustomBaseAdapter(
                    getApplicationContext(), ListaUtenti.dbUtenti);
            listView.setAdapter(customArrayAdapter);
        }

        backHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent showResult = new Intent(ControlPanel.this, HomeActivity.class);
                showResult.putExtra(USER_EXTRA, utente);
                startActivity(showResult);
                finish();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(keywordSearch.getText().toString().length() == 0){
                    keywordSearch.setError("Inserisci una keyword per effettuare la ricerca!");
                } else {
                    ArrayList<Utente> results = new ArrayList<>();
                    results.clear();

                    for(Utente u : ListaUtenti.dbUtenti){
                        if(u.getUsername().toLowerCase().contains(keywordSearch.getText().toString().toLowerCase())){
                            results.add(u);
                        }
                    }

                    if(results.size() != 0){
                        resetDefaultListView = false;
                        CustomBaseAdapter resultsArrayAdapter = new CustomBaseAdapter(
                                getApplicationContext(), results);
                        listView.setAdapter(resultsArrayAdapter);
                    } else {
                        emptyResultMessage.setVisibility(View.VISIBLE);
                        emptyResultImage.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                    }

                }
            }
        });

        resetSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResetDefaultListView();
                CustomBaseAdapter customArrayAdapter = new CustomBaseAdapter(
                        getApplicationContext(), ListaUtenti.dbUtenti/*resultsToShow*/);
                listView.setAdapter(customArrayAdapter);

                emptyResultMessage.setVisibility(View.GONE);
                emptyResultImage.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void switchInterfaceList(){
        if(ListaUtenti.dbUtenti.size() == 0){
            listView.setVisibility(View.GONE);
            emptyResultMessage.setVisibility(View.VISIBLE);
            emptyResultImage.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.VISIBLE);
            emptyResultMessage.setVisibility(View.GONE);
            emptyResultImage.setVisibility(View.GONE);
        }
    }

    public boolean getResetDefaultListView() {
        return resetDefaultListView;
    }

    public void setResetDefaultListView() {
        this.resetDefaultListView = true;
    }
}