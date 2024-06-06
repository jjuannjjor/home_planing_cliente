package com.example.home_planing.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.home_planing.R;
import com.example.home_planing.activities.List2taskActivity;
import com.example.home_planing.entities.CombinedTask;
import com.example.home_planing.entities.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomAdapter extends ArrayAdapter<CombinedTask> {
    private List<Boolean> checkedList;
    private Context context;

    public CustomAdapter(Context context, List<CombinedTask> items) {
        super(context, 0, items);
        this.context = context;
        checkedList = new ArrayList<>(Collections.nCopies(items.size(), false));
        for (int i = 0; i < items.size(); i++) {
            checkedList.set(i, items.get(i).isTerminado());
        }
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_layout, parent, false);
        }

        CheckBox checkBox = listItemView.findViewById(R.id.checkbox);
        checkBox.setOnCheckedChangeListener(null); // Prevent recursive calls

        checkBox.setChecked(checkedList.get(position));

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkedList.set(position, isChecked);
                getItem(position).setTerminado(isChecked);
                List2taskActivity list2taskActivity = (List2taskActivity) context;
                list2taskActivity.updateuserhometask(getItem(position).getId(),isChecked);
            }
        });

        TextView textViewId = listItemView.findViewById(R.id.textViewId);
        TextView textViewDescription = listItemView.findViewById(R.id.textViewDescription);

        CombinedTask currentItem = getItem(position);
        if (currentItem != null) {
            textViewId.setText(String.valueOf(currentItem.getId()));
            textViewDescription.setText(currentItem.getDescripcion());
        }

        return listItemView;
    }

    // Method to get the list of checked items
    public List<Boolean> getCheckedList() {
        return checkedList;
    }
}
