package com.yesand.socialsave;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

public class MakingGoals extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_making_goals);

        EditText amount = (EditText)findViewById(R.id.amount);
        RadioButton isWeekly = (RadioButton)findViewById(R.id.weekIncome);
        RadioButton isMonthly = (RadioButton)findViewById(R.id.monthIncome);
        RadioButton isYearly = (RadioButton)findViewById(R.id.yearIncome);

        Button createGroup = (Button)findViewById(R.id.createGroup);
        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateInfo();
            }
        });

        Button joinGroup = (Button)findViewById(R.id.joinGroup);
        joinGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateInfo();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void updateInfo()
    {

    }
}
