package com.meomoc.toolbar_optionmenu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ViewSelectedEntry extends AppCompatActivity {

    TextView entryTitle, entryDate, entryContent;
    String title, date, content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_selected_entry);

        entryTitle = findViewById(R.id.entryTitle);
        entryDate = findViewById(R.id.entryDate);
        entryContent = findViewById(R.id.entryContent);


        Intent intent = getIntent();
        title = intent.getStringExtra("Title");
        date = intent.getStringExtra("Date");
        content = intent.getStringExtra("Content");

        entryTitle.setText(title);
        entryDate.setText(date);
        entryContent.setText(content);

    }
}
