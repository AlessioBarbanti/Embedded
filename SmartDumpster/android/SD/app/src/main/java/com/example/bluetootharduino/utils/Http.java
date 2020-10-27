package com.example.bluetootharduino.utils;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Http {

    public interface Listener{
        void onHttpResponseAvailable(final HttpResponse response) throws IOException, JSONException;
    }

    public static void get(final String url, final Listener listener){
        new AsyncTask<Void, Void, HttpResponse>(){
            @Override
            protected HttpResponse doInBackground(Void... voids) {
                final HttpURLConnection connection;
                try {
                    connection = (HttpURLConnection) new URL(url).openConnection();
                    connection.setRequestMethod("GET");
                    return new HttpResponse(connection.getResponseCode(), connection.getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(HttpResponse response) {
                try {
                    listener.onHttpResponseAvailable(response);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    public static void post(final String url, final byte[] payload, final Listener listener){
        new AsyncTask<Void, Void, HttpResponse>(){
            @Override
            protected HttpResponse doInBackground(Void... voids) {
                final HttpURLConnection connection;
                try {
                    connection = (HttpURLConnection) new URL(url).openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);
                    connection.getOutputStream().write(payload);

                    return new HttpResponse(connection.getResponseCode(), connection.getInputStream());
                } catch (IOException e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(HttpResponse response) {
                try {
                    listener.onHttpResponseAvailable(response);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }
}
