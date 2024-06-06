package com.example.home_planing.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
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
import com.example.home_planing.adapters.CustomAdapter;
import com.example.home_planing.entities.CombinedTask;
import com.example.home_planing.entities.Task;
import com.example.home_planing.entities.TaskDTO;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class List2taskActivity extends AppCompatActivity {

    private ListView listView;
    private CustomAdapter adapter;
    private TextView textView;
    private List<Task> taskList;
    private List<TaskDTO> taskListDTO;
    Long id_user, id_home;

    FloatingActionButton delete;
    String housecode;
    private static List2taskActivity instance;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list2task);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        instance = this;
        getCode();
        Intent intent = getIntent();
        // Extraer los datos pasados por el Intent
        id_user = intent.getLongExtra("user_id", -1); // Valor por defecto -1 si no se encuentra la clave
        id_home = intent.getLongExtra("home_id",-1);

        //Toast.makeText(List2taskActivity.this, "code:  " + housecode, Toast.LENGTH_SHORT).show();
        // Inicializar la lista de tareas
        taskList = new ArrayList<>();
        taskListDTO = new ArrayList<>();
        getList();

        listView = findViewById(R.id.listView);
        textView = findViewById(R.id.textView_code);
        delete = findViewById(R.id.floatingActionButton);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detelesuerhome();
            }
        });




    }
    public void updateuserhometask(Long id_task, boolean isChecked) {
        System.out.println(id_user+" "+id_home+" "+id_task+" "+"true");
        //Toast.makeText(List2taskActivity.this, id_user+" "+id_home+" "+id_task+" "+"true", Toast.LENGTH_SHORT).show();
        if (isChecked==true){
            updateuserhome(id_user,id_home,id_task,"true");
        } else updateuserhome(id_user,id_home,id_task,"false");

    }



    public void updateuserhome( Long id_user,Long id_home, Long id_task, String isChecked){
        RequestQueue queue = Volley.newRequestQueue(List2taskActivity.this);
        String url = "http://192.168.79.172:8080/tareas/updateTareas";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Manejar el error
                error.printStackTrace();
                Toast.makeText(List2taskActivity.this, "Error al : " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Configura los parámetros de la solicitud
                Map<String, String> params = new HashMap<>();
                params.put("id_user", String.valueOf(id_user));
                params.put("id_home", String.valueOf(id_home));
                params.put("id_task", String.valueOf(id_task));
                params.put("terminado", String.valueOf(isChecked));
                return params;
            }
        };
        queue.add(stringRequest);


    }
    public void detelesuerhome(){
        RequestQueue queue = Volley.newRequestQueue(List2taskActivity.this);
        String url = "http://192.168.79.172:8080/home/delUserHome";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(List2taskActivity.this, response , Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(List2taskActivity.this,MenuHomeActivity.class);
                intent.putExtra("id",Math.toIntExact(id_user));
                startActivity(intent);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Manejar el error
                error.printStackTrace();
                Intent intent = new Intent(List2taskActivity.this,MenuHomeActivity.class);
                intent.putExtra("id",Math.toIntExact(id_user));
                startActivity(intent);
                //Toast.makeText(List2taskActivity.this, "Error al obtener datos: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Configura los parámetros de la solicitud
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(id_user));
                params.put("id_home", String.valueOf(id_home));
                return params;
            }
        };
        queue.add(stringRequest);


    }
    public void combineTasks() {
        List<CombinedTask> combinedTasks = new ArrayList<>();

        for (Task task : taskList) {
            for (TaskDTO taskDTO : taskListDTO) {
                if (task.getId() ==taskDTO.getId()) {
                    CombinedTask combinedTask = new CombinedTask((long) task.getId(), task.getDescription(), Boolean.parseBoolean(taskDTO.getTerminado()));
                    combinedTasks.add(combinedTask);
                    break;
                }
            }
        }

        // Utiliza la lista combinada en tu adaptador
        adapter = new CustomAdapter(List2taskActivity.this, combinedTasks);
        listView.setAdapter(adapter);
    }

    public void getListDTO() {
        RequestQueue queue = Volley.newRequestQueue(List2taskActivity.this);
        String url = "http://192.168.79.172:8080/tareas/getTareasDTO";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                taskListDTO = parseJsonToListDTO(response);
                combineTasks(); // Llama a combineTasks después de obtener la lista de TaskDTO
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(List2taskActivity.this, "Error al obtener datos: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_user", String.valueOf(id_user));
                params.put("id_home", String.valueOf(id_home));
                return params;
            }
        };
        queue.add(stringRequest);
    }
    public static List<TaskDTO> parseJsonToListDTO(String json) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<TaskDTO>>(){}.getType();
        return gson.fromJson(json, listType);
    }
    public void getList() {
        RequestQueue queue = Volley.newRequestQueue(List2taskActivity.this);
        String url = "http://192.168.79.172:8080/tareas/getTareas";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                taskList = parseJsonToList(response);
                getListDTO(); // Llama a getListDTO después de obtener la lista de tareas
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(List2taskActivity.this, "Error al obtener datos: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_user", String.valueOf(id_user));
                params.put("id_home", String.valueOf(id_home));
                return params;
            }
        };
        queue.add(stringRequest);
    }
    public static List<Task> parseJsonToList(String json) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Task>>(){}.getType();
        return gson.fromJson(json, listType);
    }
    public void getCode(){

        RequestQueue queue = Volley.newRequestQueue(List2taskActivity.this);
        String url = "http://192.168.79.172:8080/home/get/code";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(List2taskActivity.this, "response"+response, Toast.LENGTH_SHORT).show();
                textView.setText("El código de este hogar es "+response);
                housecode = response;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Manejar el error
                error.printStackTrace();
                Toast.makeText(List2taskActivity.this, "Error al obtener en código de la casa ", Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Configura los parámetros de la solicitud
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(id_home));
                //params.put("id_user", String.valueOf(id_user));
                return params;
            }
        };
        queue.add(stringRequest);
    }


    }
