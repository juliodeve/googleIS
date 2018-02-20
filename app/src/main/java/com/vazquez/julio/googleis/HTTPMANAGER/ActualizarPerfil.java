package com.vazquez.julio.googleis.HTTPMANAGER;

import android.util.Base64;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by julio on 20/02/2018.
 */

public class ActualizarPerfil {
    public String enviarPost(String mail, String nom, double peso, int altura,String nacimiento, int sexo, String uri) {
        String parametros = "mail=" + mail + "&nom=" + nom + "&peso=" + peso + "&altura=" + altura + "&nacimiento=" + nacimiento + "&sexo=" + sexo ;
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
}
