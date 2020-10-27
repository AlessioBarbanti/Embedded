package com.example.bluetootharduino;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.CountDownTimer;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;


public class Utilities {

    public static String trash_type;
    public static String token;
    public static Boolean DumpsterAvail;
    public static String url; //complete with Ngrok URL (before ".ngrok")
    public static Boolean serviceFragment = false;
    public static String tokenReceived;


    static AlertDialog createTimeDialog(final Activity activity, final CountDownTimer countDownTimer){
        final AlertDialog dialog = new AlertDialog.Builder(activity)
                .setMessage("Tempo incrementato di 10s")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        countDownTimer.start();
                    }
                })
                .create();
        return dialog;
    }

    static AlertDialog createDialog(final Activity activity, final String message){
        final AlertDialog dialog = new AlertDialog.Builder(activity)
                .setMessage(message)
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                      activity.finish();
                    }
                })
                .create();
        return dialog;
    }

    static AlertDialog createFailLoginDialog(final Activity activity, final String message){
        final AlertDialog dialog = new AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .create();
        return dialog;
    }

    public static void aggiornaIndirizzo(String indirizzo){
        url = "https://"+indirizzo+".ngrok.io/api/";
    }
}

