package com.yesand.socialsave;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateAGroupActivity extends AppCompatActivity {

    private DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    private final String GROUP_KEY = "groupId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_a_group_);
        final EditText group = (EditText)findViewById(R.id.groupName);
        final Button create = (Button)findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String groupName = group.getText().toString();
                DatabaseReference newGroup = dbRef.child("groups").push(); //creates group
                newGroup.child("name").setValue(groupName);
                String groupKey = newGroup.getKey().substring(1); //gets generated key, remove "-" at the beginning
                ResourceManager.getCurrUser().child(GROUP_KEY).setValue(groupKey); //sets user's group

                AlertDialog.Builder b = new AlertDialog.Builder(CreateAGroupActivity.this);
                String report = "Congrats on creating " + groupName + "! Here is your group id: " + groupKey
                        +". Invite your friends & they can join your group with this key.";
                b.setMessage(report);
                b.setPositiveButton(
                        "OK!",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);;
                                finish();
                            }
                        });
                AlertDialog confirmation = b.create();
                confirmation.show();
            }
        });
    }
}
