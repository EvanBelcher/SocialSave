package com.yesand.socialsave.group.create;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yesand.socialsave.MainActivity;
import com.yesand.socialsave.R;
import com.yesand.socialsave.ResourceManager;

public class CreateAGroupActivity extends AppCompatActivity {

    private DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    private final String GROUP_KEY = "groupId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_a_group);
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        final EditText group = (EditText) findViewById(R.id.createGroup_groupName);
        final Button create = (Button) findViewById(R.id.create);
        final Activity activity = this;
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String groupName = group.getText().toString();
                DatabaseReference newGroup = dbRef.child("groups").push(); //creates group
                Group group = new Group(groupName);
                newGroup.setValue(group);
                //gets generated key, remove "-" at the beginning
                String groupKey = newGroup.getKey();
                if (groupKey.charAt(0) == '-')
                    groupKey = groupKey.substring(1);
                ResourceManager.getCurrUser().child(GROUP_KEY).setValue(groupKey); //sets user's group


                TextView alertTextView = new TextView(activity);
                String report = "Congrats on creating " + groupName + "! Here is your group id: <b>" + groupKey
                        + "</b><br>Invite your friends & they can join your group with this key.";
                //noinspection deprecation
                alertTextView.setText(Html.fromHtml(report));
                alertTextView.setTextIsSelectable(true);
                AlertDialog.Builder b = new AlertDialog.Builder(activity);
                b.setView(alertTextView);
                b.setPositiveButton(
                        "OK!",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                AlertDialog confirmation = b.create();
                confirmation.show();

                ClipboardManager manager =
                        (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                manager.setPrimaryClip(ClipData.newPlainText("SocialSave Group Key", groupKey));
                // Show a message:
                Toast.makeText(v.getContext(), "Your group key has been copied to the clipboard",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }
}
