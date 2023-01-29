package com.unica.esbonusium2022mattiacani;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    EditText editNickname, editPassword;
    Button loginButton;
    Utente utente;
    TextView errorText, newUser;

    //dichiarazione di costante pubblica
    public static final String USER_EXTRA = "com.unica.esbonusium2022mattiacani.Utente";

    public final static String USERNAME_ADMIN = "admin";
    public final static String PASSWORD_ADMIN = "admin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        //background animato schermata login
        ConstraintLayout constraintLayout = findViewById(R.id.mainLayout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();

        if(!(ListaUtenti.initialized)){
            ListaUtenti.initializeDB();
        }

        //prendiamo i riferimenti delle view dell'xml e li salviamo nelle variabili seguenti
        //usando la findview. La R sta per "Resources" un punto dell'app dove salviamo le risorse.
        editNickname = findViewById(R.id.editNickname);
        editPassword = findViewById(R.id.editPassword);
        errorText = findViewById(R.id.error);
        loginButton = findViewById(R.id.loginButton);
        newUser = findViewById(R.id.newUser);

        utente = new Utente();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //controllo che l'utente esista o meno nel database del sistema fatto dentro checkInput
                if(checkInput()){
                    updatePerson();

                    if(ListaUtenti.dbUtenti.contains(utente)){
                        if(!(isAdmin(utente.getUsername(), utente.getPassword()))){
                            utente = ListaUtenti.dbUtenti.get(ListaUtenti.dbUtenti.indexOf(utente));
                        }

                        Intent showResult = new Intent(LoginActivity.this, HomeActivity.class);

                        showResult.putExtra(USER_EXTRA, utente);
                        startActivity(showResult);
                        finish();
                    } else {
                        editNickname.setError("L'utente non è registrato nel sistema");
                    }
                }
            }
        });

        newUser.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent newUser = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(newUser);
            }
        });
    }

    private boolean checkInput(){
        boolean check = false;
        int errors = 0;
        //editName ecc contengono i riferimenti all'edit text
        if(editNickname.getText().toString().length() == 0){
            errors++;
            editNickname.setError("Inserire l'username");
        }

        if(editPassword.getText().toString().length() == 0){
            errors++;
            editPassword.setError("Inserire una password");
        }

        if(!(ListaUtenti.dbUtenti.contains(utente))){
            check = true;
            errorText.setVisibility(View.VISIBLE);
            errorText.setText("Credenziali non valide");
        }

        switch(errors){
            case 0:
                //se l'utente c'è non viene mostrato il messaggio, se non c'è altrimenti.
                if(check){
                    errorText.setVisibility(View.GONE);
                }
                break;
            case 1:
                errorText.setVisibility(View.VISIBLE);
                errorText.setText("Si è verificato un errore");
                break;
            default:
                errorText.setVisibility(View.VISIBLE);
                errorText.setText("Si sono verificati " + errors + " errori");
                break;
        }

        return errors == 0;
    }

    private void updatePerson(){
        String username = this.editNickname.getText().toString();
        this.utente.setUsername(username);
        String password = this.editPassword.getText().toString();
        this.utente.setPassword(password);
    }

    public boolean isAdmin(String user, String password){
        if(user.equals(USERNAME_ADMIN) && password.equals(PASSWORD_ADMIN)){
            return true;
        }

        return false;
    }
}