package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class AddRemoveActivity extends AppCompatActivity {

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView listView;
    private Button add_button, save_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_remove);

        listView = findViewById(R.id.listView);
        add_button = findViewById(R.id.addButton);
        save_button = findViewById(R.id.saveButton);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem(view);
            }
        });

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveItems(true);
            }
        });

        items = new ArrayList<>();
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(itemsAdapter);
        loadItems();
        removeItem();
    }

    private void loadItems(){
        File fileEvents = new File(this.getFilesDir() + "/text/sample");
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileEvents));
            String line;
            while ((line = br.readLine()) != null) {
                itemsAdapter.add(line);
                text.append(line);
                text.append('\n');
            }
            br.close();
        } catch (IOException e) { }
    }

    private void removeItem() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Context context = getApplicationContext();
                Toast.makeText(context, "Item removed", Toast.LENGTH_LONG).show();
                items.remove(i);
                itemsAdapter.notifyDataSetChanged();
                saveItems(false);
                return false;
            }
        });
    }

    private void addItem(View view) {
        EditText input = findViewById(R.id.nameTextView);
        String itemText = input.getText().toString();

        if(!(itemText.equals(""))){
            itemsAdapter.add(itemText);
            input.setText("");
        }
        else{
            Toast.makeText(getApplicationContext(), "Please enter text", Toast.LENGTH_LONG).show();
        }
    }

    private void saveItems(boolean saveFromButton) {
        if(saveFromButton){
            Toast.makeText(this, "Saved", Toast.LENGTH_LONG).show();
        }

        File file = new File(this.getFilesDir(), "text");

        if (!items.isEmpty()) {

            if (!file.exists()) {
                file.mkdir();
            }
            try {
                File gpxfile = new File(file, "sample");
                FileWriter writer = new FileWriter(gpxfile);
                for(int i=0; i<items.size(); i++){
                    writer.append(items.get(i));
                    writer.append('\n');
                    writer.flush();
                }
                writer.close();
            } catch (Exception e) { }
        }
        else if(items.isEmpty() && file.exists()){
            try {
                File gpxfile = new File(file, "sample");
                FileWriter writer = new FileWriter(gpxfile);
                writer.close();
            } catch (Exception e) { }
        }
    }
}