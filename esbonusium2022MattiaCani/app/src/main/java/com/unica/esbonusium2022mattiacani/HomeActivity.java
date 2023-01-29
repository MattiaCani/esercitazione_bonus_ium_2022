package com.unica.esbonusium2022mattiacani;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class HomeActivity extends AppCompatActivity {
    public final static String USERNAME_ADMIN = "admin";
    public final static String PASSWORD_ADMIN = "admin";

    //variabile che salva i dati dell'oggetto Person ricevuto
    Utente utente;
    TextView resultNickname, resultCity, resultDate, modificaPassword, cityField, dateField;
    Button logoutButton, manageUsersButton;
    ImageView userBanner, adminBanner, userPic, adminPic;

    int userPics[] = {R.drawable.user};

    public static final String USER_EXTRA = "com.unica.esbonusium2022mattiacani.Utente";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().hide();

        //setto le variabili associate agli elementi della UI
        resultNickname = findViewById(R.id.resultNickname);
        cityField = findViewById(R.id.cityField);
        resultCity = findViewById(R.id.resultCity);
        dateField = findViewById(R.id.dateField);
        resultDate = findViewById(R.id.resultDate);
        manageUsersButton = findViewById(R.id.manageUsersButton);
        logoutButton = findViewById(R.id.logoutButton);
        modificaPassword = findViewById(R.id.modifyPassword);
        userBanner = findViewById(R.id.bannerNormale);
        adminBanner = findViewById(R.id.bannerAdmin);
        userPic = findViewById(R.id.userPic);
        adminPic = findViewById(R.id.adminPic);

        //estraggo gli username dal database e li converto ad un array di stringhe
        ArrayList<String> gettedUserList = ListaUtenti.getUsernameArrayList();
        String[] userList = gettedUserList.toArray(new String[gettedUserList.size()]);

        Intent intent = getIntent();

        Serializable obj = intent.getSerializableExtra(LoginActivity.USER_EXTRA);

        if(obj instanceof Utente){
            utente = (Utente) obj;
        } else {
            utente = new Utente();
        }

        //setto l'interfaccia in base al tipo di utente
        switchInterface();

        manageUsersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent showResult = new Intent(HomeActivity.this, ControlPanel.class);

                //put extra ci permette di aggangciare al nostro oggetto degli ulteriori
                //dati. Ora agganciamo la persona che ha i dati utente.
                //passiamo una chiave che è PERSON_EXTRA, il quale di solito per convenzione
                //è il package. Diciamo che lo stiamo lanciando all'altra activity, che deve
                //prendere questi dati. L'intent è come una palla che lanciamo e l'altra activity
                //di destinazione la deve acchiappare.
                showResult.putExtra(USER_EXTRA, utente);
                startActivity(showResult);
                finish();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent showResult = new Intent(HomeActivity.this, LoginActivity.class);

                //put extra ci permette di aggangciare al nostro oggetto degli ulteriori
                //dati. Ora agganciamo la persona che ha i dati utente.
                //passiamo una chiave che è PERSON_EXTRA, il quale di solito per convenzione
                //è il package. Diciamo che lo stiamo lanciando all'altra activity, che deve
                //prendere questi dati. L'intent è come una palla che lanciamo e l'altra activity
                //di destinazione la deve acchiappare.
                showResult.putExtra(USER_EXTRA, utente);

                Toast.makeText(HomeActivity.this, "Sei stato disconnesso!", Toast.LENGTH_SHORT).show();

                startActivity(showResult);
                finish();
            }
        });

        modificaPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent showResult = new Intent(HomeActivity.this, ModificaPasswordActivity.class);

                //put extra ci permette di aggangciare al nostro oggetto degli ulteriori
                //dati. Ora agganciamo la persona che ha i dati utente.
                //passiamo una chiave che è PERSON_EXTRA, il quale di solito per convenzione
                //è il package. Diciamo che lo stiamo lanciando all'altra activity, che deve
                //prendere questi dati. L'intent è come una palla che lanciamo e l'altra activity
                //di destinazione la deve acchiappare.
                showResult.putExtra(USER_EXTRA, utente);
                startActivity(showResult);
            }
        });

        updateTextViews();
        updateProfilePicture();
    }

    void updateTextViews(){
        if(isAdmin(resultNickname.getText().toString(), utente.getPassword())){
            resultNickname.setText(utente.getUsername());
        } else {
            resultNickname.setText(utente.getUsername());
            resultCity.setText(utente.getCittaProvenienza());

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            resultDate.setText(format.format(utente.getDataNascita().getTime()));
        }
    }

    void updateProfilePicture(){
        if(utente.getPicId() == 1){
            userPic.setImageResource(R.drawable.avatar1);
        } else if(utente.getPicId() == 2){
            userPic.setImageResource(R.drawable.avatar2);
        } else if(utente.getPicId() == 3){
            userPic.setImageResource(R.drawable.avatar3);
        }
    }

    public void switchInterface(){
        //if usato per mostrare elementi diversi dell'interfaccia in base ad utente normale o utente admin
        if(utente.getUsername().equals(USERNAME_ADMIN) && utente.getPassword().equals(PASSWORD_ADMIN)){
            userBanner.setVisibility(View.GONE);
            userPic.setVisibility(View.GONE);
            adminBanner.setVisibility(View.VISIBLE);
            adminPic.setVisibility(View.VISIBLE);
            modificaPassword.setVisibility(View.GONE);

            resultNickname.setText(USERNAME_ADMIN);
            cityField.setVisibility(View.GONE);
            resultCity.setVisibility(View.GONE);
            dateField.setVisibility(View.GONE);
            resultDate.setVisibility(View.GONE);

            //Log.d("SWITCH_INTERFACE", "Ho switchato l'interfaccia. " + utente.getUsername() + " è: " + utente.getAdmin());
            Log.d("SWITCH_INTERFACE", "Admin root");
        } else if(utente.getRootFlag() || utente.getAdmin()){
            userBanner.setVisibility(View.GONE);
            updateProfilePicture();
            adminBanner.setVisibility(View.VISIBLE);
            modificaPassword.setVisibility(View.VISIBLE);

            resultNickname.setText(USERNAME_ADMIN);
            cityField.setVisibility(View.VISIBLE);
            resultCity.setVisibility(View.VISIBLE);
            dateField.setVisibility(View.VISIBLE);
            resultDate.setVisibility(View.VISIBLE);

            Log.d("SWITCH_INTERFACE", "Utente normale diventato admin");
        } else {
            userBanner.setVisibility(View.VISIBLE);
            userPic.setVisibility(View.VISIBLE);
            adminBanner.setVisibility(View.GONE);
            adminPic.setVisibility(View.GONE);
            modificaPassword.setVisibility(View.VISIBLE);
            manageUsersButton.setVisibility(View.GONE);

            Log.d("SWITCH_INTERFACE", "Utente normale");
        }
    }

    public boolean isAdmin(String user, String password){
        if(user.equals(USERNAME_ADMIN) && password.equals(PASSWORD_ADMIN)){
            return true;
        }

        return false;
    }
}