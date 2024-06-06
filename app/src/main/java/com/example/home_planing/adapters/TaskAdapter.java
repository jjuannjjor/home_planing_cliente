package com.example.home_planing.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.home_planing.entities.CombinedTask;
import com.example.home_planing.entities.Home;
import com.example.home_planing.entities.Task;

import java.util.List;

public class TaskAdapter extends ArrayAdapter<CombinedTask> {
    private List<CombinedTask> combinedTasks;
    private Context context;

    public TaskAdapter(Context context, List<CombinedTask> combinedTasks) {
        super(context, 0, combinedTasks);
        this.context = context;
        this.combinedTasks = combinedTasks;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CombinedTask combinedTask = combinedTasks.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        String taskDescription = combinedTask.getDescripcion();
        String status = combinedTask.isTerminado() ? "Hecho" : "Pendiente";
        String displayText = taskDescription + " - " + status;
        textView.setText(displayText);

        return convertView;
    }
}
