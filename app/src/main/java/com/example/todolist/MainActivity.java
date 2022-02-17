package com.example.todolist;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
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

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView listView;
    private Button add_button, save_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        add_button = findViewById(R.id.add_button);
        save_button = findViewById(R.id.save_button);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem(view);
            }
        });

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { saveItems(); }
        });

        items = new ArrayList<>();
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(itemsAdapter);
        setUpListViewListener();
        loadItems();
    }

    private void loadItems(){
        File fileEvents = new File(MainActivity.this.getFilesDir() + "/text/sample");
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

    private void setUpListViewListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Context context = getApplicationContext();
                Toast.makeText(context, "Item removed", Toast.LENGTH_LONG).show();
                items.remove(i);
                itemsAdapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    private void addItem(View view) {
        EditText input = findViewById(R.id.editTextTextPersonName);
        String itemText = input.getText().toString();

        if(!(itemText.equals(""))){
            itemsAdapter.add(itemText);
            input.setText("");
        }
        else{
            Toast.makeText(getApplicationContext(), "Please enter text", Toast.LENGTH_LONG).show();
        }
    }

    private void saveItems() {
        if (!items.isEmpty()) {
            File file = new File(MainActivity.this.getFilesDir(), "text");
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
                Toast.makeText(MainActivity.this, "Saved your text", Toast.LENGTH_LONG).show();
            } catch (Exception e) { }
        }
    }
}