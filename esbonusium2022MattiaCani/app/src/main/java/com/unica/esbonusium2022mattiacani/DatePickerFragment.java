package com.unica.esbonusium2022mattiacani;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

//widget fusion calendario + fragment
public class DatePickerFragment extends DialogFragment {
    private Calendar date;
    public static Integer pickedYear;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //dichiaro un oggetto di tipo DatePicker
        DatePicker datePicker = new DatePicker(getActivity());

        //dobbiamo anche inizializzare la variabile date di tipo calendar.
        date = Calendar.getInstance();

        //ora al dateDialog dobbiamo impostare il datePicker. Usiamo la set view

        return new AlertDialog.Builder(getActivity())
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                date.set(Calendar.YEAR, datePicker.getYear());
                                pickedYear = datePicker.getYear();
                                date.set(Calendar.MONTH, datePicker.getMonth());
                                date.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());

                                ((RegistrationActivity)getActivity()).doPositiveClick(date);
                            }
                        }
                )
                .setView(datePicker)
                .create();
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }
}
