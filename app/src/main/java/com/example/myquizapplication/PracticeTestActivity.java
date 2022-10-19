package com.example.myquizapplication;

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

import java.util.ArrayList;
import java.util.List;

public class PracticeTestActivity extends AppCompatActivity {
    final int[] currentCardNumber = {0};

    Button btnExit;
    Button btnNext;
    Button btnPracticeDone;
    Button btnSubmit;
    TextView tvSetTitle;
    EditText etTerm, etDefinition;
    Button btnDone;
    EditText etEnterTerm;

    TextView tvCorrectTerm;
    TextView tvDefinition;
    TextView tvGivenDefinition;
    TextView tvResult;
    TextView tvScore;
    TextView tvTerm;
    boolean isPracticeMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_term_view);

        tvSetTitle = findViewById(R.id.tvSetTitle3);

        etTerm = findViewById(R.id.etTerm);
        etDefinition = findViewById(R.id.etDefinition);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnDone = findViewById(R.id.btnPracticeDone);

        Intent myIntent = getIntent();
        int setId = myIntent.getIntExtra("setId", 0);
        String setTitle = myIntent.getStringExtra("setTitle");
        isPracticeMode = myIntent.getStringExtra("mode").equals("practice");
        QuizDB quizDB = new QuizDB(this);
        quizDB.open();
        List<Card> cardList = quizDB.getSetById(setId);
        if (cardList.size() == 0) {
            finish();
            return;
        }
        quizDB.close();

        tvSetTitle.setText(setTitle);
        if (isPracticeMode) {
            managePractice(cardList, currentCardNumber);
        } else {
            manageTest(cardList, currentCardNumber);
        }
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
        btnDone.setOnClickListener(new View.OnClickListener() {
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
    }

    private void managePractice(List<Card> cardList, int[] currentCardNumber) {
        if (cardList.size() == 0)
            return;
        final boolean[] isAllCorrect = {true};

        findViewsById();

        tvScore.setVisibility(View.GONE);
        btnPracticeDone.setVisibility(View.GONE);

        tvGivenDefinition.setText(cardList.get(currentCardNumber[0]).getDefinition());
        etEnterTerm.setText("");

        tvCorrectTerm.setVisibility(View.GONE);
        tvResult.setVisibility(View.GONE);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAllCorrect[0] && !cardList.get(currentCardNumber[0]).isCorrectAnswerFlag())
                    isAllCorrect[0] = false;
                etEnterTerm.setEnabled(true);
                btnSubmit.setEnabled(true);
                currentCardNumber[0]++;
                if (currentCardNumber[0] < cardList.size()) {
                    etEnterTerm.setText("");
                    tvCorrectTerm.setVisibility(View.GONE);
                    tvResult.setVisibility(View.GONE);
                    tvGivenDefinition.setText(cardList.get(currentCardNumber[0]).getDefinition());
                } else {
                    if (isAllCorrect[0]) {
                        hideViews();

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
                        cardList.addAll(tempList);
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
                    tvResult.setText(R.string.correct_answer);
                } else {
                    isAllCorrect[0] = false;
                    tvResult.setText(R.string.wrong_answer);
                    tvCorrectTerm.setText(getString(R.string.correct_answer_is) + correctTerm);
                    tvCorrectTerm.setVisibility(View.VISIBLE);
                }
                tvResult.setVisibility(View.VISIBLE);
            }
        });
    }

    private void manageTest(List<Card> cardList, int[] currentCardNumber) {
        int numCards = cardList.size();
        if (numCards == 0)
            return;
        final int[] score = {0};
        findViewsById();

        tvScore.setVisibility(View.GONE);
        btnPracticeDone.setVisibility(View.GONE);

        tvGivenDefinition.setText(cardList.get(currentCardNumber[0]).getDefinition());
        tvResult.setVisibility(View.GONE);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentCardNumber[0]++;
                if (currentCardNumber[0] < cardList.size()) {
                    etEnterTerm.setEnabled(true);
                    btnSubmit.setEnabled(true);
                    etEnterTerm.setText("");
                    tvCorrectTerm.setVisibility(View.GONE);
                    tvResult.setVisibility(View.GONE);
                    tvGivenDefinition.setText(cardList.get(currentCardNumber[0]).getDefinition());
                } else {
                    hideViews();

                    tvScore.setText(getString(R.string.score) + score[0] + "/" + numCards);
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
                    tvResult.setText(R.string.correct_answer);
                    cardList.get(currentCardNumber[0]).setCorrectAnswerFlag(true);
                    score[0]++;
                } else {
                    tvResult.setText(R.string.wrong_answer);
                    tvCorrectTerm.setText(getString(R.string.correct_answer_is) + correctTerm);
                    tvCorrectTerm.setVisibility(View.VISIBLE);
                }
                tvResult.setVisibility(View.VISIBLE);
            }
        });
    }

    private void hideViews() {
        btnExit.setVisibility(View.GONE);
        btnNext.setVisibility(View.GONE);
        btnSubmit.setVisibility(View.GONE);

        etEnterTerm.setVisibility(View.GONE);

        tvCorrectTerm.setVisibility(View.GONE);
        tvDefinition.setVisibility(View.GONE);
        tvGivenDefinition.setVisibility(View.GONE);
        tvResult.setVisibility(View.GONE);
        tvTerm.setVisibility(View.GONE);
    }

    private void findViewsById() {
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
    }
}