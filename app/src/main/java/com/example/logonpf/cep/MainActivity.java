package com.example.logonpf.cep;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private EditText edtCep;
    private TextView txtRua;
    private TextView txtCidade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtCep = (EditText) findViewById(R.id.edtCEP);
        txtRua = (TextView) findViewById(R.id.txtRua);
        txtCidade = (TextView) findViewById(R.id.txtCidade);
    }

    public void consultar(View v) {

        RequestQueue q = Volley.newRequestQueue(this);

        String cep = edtCep.getText().toString();
        String url = "http://viacep.com.br/ws/" + cep + "/json/";

        JsonObjectRequest r = new JsonObjectRequest(url,null,
                                                    new BuscaCep(),
                                                    new Erro());

        q.add(r);

    }

    class Erro implements Response.ErrorListener {

        @Override
        public void onErrorResponse(VolleyError error) {

            if (error.networkResponse.statusCode == 400) {
                Toast.makeText(MainActivity.this,
                        "CEP INVÁLIDO!!!",
                        Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(MainActivity.this,
                        "Erro: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();

            }

        }
    }

    class BuscaCep implements Response.Listener<JSONObject>{

        @Override
        public void onResponse(JSONObject response) {
            Log.i("JSON_CEP", response.toString());
            // https://github.com/esensato/2TDSS.git
            try {

                Boolean erro = response.optBoolean("erro");
                if (erro != null && erro) {

                    txtRua.setText("CEP INVALIDO");
                    txtCidade.setText("CEP INVALIDO");

                } else {

                    String rua =  response.getString("logradouro");
                    String cidade = response.getString("localidade");

                    txtRua.setText(rua);
                    txtCidade.setText(cidade);

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
