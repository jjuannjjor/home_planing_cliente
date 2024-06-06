package com.example.home_planing.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.home_planing.R;
import com.example.home_planing.helpers.StringHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SignInActivity extends AppCompatActivity {


    Button sign_in_btn;
    EditText et_email, et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        et_email = findViewById(R.id.email);
        et_password = findViewById(R.id.password);


        sign_in_btn = findViewById(R.id.sign_in_btn);
        sign_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authenticateUser();
            }
        });
    }

    public void authenticateUser() {//Autenticar el usuario

        //Validar que los datos no esten vacios
        if(!validateEmail() || !validatePassword()) return;

        //Create Request Queue
        RequestQueue queue = Volley.newRequestQueue(SignInActivity.this);
        String url = "http://192.168.79.172:8080/user/login";

        //Set parameters
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("email", et_email.getText().toString());
        params.put("password", et_password.getText().toString());

        //Set Request Object
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            //Get Values From Response Object
                            int id = (Integer) response.get("id");
                            String nombre = (String) response.get("nombre");
                            String apellido = (String) response.get("apellido");
                            String email = (String) response.get("email");

                            //Toast.makeText(SignInActivity.this, "ID user: "+id, Toast.LENGTH_LONG).show();

                            Intent goToProfile = new Intent(SignInActivity.this, MenuHomeActivity.class);

                            goToProfile.putExtra("id", id);
                            goToProfile.putExtra("nombre", nombre);
                            goToProfile.putExtra("apellido", apellido);
                            goToProfile.putExtra("email", email);

                            startActivity(goToProfile);


                        }catch (JSONException e){ //Si hay un error en la respuesta
                            e.printStackTrace();
                            System.out.println(e.getMessage());

                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.out.println(error.getMessage());
                Toast.makeText(SignInActivity.this, "Fallo al iniciar sesión", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(jsonObjectRequest);
    }

    public void goToHome(View view) { //ir a la pantalla principal
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    public void goToSignUp (View view){ //Ir a la pantalla de registro
        Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
        startActivity(intent);

    }

    public boolean validateEmail(){ //Validar que el email no este vacio y tenga un formato valido
        String email = et_email.getText().toString();
        if(email.isEmpty()){
            et_email.setError("El email es obligatorio");
            return false;
        }else if (!StringHelper.regexEmailValidationPattern(email)){
            et_email.setError("El email es invalido");
            return false;
        }else {
            et_email.setError(null);
            return true;
        }
    }

    public boolean validatePassword(){ //Validar que la contraseña no este vacia
        String password = et_password.getText().toString();

        if(password.isEmpty()){
            et_password.setError("Contraseña obligatoria");
            return false;
        }else{
            et_password.setError(null);
            return true;
        }
    }
}