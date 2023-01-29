package com.unica.esbonusium2022mattiacani;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModificaPasswordActivity extends AppCompatActivity {
    Utente utente;
    Button changePasswordButton, cancelChangeButton;
    EditText oldPass, newPass;
    TextView errorText, recapUsername, recapCity, recapDate;

    public static final String USER_EXTRA = "com.unica.esbonusium2022mattiacani.Utente";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_password);

        getSupportActionBar().hide();

        oldPass = findViewById(R.id.editOldpassword);
        newPass = findViewById(R.id.editNewPassword);
        changePasswordButton = findViewById(R.id.modifyPasswordButton);
        cancelChangeButton = findViewById(R.id.cancelEditPassword);
        errorText = findViewById(R.id.error);
        recapUsername = findViewById(R.id.recapUsername);
        recapCity = findViewById(R.id.recapCity);
        recapDate = findViewById(R.id.recapDate);

        Intent intent = getIntent();

        Serializable obj = intent.getSerializableExtra(LoginActivity.USER_EXTRA);

        if(obj instanceof Utente){
            utente = (Utente) obj;
        } else {
            utente = new Utente();
        }

        //prendo l'indice del database dell'utente loggato in questo momento
        int loggedUserIndex = ListaUtenti.dbUtenti.indexOf(utente);

        recapUsername.setText("Username: " + utente.getUsername());
        recapCity.setText("Città: " + utente.getCittaProvenienza());
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        recapDate.setText("Data di nascita: "+ format.format(utente.getDataNascita().getTime()));

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkInput()){
                    Intent showResult = new Intent(ModificaPasswordActivity.this, LoginActivity.class);

                    if(checkInput()){
                        utente.setPassword(newPass.getText().toString());
                        ListaUtenti.dbUtenti.set(loggedUserIndex, utente);
                    }

                    showResult.putExtra(USER_EXTRA, utente);
                    startActivity(showResult);
                    finish();
                }
            }
        });

        cancelChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent annulla = new Intent(ModificaPasswordActivity.this, HomeActivity.class);
                startActivity(annulla);
                finish();
            }
        });
    }

    private boolean checkInput(){
        int errors = 0;
        //editName ecc contengono i riferimenti all'edit text
        if(oldPass.getText().toString().length() == 0){
            errors++;
            oldPass.setError("Inserire la vecchia password");
        } else if(!(utente.getPassword().equals(oldPass.getText().toString()))){
            errors++;
            oldPass.setError("Questa password non coincide con la vecchia");
        }

        if(newPass.getText().toString().length() == 0){
            errors++;
            newPass.setError("Inserire la nuova password");
        } else if(newPass.getText().toString().equals(oldPass.getText().toString())){
            errors++;
            newPass.setError("La nuova password non può essere uguale alla vecchia");
        }

        if(newPass.getText().toString().length() < 8){
            errors++;
            newPass.setError("La password deve contenere almeno 8 caratteri");
        } else if(!(checkPassword(newPass.getText().toString()))){
            errors++;
            newPass.setError("La password deve contenere almeno 8 caratteri, una maiuscola, una minuscola, un numero e un carattere speciale");
        }

        switch(errors){
            case 0:
                errorText.setVisibility(View.GONE);
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

    private boolean checkPassword(String password) {
        //definisco i pattern di ricerca compilando le regex per farle diventare dei pattern
        Pattern carattere = Pattern.compile("[a-zA-Z]");
        Pattern cifra = Pattern.compile("[0-9]");
        Pattern carattereSpeciale = Pattern.compile ("[!@\"£/#$%&*()_+=|<>?{}\\[\\]~-]");

        //definisco dei matcher che ci consentono di effettuare delle operazioni sui pattern
        //data la stringa password presa in input
        Matcher hasLetter = carattere.matcher(password);
        Matcher hasDigit = cifra.matcher(password);
        Matcher hasSpecial = carattereSpeciale.matcher(password);

        //se vengono trovate lettere, simboli e caratteri speciali, allora la password soddisfa i
        //criteri di sicurezza adottati dalle specifile
        return hasLetter.find() && hasDigit.find() && hasSpecial.find();
    }
}