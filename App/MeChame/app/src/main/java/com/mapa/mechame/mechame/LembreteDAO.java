package com.mapa.mechame.mechame;

import android.util.Log;

import com.google.gson.Gson;

/**
 * Created by Rafael on 11/05/2015.
 */
public class LembreteDAO {


    public void lembreteToJson(String titulo, double raio, boolean ativo, double lat, double longi){

        Lembrete lembrete = new Lembrete();
        Gson gson = new Gson();
        String json;

        lembrete.setTitulo(titulo);
        lembrete.setRaio(raio);
        lembrete.setAtivo(ativo);
        lembrete.setLatitude(lat);
        lembrete.setLongitude(longi);

        json = gson.toJson(lembrete);

        Log.d("Json", " Lembrete Completo -> " + json);

    }
}
