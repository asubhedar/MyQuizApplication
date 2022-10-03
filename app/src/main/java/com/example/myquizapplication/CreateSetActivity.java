package com.example.myquizapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myquizapplication.model.Card;
import com.example.myquizapplication.model.QuizDB;

public class CreateSetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_set_activity);

        TextView tvSetTitle;
        EditText etTerm, etDefinition;
        Button btnSubmit, btnDone;

        tvSetTitle = findViewById(R.id.tvSetTitle);

        etTerm = findViewById(R.id.etTerm);
        etDefinition = findViewById(R.id.etDefinition);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnDone = findViewById(R.id.btnDone);

        Intent myIntent = getIntent();
        Integer setId = myIntent.getIntExtra("setId",0);
        String setTitle = myIntent.getStringExtra("setTitle");
        QuizDB quizDB = new QuizDB(this);
        tvSetTitle.setText(setTitle);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String term = etTerm.getText().toString();
                String definition = etDefinition.getText().toString();
                if (!term.isEmpty() && !definition.isEmpty()) {
                    Card card = new Card();
                    card.setSetId(setId);
                    card.setTerm(etTerm.getText().toString());
                    card.setDefinition(etDefinition.getText().toString());
                    quizDB.open();
                    int result = (int) quizDB.createEntry("CardsTable",card);
                    quizDB.close();
                    etTerm.setText("");
                    etDefinition.setText("");
                }
            }
        });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}