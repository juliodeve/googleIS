package com.vazquez.julio.googleis.PARSERS;

import com.vazquez.julio.googleis.POJO.Trayecto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by julio on 19/02/2018.
 */

public class ParserTrayecto {
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
}
