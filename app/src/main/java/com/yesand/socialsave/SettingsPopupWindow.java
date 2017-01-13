package com.yesand.socialsave;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;

public class SettingsPopupWindow extends PopupWindow {

    //plsupdate

    public SettingsPopupWindow(final String prompt, final SettingsActivity context) {
        super();
        setFocusable(true);


        LinearLayout layout = new LinearLayout(context);
        layout.setBackgroundColor(Color.WHITE);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(5, 10, 5, 10);

        TextView textView = new TextView(context);
        textView.setTextSize(20);
        textView.setText(Html.fromHtml(prompt));
        layout.addView(textView);

        final EditText editText = new EditText(context);

        if (prompt.equals("Turn Notifications Off?")) {
            Switch switcheroo = new Switch(context);
            switcheroo.setGravity(Gravity.CENTER);
            switcheroo.setTextSize(30);
            switcheroo.setTextOff("OFF");
            switcheroo.setTextOn("ON");
            switcheroo.setShowText(true);
            Drawable thumb = context.getResources().getDrawable(R.drawable.thumb);
            Drawable track = context.getResources().getDrawable(R.drawable.track);
            switcheroo.setTrackDrawable(track);
            switcheroo.setThumbDrawable(thumb);
            switcheroo.setSwitchMinWidth(350);

            layout.addView(switcheroo);
        } else {
            editText.setTextSize(20);
            editText.setHint(prompt.equals("Do you really want to <b>PERMANENTLY</b> leave this group?") ? "Type \"yes\" to confirm" : "Type here");
            editText.setTextIsSelectable(true);
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            editText.setShowSoftInputOnFocus(true);

            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        im.toggleSoftInputFromWindow(editText.getApplicationWindowToken(), InputMethodManager.SHOW_IMPLICIT, 0);
                    }
                }
            });

            layout.addView(editText);
        }

        Button okButton = new Button(context);
        Button joinButton = new Button(context);
        Button createButton = new Button(context);
        if (prompt.equals("Do you really want to <b>PERMANENTLY</b> leave this group?")) {
            joinButton.setTextSize(20);
            joinButton.setText("Join another group");
            layout.addView(joinButton);

            createButton.setTextSize(20);
            createButton.setText("Create a group");
            layout.addView(createButton);
        } else {
            okButton.setTextSize(20);
            okButton.setText("Submit");
            layout.addView(okButton);
        }


        final PopupWindow popupWindow = this;

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = editText.getText().toString();
                switch (prompt) {
                    case "Enter a new name":
                        ResourceManager.getCurrUser().child("name").setValue(str);
                        break;
                    case "Enter your projected income for <b>next week</b>":
                        ResourceManager.getCurrUser().child("nextIncomePerWeek").setValue(str);
                        break;
                    case "Enter a goal for <b>next week</b>":
                        ResourceManager.getCurrUser().child("nextGoal").setValue(str);
                        break;
                    case "Turn Notifications Off?":
                        break;
                }
                popupWindow.dismiss();
                context.getUserData(context);
            }
        });

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().trim().equalsIgnoreCase("yes")) {
                    Intent intent = new Intent(context.getApplicationContext(), JoinGroupActvity.class);
                    context.startActivity(intent);
                    context.finish();
                }
                popupWindow.dismiss();
            }
        });
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().trim().equalsIgnoreCase("yes")) {
                    Intent intent = new Intent(context.getApplicationContext(), CreateAGroupActivity.class);
                    context.startActivity(intent);
                    context.finish();
                }
                popupWindow.dismiss();
            }
        });

        Button cancelButton = new Button(context);
        cancelButton.setTextSize(18);
        cancelButton.setText("Cancel");
        layout.addView(cancelButton);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        this.setContentView(layout);
    }
}
