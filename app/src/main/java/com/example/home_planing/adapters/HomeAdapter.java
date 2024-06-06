package com.example.home_planing.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.home_planing.entities.Home;

import java.util.List;

public class HomeAdapter extends ArrayAdapter<Home> {
    private List<Home> casas;
    private Context context;

    public HomeAdapter(Context context, List<Home> casas) {
        super(context, 0, casas);
        this.context = context;
        this.casas = casas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Home casa = casas.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(casa.getNombre()); // Suponiendo que "nombre" es un campo de Casa que deseas mostrar

        return convertView;
    }
}
