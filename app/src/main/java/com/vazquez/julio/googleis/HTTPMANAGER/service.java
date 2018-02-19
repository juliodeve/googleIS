package com.vazquez.julio.googleis.HTTPMANAGER;

import android.util.Base64;

import com.vazquez.julio.googleis.POJO.Coordenadas;
import com.vazquez.julio.googleis.POJO.Trayecto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by julio on 06/02/2018.
 */

public class service {
    public String enviarPost(String mail, String pass, String uri) {
        String parametros = "mail=" + mail + "&pass=" + pass;
        HttpURLConnection connection = null;
        String respuesta = "";
        byte[] loginBytes = ("julio" + ":" + "julio").getBytes();
        StringBuilder loginBuilder = new StringBuilder()
                .append("Basic ")
                .append(Base64.encodeToString(loginBytes, Base64.DEFAULT));

        try {
            URL url = new URL(uri);
            connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("Authorization", loginBuilder.toString());
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(parametros.getBytes().length));
            connection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(parametros);
            wr.close();
            Scanner inStream = new Scanner(connection.getInputStream());
            while (inStream.hasNextLine()) {
                respuesta += (inStream.nextLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respuesta.toString();
    }

    public String enviarPost(String mail, String uri) {
        String parametros = "mail=" + mail;
        HttpURLConnection connection = null;
        String respuesta = "";
        byte[] loginBytes = ("julio" + ":" + "julio").getBytes();
        StringBuilder loginBuilder = new StringBuilder()
                .append("Basic ")
                .append(Base64.encodeToString(loginBytes, Base64.DEFAULT));

        try {
            URL url = new URL(uri);
            connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("Authorization", loginBuilder.toString());
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(parametros.getBytes().length));
            connection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(parametros);
            wr.close();
            Scanner inStream = new Scanner(connection.getInputStream());
            while (inStream.hasNextLine()) {
                respuesta += (inStream.nextLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respuesta.toString();
    }

    public String enviarPost(String mail, String horaI, String horaF, String fecha, double velocidad, double distancia, String duracion, int calorias, String uri) {
        String parametros = "mail=" + mail + "&horaI=" + horaI + "&horaF=" + horaF + "&fecha=" + fecha + "&velocidad=" + velocidad + "&distancia=" + distancia + "&duracion" + duracion + "&calorias=" + calorias;
        HttpURLConnection connection = null;
        String respuesta = "";
        byte[] loginBytes = ("julio" + ":" + "julio").getBytes();
        StringBuilder loginBuilder = new StringBuilder()
                .append("Basic ")
                .append(Base64.encodeToString(loginBytes, Base64.DEFAULT));

        try {
            URL url = new URL(uri);
            connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("Authorization", loginBuilder.toString());
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(parametros.getBytes().length));
            connection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(parametros);
            wr.close();
            Scanner inStream = new Scanner(connection.getInputStream());
            while (inStream.hasNextLine()) {
                respuesta += (inStream.nextLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respuesta.toString();
    }

    public String enviarPost(String hora, String idTrajet, double latitud, double longitud, String uri) {
        String parametros = "idTrajet=" + idTrajet + "&latitud=" + latitud + "&longitud=" + longitud + "&hora=" + hora;
        HttpURLConnection connection = null;
        String respuesta = "";
        byte[] loginBytes = ("julio" + ":" + "julio").getBytes();
        StringBuilder loginBuilder = new StringBuilder()
                .append("Basic ")
                .append(Base64.encodeToString(loginBytes, Base64.DEFAULT));

        try {
            URL url = new URL(uri);
            connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("Authorization", loginBuilder.toString());
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(parametros.getBytes().length));
            connection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(parametros);
            wr.close();
            Scanner inStream = new Scanner(connection.getInputStream());
            while (inStream.hasNextLine()) {
                respuesta += (inStream.nextLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respuesta.toString();
    }

    public String enviarPost(int idTrajet, String uri) {
        String parametros = "idTrajet=" + idTrajet;
        HttpURLConnection connection = null;
        String respuesta = "";
        byte[] loginBytes = ("julio" + ":" + "julio").getBytes();
        StringBuilder loginBuilder = new StringBuilder()
                .append("Basic ")
                .append(Base64.encodeToString(loginBytes, Base64.DEFAULT));

        try {
            URL url = new URL(uri);
            connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("Authorization", loginBuilder.toString());
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(parametros.getBytes().length));
            connection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(parametros);
            wr.close();
            Scanner inStream = new Scanner(connection.getInputStream());
            while (inStream.hasNextLine()) {
                respuesta += (inStream.nextLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respuesta.toString();
    }

    public static List<Trayecto> parser (String content){

        try {
            JSONArray jsonArray = new JSONArray(content);
            List<Trayecto> usuarioList = new ArrayList<>();

            for (int i =0; i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Trayecto usuario = new Trayecto();

                usuario.setIdTrayecto(jsonObject.getInt("idTrajet"));
                usuario.setEmail(jsonObject.getString("mail"));
                usuario.setCalorias(jsonObject.getInt("calorias"));
                usuario.setDistancia(jsonObject.getDouble("distancia"));
                usuario.setDuracion(jsonObject.getString("duracion"));
                usuario.setFecha(jsonObject.getString("fecha"));
                usuario.setHoraF(jsonObject.getString("horaf"));
                usuario.setHoraI(jsonObject.getString("horaI"));
                usuario.setVelocidad(jsonObject.getDouble("velocidad"));

                usuarioList.add(usuario);
            }
            return usuarioList;
        } catch (JSONException e) {
            e.printStackTrace();
            return  null;
        }
    }

    public static List<Coordenadas> parser2 (String content){

        try {
            JSONArray jsonArray = new JSONArray(content);
            List<Coordenadas> usuarioList = new ArrayList<>();

            for (int i =0; i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Coordenadas usuario = new Coordenadas();

                usuario.setIdTrajet(jsonObject.getInt("idTrajet"));
                usuario.setLatitud(jsonObject.getDouble("latitud"));
                usuario.setLongitud(jsonObject.getDouble("longitud"));
                usuario.setHora(jsonObject.getString("hora"));

                usuarioList.add(usuario);
            }
            return usuarioList;
        } catch (JSONException e) {
            e.printStackTrace();
            return  null;
        }
    }
}
