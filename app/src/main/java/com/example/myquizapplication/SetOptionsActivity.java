package com.example.myquizapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myquizapplication.model.Card;
import com.example.myquizapplication.model.QuizDB;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SetOptionsActivity extends AppCompatActivity {
    final int[] currentCardNumber = {0};

    Button btnDeleteSet;
    Button btnEditSet;
    Button btnExit;
    Button btnTest;
    Button btnNext;
    Button btnPractice;
    Button btnPracticeDone;
    Button btnSubmit;

    EditText etEnterTerm;

    TextView tvCorrectTerm;
    TextView tvDefinition;
    TextView tvGivenDefinition;
    TextView tvResult;
    TextView tvScore;
    TextView tvSetTitle2;
    TextView tvTerm;
    List<Card> cardList;
    int setId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_options);

        btnDeleteSet = findViewById(R.id.btnDeleteSet);
        btnEditSet = findViewById(R.id.btnEditSet);
        btnPractice = findViewById(R.id.btnPractice);
        btnTest = findViewById(R.id.btnTest);

        tvSetTitle2 = findViewById(R.id.tvSetTitle2);

        Intent myIntent = getIntent();
        setId = myIntent.getIntExtra("setId", 0);
        String setTitle = myIntent.getStringExtra("setTitle");

        tvSetTitle2.setText(setTitle);

        QuizDB quizDB = new QuizDB(getApplicationContext());
        quizDB.open();
        cardList = quizDB.getSetById(setId);
        quizDB.close();

        btnPractice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PracticeTestActivity.class);
                intent.putExtra("setId", setId);
                intent.putExtra("setTitle", setTitle);
                intent.putExtra("mode", "practice");
                startActivity(intent);
            }
        });
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PracticeTestActivity.class);
                intent.putExtra("setId", setId);
                intent.putExtra("setTitle", setTitle);
                intent.putExtra("mode", "text");
                startActivity(intent);
            }
        });
        btnEditSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditSetActivity.class);
                intent.putExtra("setId", setId);
                intent.putExtra("setTitle", setTitle);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        btnDeleteSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(SetOptionsActivity.this);
                //final EditText edittext = new EditText(PracticeActivity.this);
                alert.setTitle("Are you sure you want to delete this set?");
                //alert.setView(edittext);
                alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        quizDB.open();
                        quizDB.deleteSet(setId);
                        quizDB.close();
                        finish();
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // do nothing
                    }
                });
                alert.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        QuizDB quizDB = new QuizDB(getApplicationContext());
        quizDB.open();
        cardList = quizDB.getSetById(setId);
        quizDB.close();
        currentCardNumber[0] = 0;
    }
}