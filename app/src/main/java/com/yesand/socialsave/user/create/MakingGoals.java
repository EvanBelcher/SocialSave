package com.yesand.socialsave.user.create;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;

import com.yesand.socialsave.R;
import com.yesand.socialsave.ResourceManager;
import com.yesand.socialsave.group.create.CreateAGroupActivity;
import com.yesand.socialsave.group.join.JoinGroupActvity;

public class MakingGoals extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_making_goals);
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        final EditText amount = (EditText) findViewById(R.id.amount);
        final RadioButton isWeekly = (RadioButton) findViewById(R.id.weekIncome);
        final RadioButton isMonthly = (RadioButton) findViewById(R.id.monthIncome);
        final RadioButton isYearly = (RadioButton) findViewById(R.id.yearIncome);

        final SeekBar bar = (SeekBar) findViewById(R.id.seekBar2);

        Button createGroup = (Button) findViewById(R.id.createGroup);
        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateInfo(amount, isWeekly, isMonthly, isYearly, bar);
                Intent intent = new Intent(getApplicationContext(), CreateAGroupActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button joinGroup = (Button) findViewById(R.id.joinGroup);
        joinGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateInfo(amount, isWeekly, isMonthly, isYearly, bar);
                Intent intent = new Intent(getApplicationContext(), JoinGroupActvity.class);
                startActivity(intent);
                finish();
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

    private void updateInfo(EditText amount, RadioButton isWeekly, RadioButton isMonthly, RadioButton isYearly, SeekBar bar) {
        double income = 0.0;
        double amt = Double.parseDouble(amount.getText().toString());
        if (isWeekly.isChecked()) {
            income = amt;
        } else if (isMonthly.isChecked()) {
            income = amt * 12 / 52;
        } else if (isYearly.isChecked()) {
            income = amt / 52;
        }
        income = Math.round(income * 100.0) / 100.0; //rounds to two decimals
        ResourceManager.getCurrUser().child("incomePerWeek").setValue(income);

        double percentage = bar.getProgress() / 10.0;
        double goal = income * percentage;
        goal = Math.round(goal * 100.0) / 100.0;
        ResourceManager.getCurrUser().child("goal").setValue(goal);
    }
}
