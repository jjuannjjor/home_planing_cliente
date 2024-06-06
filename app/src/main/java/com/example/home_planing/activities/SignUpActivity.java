package com.example.home_planing.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.home_planing.R;
import com.example.home_planing.helpers.StringHelper;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    EditText nombre, apellido, email, password, confirm_password;
    Button sign_up_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        nombre = findViewById(R.id.nombre_et);
        apellido = findViewById(R.id.apellido_et);
        email = findViewById(R.id.email_et);
        password = findViewById(R.id.password_et);
        confirm_password = findViewById(R.id.confirm_et);

        sign_up_btn = findViewById(R.id.sign_up_btn);

        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processFormFields();
            }
        });

    }

    public void goToHome(View view) { //Volver Main activity
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(intent);

    }
    public void goToSignIn (View view){ //Ir a SignIn activity
        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
        startActivity(intent);


    }
    public void processFormFields(){ // crear cuenta en el servidor

        //validar los datos
        if (!validateNombre()||!validateApellido()||!validateEmail()||!validatePasswordAndConfirm()) return;

        RequestQueue queue = Volley.newRequestQueue(SignUpActivity.this);
        String url = "http://192.168.79.172:8080/user/register";
        //consulta a la API
        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(response.equalsIgnoreCase("success")){

                    //Vaciar valores Text View
                    nombre.setText(null);
                    apellido.setText(null);
                    email.setText(null);
                    password.setText(null);
                    confirm_password.setText(null);
                    Toast.makeText(SignUpActivity.this, "Cuenta creada", Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {  //error en la conexion
                error.printStackTrace();
                System.out.println(error.getMessage());
                Toast.makeText(SignUpActivity.this, "Error al crear cuenta", Toast.LENGTH_SHORT).show();

            }
        }){
            @Nullable
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {

                //enviar datos al servidor
                Map<String,String> params = new HashMap<>();
                params.put("nombre", nombre.getText().toString());
                params.put("apellido", apellido.getText().toString());
                params.put("email", email.getText().toString());
                params.put("password", password.getText().toString());
                return params;
            }
        };

        queue.add(stringRequest);


    }

    public boolean validateNombre(){ //validar nombre
        String firstName = nombre.getText().toString();
        if(firstName.isEmpty()){
            nombre.setError("El nombre es obligatorio");
            return false;
        }else {
            nombre.setError(null);
            return true;
        }
    }
    public boolean validateApellido(){ //validar apellido
        String lastName = apellido.getText().toString();
        if(lastName.isEmpty()){
            apellido.setError("El apellido es obligatorio");
            return false;
        }else {
            apellido.setError(null);
            return true;
        }
    }

    public boolean validateEmail(){ //validar email
        String email_o = email.getText().toString();
        if(email_o.isEmpty()){
            email.setError("El correo es obligatorio");
            return false;
        }else if (!StringHelper.regexEmailValidationPattern(email_o)){ //validar formato email
            email.setError("Formato de correo incorrecto");
            return false;
        }else {
            email.setError(null);
            return true;
        }
    }

    public boolean validatePasswordAndConfirm(){ //validar password y confirmar password
        String password_o = password.getText().toString();
        String confirm_o = confirm_password.getText().toString();


        if(password_o.isEmpty()){
            password.setError("La contraseña es obligatoria");
            return false;
        }else if(confirm_o.isEmpty()){
            confirm_password.setError("la contraseña de confirmacion es obligatoria");
            return false;
        }else if (!StringHelper.regexPasswordValidationPattern(password_o)){ //validar formato password
            password.setError("Al menos 8 caracteres, " +
                    "una letra mayuscula, " +
                    "una letra minuscula, " +
                    "un caracter especial, " +
                    "y un número");
            return false;
        }else if (!password_o.equals(confirm_o)){ //validar que password y confirmar password sean iguales
            confirm_password.setError("No son iguales");
            return false;
        }else{
            password.setError(null);
            confirm_password.setError(null);
            return true;
        }
    }


}