package com.unica.esbonusium2022mattiacani;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {
    EditText editNickname, editPassword, editRepeatPassword, editCity, editDate;
    Button regButton, cancelRegistrationButton, avatar1Button, avatar2Button, avatar3Button;
    Utente utente;
    TextView errorText;
    private boolean utenteGiaEsistente = false;

    //salvo l'anno corrente in una variabile
    Calendar calendar = Calendar.getInstance();
    Integer currentYear = calendar.get(Calendar.YEAR);

    //dichiarazione di costante pubblica
    public static final String USER_EXTRA = "com.unica.esbonusium2022mattiacani.Utente";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        getSupportActionBar().hide();

        editNickname = findViewById(R.id.editNickname);
        editPassword = findViewById(R.id.editPassword);
        editRepeatPassword = findViewById(R.id.editRepeatPassword);
        editCity = findViewById(R.id.editCity);
        editDate = findViewById(R.id.editDate);
        errorText = findViewById(R.id.error);
        regButton = findViewById(R.id.regButton);
        cancelRegistrationButton = findViewById(R.id.cancelRegistrationButton);
        avatar1Button = findViewById(R.id.pick1);
        avatar2Button = findViewById(R.id.pick2);
        avatar3Button = findViewById(R.id.pick3);

        editDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                //all'interno della funzione dobbiamo msotrare il nostro dialog
                //copiamo dalla documentazione la show dialog
                showDialog();
            }
        });

        utente = new Utente();

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkInput()){
                    updatePerson();

                    Intent showResult = new Intent(RegistrationActivity.this, LoginActivity.class);

                    utente.setUserId(ListaUtenti.dbUtenti.size()+1);
                    ListaUtenti.dbUtenti.add(utente);

                    Toast.makeText(RegistrationActivity.this, "Utente registrato!", Toast.LENGTH_SHORT).show();

                    //showResult.putExtra(USER_EXTRA, utente);
                    startActivity(showResult);
                    finish();
                }
            }
        });

        cancelRegistrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        avatar1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                utente.setPicId(1);
                Toast.makeText(RegistrationActivity.this, "Selezionato avatar 1!", Toast.LENGTH_SHORT).show();
            }
        });

        avatar2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                utente.setPicId(2);
                Toast.makeText(RegistrationActivity.this, "Selezionato avatar 2!", Toast.LENGTH_SHORT).show();
            }
        });

        avatar3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                utente.setPicId(3);
                Toast.makeText(RegistrationActivity.this, "Selezionato avatar 3!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void doPositiveClick(Calendar date){
        // 26/10/2022
        this.utente.setDataNascita(date);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        editDate.setText(format.format(date.getTime()));
    }

    private void showDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        //mettiamo support perché il codice essendo pulito non estende più la classe di prima
        newFragment.show(getSupportFragmentManager(), "dialog");
    }

    private boolean checkInput(){
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

        if(editPassword.getText().toString().length() < 8){
            errors++;
            editPassword.setError("La password deve contenere almeno 8 caratteri");
        } else if(!(checkPassword(editPassword.getText().toString()))){
            errors++;
            editPassword.setError("La password deve contenere almeno 8 caratteri, una maiuscola, una minuscola, un numero e un carattere speciale");
        }

        if(editRepeatPassword.getText().toString().length() == 0){
            errors++;
            editRepeatPassword.setError("Reinserisci la password");
        }

        if(!(editPassword.getText().toString().equals(editRepeatPassword.getText().toString()))){
            errors++;
            editRepeatPassword.setError("La password non coincide");
        }

        if(editCity.getText().toString().length() == 0){
            errors++;
            editCity.setError("Inserire una città");
        }

        if(editDate.getText().toString().length() == 0){
            errors++;
            editDate.setError("Inserire la data");
        } else if(currentYear - DatePickerFragment.pickedYear < 18){
            errors++;
            editDate.setError("Non puoi registrarti se sei minorenne!");
        }

        //controllo se l'username esiste già nel DB
        for(Utente u : ListaUtenti.dbUtenti){
            if(u.getUsername().equals(editNickname.getText().toString())){
                utenteGiaEsistente = true;
            }
        }

        if(utenteGiaEsistente){
            errors++;
            editNickname.setError("L'username scelto è già stato preso");
            utenteGiaEsistente = false;
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

    private void updatePerson(){
        String username = this.editNickname.getText().toString();
        this.utente.setUsername(username);
        String password = this.editPassword.getText().toString();
        this.utente.setPassword(password);
        String citta = this.editCity.getText().toString();
        this.utente.setCittaProvenienza(citta);
        this.utente.setUserId(ListaUtenti.dbUtenti.size());
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