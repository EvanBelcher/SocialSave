package com.yesand.socialsave;

import android.app.Dialog;
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
                String groupKey = newGroup.getKey(); //gets generated key
                String confirmation = "Congrats on creating " + groupName + "! Here is your group id: " + groupKey
                        +". Invite your frineds & they can join your group with this key.";
                Toast.makeText(CreateAGroupActivity.this, confirmation, Toast.LENGTH_LONG).show();
            }
        });
    }
}
