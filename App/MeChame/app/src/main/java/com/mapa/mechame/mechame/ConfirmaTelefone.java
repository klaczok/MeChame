package com.mapa.mechame.mechame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class ConfirmaTelefone extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirma_telefone);


        ImageView btnConfirmaTelefone = (ImageView) findViewById(R.id.btnValidarTelefone);

        btnConfirmaTelefone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UsuarioDAO dao = new UsuarioDAO();
                TextView txtTelefone = (TextView) findViewById(R.id.txtTelefone);

                if(validacao(txtTelefone.getText().toString())){
                    dao.telefoneToJson(txtTelefone.getText().toString());
                    finish();
                    Intent itProsseguir = new Intent(ConfirmaTelefone.this, CadastroUsuario.class);
                    startActivity(itProsseguir);
                }
            }
        });


    }


    private boolean validacao(String telefone){
        String mensagem = "";

        if(telefone.equals("")) {
            mensagem += "\nTelefone Obrigatório";
        }

        if(telefone.length() < 13)
           mensagem +="\nDigite corretamente o Telefone !";

        if (!mensagem.equals("")) {
            AlertDialog alerta;
            AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmaTelefone.this);
            builder.setTitle("Atenção !");
            builder.setNegativeButton("Fechar",null);
            builder.setMessage(mensagem);
            alerta = builder.create();
            alerta.show();
        }

        return mensagem.equals("");
    }
}
