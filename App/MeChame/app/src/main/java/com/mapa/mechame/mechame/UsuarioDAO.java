package com.mapa.mechame.mechame;

import android.util.Log;

import com.google.gson.Gson;

/**
 * Created by Rafael on 10/05/2015.
 */
public class UsuarioDAO {



    public void telefoneToJson(String tel){
        Usuario usuario = new Usuario();
        Gson gson = new Gson();
        String json;
        usuario.setTelefone(tel);
        json = gson.toJson(usuario);
        Log.d("Json"," Telefone do usuario -> "+json);

    }

    public void usuarioToJson(String nome, String email, String senha){
        Usuario usuario = new Usuario();
        Gson gson = new Gson();
        String json;
        usuario.getTelefone();

        usuario.setNome(nome);
        usuario.setSenha(senha);
        usuario.setEmail(email);

        json = gson.toJson(usuario);

        Log.d("Json"," Usuario Completo -> "+json);

    }
}
