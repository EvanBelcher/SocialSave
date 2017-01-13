package com.yesand.socialsave;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // get the listview
        ListView listView = (ListView) findViewById(R.id.list_view_thing);

        String[] arr = {"Name: Evan Belcher", "Email: evanbelcher3@gmail.com", "Password", "This Week's Income: $10,000", "Next Week's Income: $10,000", "This Week's Goal: $8,000", "Next Week's Goal: $8,000", "Group Id: abcdef", "Notifications", "Leave Group"};//TODO

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.list_item, arr);

        listView.setAdapter(adapter);

        final Activity context = this;

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppCompatTextView textView = (AppCompatTextView) view;

                if (textView.getText().toString().contains("Name: ")) {
                    SettingsPopupWindow popupWindow = new SettingsPopupWindow("Enter a new name", context);
                    popupWindow.showAtLocation(parent, Gravity.TOP, 0, 0);
                    popupWindow.update(0, 250, parent.getWidth() - 150, parent.getHeight() / 2);
                    view.clearFocus();
                    parent.clearFocus();
                } else if (textView.getText().toString().equals("Password")) {
                } else if (textView.getText().toString().contains("Next Week's Income")) {
                    PopupWindow popupWindow = new SettingsPopupWindow("Enter your projected income for <b>next week</b>", context);
                    popupWindow.showAtLocation(parent, Gravity.TOP, 0, 0);
                    popupWindow.update(0, 250, parent.getWidth() - 150, parent.getHeight() / 2);
                } else if (textView.getText().toString().contains("Next Week's Goal")) {
                    PopupWindow popupWindow = new SettingsPopupWindow("Enter a goal for <b>next week</b>", context);
                    popupWindow.showAtLocation(parent, Gravity.TOP, 0, 0);
                    popupWindow.update(0, 250, parent.getWidth() - 150, parent.getHeight() / 2);
                } else if (textView.getText().toString().contains("Notifications")) {
                    PopupWindow popupWindow = new SettingsPopupWindow("Turn Notifications Off?", context);
                    popupWindow.showAtLocation(parent, Gravity.TOP, 0, 0);
                    popupWindow.update(0, 250, parent.getWidth() - 150, parent.getHeight() / 2);
                } else if (textView.getText().toString().equals("Leave Group")) {
                    PopupWindow popupWindow = new SettingsPopupWindow("Do you really want to <b>PERMANENTLY</b> leave this group?", context);
                    popupWindow.showAtLocation(parent, Gravity.TOP, 0, 0);
                    popupWindow.update(0, 250, parent.getWidth() - 150, parent.getHeight() / 2);
                }
            }
        });

    }

}
