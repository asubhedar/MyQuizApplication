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

import java.util.List;

public class EditSetActivity extends AppCompatActivity {
    final int[] currentCardNumber = {0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_set_activity);

        TextView tvSetTitle;
        EditText etTerm, etDefinition;
        Button btnSubmit, btnDone, btnNextCard;

        tvSetTitle = findViewById(R.id.tvSetTitle);

        etTerm = findViewById(R.id.etTerm);
        etDefinition = findViewById(R.id.etDefinition);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnDone = findViewById(R.id.btnDone);
        btnNextCard = findViewById(R.id.btnNextCard);

        Intent myIntent = getIntent();
        int setId = myIntent.getIntExtra("setId", 0);
        String setTitle = myIntent.getStringExtra("setTitle");
        QuizDB quizDB = new QuizDB(this);
        quizDB.open();
        List<Card> cardList = quizDB.getSetById(setId);
        if (cardList.size() == 0) {
            finish();
            return;
        }
        quizDB.close();
        tvSetTitle.setText(setTitle);
        Card card = cardList.get(0);
        etTerm.setText(card.getTerm());
        etDefinition.setText(card.getDefinition());

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Card newCard = cardList.get(currentCardNumber[0]);
                String term = etTerm.getText().toString();
                String definition = etDefinition.getText().toString();
                if (!term.isEmpty() && !definition.isEmpty()) {
                    newCard.setTerm(etTerm.getText().toString());
                    newCard.setDefinition(etDefinition.getText().toString());
                    quizDB.open();
                    int result = (int) quizDB.updateEntry("CardsTable", newCard);
                    quizDB.close();
                }
            }
        });
        btnNextCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentCardNumber[0]++;
                if (currentCardNumber[0] < cardList.size()) {
                    Card card = cardList.get(currentCardNumber[0]);
                    etTerm.setText(card.getTerm());
                    etDefinition.setText(card.getDefinition());
                } else {
                    finish();
/*
                    btnExit.setVisibility(View.GONE);
                    btnNext.setVisibility(View.GONE);
                    btnSubmit.setVisibility(View.GONE);

                    etTerm.setVisibility(View.GONE);

                    tvCorrectTerm.setVisibility(View.GONE);
                    tvDefinition.setVisibility(View.GONE);
                    tvGivenDefinition.setVisibility(View.GONE);
                    tvResult.setVisibility(View.GONE);
                    tvTerm.setVisibility(View.GONE);

                    tvScore.setText("Score: " + score[0] + "/" + numCards);
                    tvScore.setVisibility(View.VISIBLE);

                    btnPracticeDone.setVisibility(View.VISIBLE);
                    btnPracticeDone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
*/
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