package com.example.myquizapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myquizapplication.model.Card;
import com.example.myquizapplication.model.QuizDB;

import java.util.ArrayList;
import java.util.List;

public class PracticeActivity extends AppCompatActivity {
    final int[] currentCardNumber = {0};

    Button btnDeleteSet;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.practice_activity);

        btnDeleteSet = findViewById(R.id.btnDeleteSet);
        btnPractice = findViewById(R.id.btnPractice);
        btnTest = findViewById(R.id.btnTest);

        tvSetTitle2 = findViewById(R.id.tvSetTitle2);

        Intent myIntent = getIntent();
        Integer setId = myIntent.getIntExtra("setId", 0);
        String setTitle = myIntent.getStringExtra("setTitle");

        tvSetTitle2.setText(setTitle);

        QuizDB quizDB = new QuizDB(getApplicationContext());
        quizDB.open();
        List<Card> cardList = quizDB.getSetById(setId);
        quizDB.close();

        btnPractice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                managePractice(cardList, currentCardNumber);
            }
        });
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manageTest(cardList, currentCardNumber);
            }
        });
        btnDeleteSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quizDB.open();
                quizDB.deleteSet(setId);
                quizDB.close();
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentCardNumber[0] = 0;
    }

    private void managePractice(List<Card> cardList, int[] currentCardNumber) {
        int numCards = cardList.size();
        final boolean[] isAllcorrect = {true};
        /*final int[] score = {0};*/
        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = vi.inflate(R.layout.enter_term_view, null);

        // insert into main view
        ViewGroup insertPoint = (ViewGroup) findViewById(R.id.practiceLayout);
        insertPoint.addView(view, 1, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        btnNext = findViewById(R.id.btnNext);
        btnPracticeDone = findViewById(R.id.btnPracticeDone);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnExit = findViewById(R.id.btnExit);

        etEnterTerm = findViewById(R.id.etEnterTerm);

        tvCorrectTerm = findViewById(R.id.tvCorrectTerm);
        tvDefinition = findViewById(R.id.tvDefinition);
        tvGivenDefinition = findViewById(R.id.tvGivenDefinition);
        tvResult = findViewById(R.id.tvResult);
        tvScore = findViewById(R.id.tvScore);
        tvTerm = findViewById(R.id.tvTerm);

        tvScore.setVisibility(View.GONE);
        btnPracticeDone.setVisibility(View.GONE);

        tvGivenDefinition.setText(cardList.get(currentCardNumber[0]).getDefinition());
        tvResult.setVisibility(View.GONE);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAllcorrect[0] && !cardList.get(currentCardNumber[0]).isCorrectAnswerFlag())
                    isAllcorrect[0] = false;
                etEnterTerm.setEnabled(true);
                btnSubmit.setEnabled(true);
                currentCardNumber[0]++;
                if (currentCardNumber[0] < cardList.size()) {
                    etEnterTerm.setText("");
                    tvCorrectTerm.setVisibility(View.GONE);
                    tvResult.setVisibility(View.GONE);
                    tvGivenDefinition.setText(cardList.get(currentCardNumber[0]).getDefinition());
                } else {
                    if (isAllcorrect[0] == true) {
                        btnExit.setVisibility(View.GONE);
                        btnNext.setVisibility(View.GONE);
                        btnSubmit.setVisibility(View.GONE);

                        etEnterTerm.setVisibility(View.GONE);

                        tvCorrectTerm.setVisibility(View.GONE);
                        tvDefinition.setVisibility(View.GONE);
                        tvGivenDefinition.setVisibility(View.GONE);
                        tvResult.setVisibility(View.GONE);
                        tvTerm.setVisibility(View.GONE);

                        /*tvScore.setText("Score: " + score[0] + "/" + numCards);
                        tvScore.setVisibility(View.VISIBLE);*/

                        btnPracticeDone.setVisibility(View.VISIBLE);
                        btnPracticeDone.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finish();
                            }
                        });
                    } else {
                        List<Card> tempList = new ArrayList<>();
                        for (Card card : cardList) {
                            if (!card.isCorrectAnswerFlag()) {
                                tempList.add(card);
                            }
                        }
                        cardList.clear();
                        for (Card card : tempList) {
                            cardList.add(card);
                        }
                        currentCardNumber[0] = 0;
                        managePractice(cardList, currentCardNumber);
                    }
                }
            }
        });
         btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etEnterTerm.setEnabled(false);
                btnSubmit.setEnabled(false);
                String answerTerm = etEnterTerm.getText().toString();
                String correctTerm = cardList.get(currentCardNumber[0]).getTerm();
                if (answerTerm.equals(correctTerm)) {
                    cardList.get(currentCardNumber[0]).setCorrectAnswerFlag(true);
                    tvCorrectTerm.setVisibility(View.GONE);
                    tvResult.setText("CORRECT ANSWER!");
                    /*score[0]++;*/
                } else {
                    isAllcorrect[0] = false;
                    tvResult.setText("WRONG ANSWER!");
                    tvCorrectTerm.setText("Correct answer is: " + correctTerm);
                    tvCorrectTerm.setVisibility(View.VISIBLE);
                }
                tvResult.setVisibility(View.VISIBLE);
            }
        });

        etEnterTerm.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    btnSubmit.performClick();
                    return true;
                }
                return false;
            }
        });


    }

    private void manageTest(List<Card> cardList, int[] currentCardNumber) {
        int numCards = cardList.size();
        final int[] score = {0};
        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = vi.inflate(R.layout.enter_term_view, null);

        // insert into main view
        ViewGroup insertPoint = (ViewGroup) findViewById(R.id.practiceLayout);
        insertPoint.addView(view, 1, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        btnExit = findViewById(R.id.btnExit);
        btnNext = findViewById(R.id.btnNext);
        btnPracticeDone = findViewById(R.id.btnPracticeDone);
        btnSubmit = findViewById(R.id.btnSubmit);

        etEnterTerm = findViewById(R.id.etEnterTerm);

        tvCorrectTerm = findViewById(R.id.tvCorrectTerm);
        tvDefinition = findViewById(R.id.tvDefinition);
        tvGivenDefinition = findViewById(R.id.tvGivenDefinition);
        tvResult = findViewById(R.id.tvResult);
        tvScore = findViewById(R.id.tvScore);
        tvTerm = findViewById(R.id.tvTerm);

        tvScore.setVisibility(View.GONE);
        btnPracticeDone.setVisibility(View.GONE);

        tvGivenDefinition.setText(cardList.get(currentCardNumber[0]).getDefinition());
        tvResult.setVisibility(View.GONE);

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etEnterTerm.setEnabled(true);
                btnSubmit.setEnabled(true);

                currentCardNumber[0]++;
                if (currentCardNumber[0] < cardList.size()) {
                    etEnterTerm.setText("");
                    tvCorrectTerm.setVisibility(View.GONE);
                    tvResult.setVisibility(View.GONE);
                    tvGivenDefinition.setText(cardList.get(currentCardNumber[0]).getDefinition());
                } else {
                    btnExit.setVisibility(View.GONE);
                    btnNext.setVisibility(View.GONE);
                    btnSubmit.setVisibility(View.GONE);

                    etEnterTerm.setVisibility(View.GONE);

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
                }
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etEnterTerm.setEnabled(false);
                btnSubmit.setEnabled(false);
                String answerTerm = etEnterTerm.getText().toString();
                String correctTerm = cardList.get(currentCardNumber[0]).getTerm();
                if (answerTerm.equals(correctTerm)) {
                    tvCorrectTerm.setVisibility(View.GONE);
                    tvResult.setText("CORRECT ANSWER!");
                    cardList.get(currentCardNumber[0]).setCorrectAnswerFlag(true);
                    score[0]++;
                } else {
                    tvResult.setText("WRONG ANSWER!");
                    tvCorrectTerm.setText("Correct answer is: " + correctTerm);
                    tvCorrectTerm.setVisibility(View.VISIBLE);
                }
                tvResult.setVisibility(View.VISIBLE);
            }
        });

        etEnterTerm.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    btnSubmit.performClick();
                    return true;
                }
                return false;
            }
        });
    }
}