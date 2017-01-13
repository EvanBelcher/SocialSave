package com.yesand.socialsave;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MakingGoals extends AppCompatActivity {

    private final String INCOME_KEY = "incomePerWeek";
    private final String GOAL_KEY = "goal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_making_goals);

        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
       // final EditText amount = (EditText)findViewById(R.id.amount);
        final RadioButton isWeekly = (RadioButton)findViewById(R.id.weekIncome);
        final RadioButton isMonthly = (RadioButton)findViewById(R.id.monthIncome);
        final RadioButton isYearly = (RadioButton)findViewById(R.id.yearIncome);

        final SeekBar bar = (SeekBar)findViewById(R.id.seekBar2);

//        Button createGroup = (Button)findViewById(R.id.createGroup);
//        createGroup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                updateInfo(amount, isWeekly, isMonthly, isYearly, bar);
//            }
//        });
//
//        Button joinGroup = (Button)findViewById(R.id.joinGroup);
//        joinGroup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                updateInfo(amount, isWeekly, isMonthly, isYearly, bar);
//            }
//        });
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//    }

    private void updateInfo(EditText amount, RadioButton isWeekly, RadioButton isMonthly, RadioButton isYearly, SeekBar bar)
    {
        double income = 0.0;
        double amt = Double.parseDouble(amount.getText().toString());
        if (isWeekly.isChecked()) {
            income = amt;
        }
        else if (isMonthly.isChecked()) {
            income = amt * 12 / 52;
        }
        else if (isYearly.isChecked()) {
            income = amt / 52;
        }
        income = Math.round(income * 100.0) / 100.0; //rounds to two decimals
        ResourceManager.getCurrUser().child(INCOME_KEY).setValue(income);

        double percentage = bar.getProgress() / 10.0;
        double goal = income * percentage;
        goal = Math.round(goal * 100.0) / 100.0;
        ResourceManager.getCurrUser().child(GOAL_KEY).setValue(goal);
    }
}
