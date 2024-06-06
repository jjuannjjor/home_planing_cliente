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
import android.widget.Button;
import android.widget.TextView;
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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.home_planing.R;

import java.util.HashMap;
import java.util.Map;

public class NewHomeActivity extends AppCompatActivity {

    TextView tv_nombre, tv_code;
    Button crear_home_btn, cancelar;
    private static final String CHANNEL_ID = "example_channel_id";
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tv_code = findViewById(R.id.code);
        tv_nombre = findViewById(R.id.name_home);
        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);
        cancelar = findViewById(R.id.cancelar);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        crear_home_btn = findViewById(R.id.btn_new_home);
        crear_home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processFormFields();
            }
        });


    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Example Channel";
            String description = "This is an example channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void showNotification(String title, String message) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_menu_share)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(0, builder.build());
    }

    public void processFormFields(){
        // Valida el campo nombre
        if (!validateNombre()) return;

        // Inicializa la cola de solicitudes de Volley
        RequestQueue queue = Volley.newRequestQueue(NewHomeActivity.this);
        String url = "http://192.168.79.172:8080/home/register";

        // Crea una solicitud StringRequest
        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Limpia el campo nombre
                tv_nombre.setText(null);

                // Maneja la respuesta del servidor
                //if(response.equalsIgnoreCase("success")){
                    //Log.d("Response", response);

                    //String housecode = response;
                    //Toast.makeText(NewHomeActivity.this, "Respuest:"+response, Toast.LENGTH_SHORT).show();
                    //
                //Toast.makeText(NewHomeActivity.this, Build.VERSION.SDK_INT +">="+ Build.VERSION_CODES.O, Toast.LENGTH_SHORT).show();

                    //showNotification("Example Title", "This is an example notification message.");
                    //Cambiamos la activity y la pasamos el dato
                    Intent goToProfile = new Intent(NewHomeActivity.this, MenuHomeActivity.class);
                    goToProfile.putExtra("id", id);
                    goToProfile.putExtra("housecode", response);
                    startActivity(goToProfile);

                //}

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) { // Maneja el error
                error.printStackTrace();
                System.out.println(error.getMessage());
                Toast.makeText(NewHomeActivity.this, "Error al crear hogar", Toast.LENGTH_SHORT).show();

            }
        }){
            @Nullable
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                // Crea un mapa de parámetros para enviar al servidor
                Map<String,String> params = new HashMap<>();
                params.put("nombre", tv_nombre.getText().toString());
                return params;
            }
        };
        queue.add(stringRequest);

    }

    public boolean validateNombre(){ // Valida el campo nombre
        String nombre = tv_nombre.getText().toString();
        if(nombre.isEmpty()){
            tv_nombre.setError("El nombre es obligatorio");
            return false;
        }else {
            tv_nombre.setError(null);
            return true;
        }
    }
}