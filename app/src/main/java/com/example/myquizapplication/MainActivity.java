package com.example.myquizapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myquizapplication.model.QuizDB;
import com.example.myquizapplication.model.Set;

import java.io.Serializable;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    LinearLayout llExistingSets;
    QuizDB quizDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        quizDB = new QuizDB(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        llExistingSets = findViewById(R.id.llExistingSets);
        Button btnNewSet = findViewById(R.id.btnNewCard);
        btnNewSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                final EditText edittext = new EditText(MainActivity.this);
                alert.setTitle("Enter Set Title");
                alert.setView(edittext);
                alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String setTitle = edittext.getText().toString();
                        quizDB.open();
                        Integer setId = (int) quizDB.createEntry("SetsTable", setTitle);
                        quizDB.close();
                        dialog.dismiss();

                        Intent intent = new Intent(getApplicationContext(), CreateSetActivity.class);
                        intent.putExtra("setId", setId);
                        intent.putExtra("setTitle", setTitle);
                        startActivity(intent);
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
        llExistingSets.removeAllViews();
        //llExistingSets.setLayoutParams(new LinearLayout.LayoutParams(new ViewGroup.MarginLayoutParams(100, 100)));

        quizDB.open();
        List<Set> setList = quizDB.getSetList();
        for(Set set:setList)
        {
            Button btn = new Button(this);
            btn.setText(set.getTitle());
            btn.setBackgroundColor(getColor(R.color.secondaryLightColor));
            btn.setTextColor(getColor(R.color.secondaryTextColor));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,0,0,20);
            params.gravity= Gravity.CENTER_HORIZONTAL;
            btn.setPadding(20,0,20,0);
            btn.setLayoutParams(params);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), PracticeActivity.class);
                    intent.putExtra("setId", set.getId());
                    intent.putExtra("setTitle", set.getTitle());
                    startActivity(intent);
                }
            });
            llExistingSets.addView(btn);
        }
        quizDB.close();
    }
}