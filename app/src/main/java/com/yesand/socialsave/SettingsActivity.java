package com.yesand.socialsave;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        getUserData(this);

    }

    public void getUserData(final SettingsActivity settingsActivity) {
        // {"Name: Evan Belcher", "Email: evanbelcher3@gmail.com", "Password", "This Week's Income: $10,000", "Next Week's Income: $10,000", "This Week's Goal: $8,000", "Next Week's Goal: $8,000", "Group Id: abcdef", "Notifications", "Leave Group"};

        ResourceManager.getCurrUser().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String email = ResourceManager.getCurrUser().getKey().replace('_', '@').replace('-', '.');
                //pass
                String incomePerWeek = dataSnapshot.child("incomePerWeek").getValue().toString();
                String nextIncomePerWeek = dataSnapshot.child("nextIncomePerWeek").getValue().toString();
                String goal = dataSnapshot.child("goal").getValue().toString();
                String nextGoal = dataSnapshot.child("nextGoal").getValue().toString();
                String groupId = dataSnapshot.child("groupId").getValue().toString().substring(1);

                NumberFormat df = DecimalFormat.getCurrencyInstance();

//                linkedList.add("Name: " + name);
//                linkedList.add("Email: " + email);
//                linkedList.add("Password");
//                linkedList.add("This Week's Income: " + df.format(Double.parseDouble(incomePerWeek)));
//                linkedList.add("Next Week's Income: " + df.format(Double.parseDouble(nextIncomePerWeek)));
//                linkedList.add("This Week's Goal: " + df.format(Double.parseDouble(goal)));
//                linkedList.add("Next Week's Goal: " + df.format(Double.parseDouble(nextGoal)));
//                linkedList.add("Group Id: " + groupId);
//                linkedList.add("Leave Group");

                String[] arr = new String[]{"Name: " + name, "Email: " + email, "Password", "This Week's Income: " + df.format(Double.parseDouble(incomePerWeek)), "Next Week's Income: " + df.format(Double.parseDouble(nextIncomePerWeek)), "This Week's Goal: " + df.format(Double.parseDouble(goal)), "Next Week's Goal: " + df.format(Double.parseDouble(nextGoal)), "Group Id: " + groupId, "Leave Group"};
                ArrayAdapter adapter = new ArrayAdapter<>(settingsActivity,
                        R.layout.list_item, arr);

                // get the listview
                ListView listView = (ListView) findViewById(R.id.list_view_thing);

                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        AppCompatTextView textView = (AppCompatTextView) view;

                        if (textView.getText().toString().contains("Name: ")) {
                            SettingsPopupWindow popupWindow = new SettingsPopupWindow("Enter a new name", settingsActivity);
                            popupWindow.showAtLocation(parent, Gravity.TOP, 0, 0);
                            popupWindow.update(0, 0, parent.getWidth(), parent.getHeight());
                            view.clearFocus();
                            parent.clearFocus();
                        } else if (textView.getText().toString().equals("Password")) {
                            //TODO
                        } else if (textView.getText().toString().contains("Next Week's Income")) {
                            PopupWindow popupWindow = new SettingsPopupWindow("Enter your projected income for <b>next week</b>", settingsActivity);
                            popupWindow.showAtLocation(parent, Gravity.TOP, 0, 0);
                            popupWindow.update(0, 0, parent.getWidth(), parent.getHeight());
                        } else if (textView.getText().toString().contains("Next Week's Goal")) {
                            PopupWindow popupWindow = new SettingsPopupWindow("Enter a goal for <b>next week</b>", settingsActivity);
                            popupWindow.showAtLocation(parent, Gravity.TOP, 0, 0);
                            popupWindow.update(0, 0, parent.getWidth(), parent.getHeight());
                        } else if (textView.getText().toString().contains("Notifications")) {
                            PopupWindow popupWindow = new SettingsPopupWindow("Turn Notifications Off?", settingsActivity);
                            popupWindow.showAtLocation(parent, Gravity.TOP, 0, 0);
                            popupWindow.update(0, 0, parent.getWidth(), parent.getHeight());
                        } else if (textView.getText().toString().equals("Leave Group")) {
                            PopupWindow popupWindow = new SettingsPopupWindow("Do you really want to <b>PERMANENTLY</b> leave this group?", settingsActivity);
                            popupWindow.showAtLocation(parent, Gravity.TOP, 0, 0);
                            popupWindow.update(0, 0, parent.getWidth(), parent.getHeight());
                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}