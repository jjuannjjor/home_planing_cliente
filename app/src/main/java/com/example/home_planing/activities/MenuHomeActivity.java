package com.example.home_planing.activities;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
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
import com.example.home_planing.entities.Home;
import com.example.home_planing.entities.User;
import com.example.home_planing.adapters.HomeAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuHomeActivity extends AppCompatActivity {

    EditText et_code;
    Button crear_home_btn, añadir_btn;
    int id;
    String nombre;
    String apellido;
    String email;
    ListView lista;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getList();

        Intent intent = getIntent();
        // Extraer los datos pasados por el Intent
        id = intent.getIntExtra("id", -1); // Valor por defecto -1 si no se encuentra la clave
        Long idL = (long) id;
        String housecode = intent.getStringExtra("housecode");

        lista = findViewById(R.id.listView);
        et_code = findViewById(R.id.name_home);
        et_code.setText(housecode);
        añadir_btn = findViewById(R.id.añadir_home_btn);
        añadir_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processFormFields();
            }
        });

        crear_home_btn = findViewById(R.id.crear_home_btn);


        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Home casa = casas.get(position);
                //Toast.makeText(MenuHomeActivity.this, "id casa: " + casa.getId(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MenuHomeActivity.this, List2taskActivity.class);
                intent.putExtra("home_id", casa.getId());
                intent.putExtra("user_id", idL);
                //intent.putExtra("housecode", housecode);
                startActivity(intent);

            }
        });
    }

    public void goToNewHome(View view) {
        Intent intent = new Intent(MenuHomeActivity.this, NewHomeActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);

    }



    public void processFormFields() {
        // Valida el campo nombre
        if (!validatecode()) return;

        // Inicializa la cola de solicitudes de Volley
        RequestQueue queue = Volley.newRequestQueue(MenuHomeActivity.this);
        String url = "http://192.168.79.172:8080/home/register/usehome";

        // Crea una solicitud StringRequest
        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Limpia el campo nombre
                et_code.setText(null);

                // Maneja la respuesta del servidor
                if (response.equals("success")) {
                    Toast.makeText(MenuHomeActivity.this, "Hogar añadido completado", Toast.LENGTH_SHORT).show();
                    recreate();
                } else if (response.equals("pertenece")) {
                    Toast.makeText(MenuHomeActivity.this, "Ya perteneces a ese hogar", Toast.LENGTH_SHORT).show();
                    recreate();
                } else if (response.equals("Casanoencontrada")) {
                    Toast.makeText(MenuHomeActivity.this, "Ese Hogar no existe", Toast.LENGTH_SHORT).show();
                    recreate();
                } else {
                    Toast.makeText(MenuHomeActivity.this, "Fallo al registrar " + response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Maneja el error de la solicitud
                error.printStackTrace();
                Log.e("VolleyError", error.getMessage() != null ? error.getMessage() : "Unknown error");
                Toast.makeText(MenuHomeActivity.this, "Error al unirte a la casa", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Configura los parámetros de la solicitud
                Map<String, String> params = new HashMap<>();
                params.put("housecode", et_code.getText().toString());
                params.put("id", String.valueOf(id));
                return params;
            }
        };

        // Añade la solicitud a la cola
        queue.add(stringRequest);
    }

    public boolean validatecode() {
        String code = et_code.getText().toString();
        if (code.isEmpty()) {
            et_code.setError("El codigo es obligatorio");
            return false;
        } else {
            et_code.setError(null);
            return true;
        }
    }
    private HomeAdapter adapter;
    List<Home> casas;
    public void getList(){

        RequestQueue queue = Volley.newRequestQueue(MenuHomeActivity.this);
        String url = "http://192.168.79.172:8080/home/getHomes";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Parsear la respuesta JSON a una lista de objetos Casa
                casas = parseJsonToList(response);
                //Toast.makeText(MenuHomeActivity.this, "Lista: " + casas, Toast.LENGTH_SHORT).show();
                
                // Utilizar la lista de casas
                adapter = new HomeAdapter(MenuHomeActivity.this, casas);
                lista.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Manejar el error
                error.printStackTrace();
                Toast.makeText(MenuHomeActivity.this, "Error al obtener datos: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Configura los parámetros de la solicitud
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(id));
                return params;
            }
        };
        queue.add(stringRequest);
    }
    public static List<Home> parseJsonToList(String json) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Home>>(){}.getType();
        return gson.fromJson(json, listType);
    }
}