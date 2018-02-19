package com.vazquez.julio.googleis.PARSERS;

import com.vazquez.julio.googleis.POJO.Coordenadas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by julio on 19/02/2018.
 */

public class ParserCoordenadas {
    public static List<Coordenadas> parser (String content){

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
