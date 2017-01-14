package com.yesand.socialsave.group.join;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseError;
import com.yesand.socialsave.MainActivity;
import com.yesand.socialsave.R;
import com.yesand.socialsave.ResourceManager;

public class JoinGroupActvity extends AppCompatActivity {

    private DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    private final String GROUP_KEY = "groupId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join__group__actvity);
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        final EditText group = (EditText) findViewById(R.id.groupId);
        final Button confirm = (Button) findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String groupId = group.getText().toString();
                if (groupId.charAt(0) != '-')
                    groupId = "-" + groupId;
                ResourceManager.getCurrUser().child(GROUP_KEY).setValue(groupId); //sets user's group

                DatabaseReference nameRef = dbRef.child("groups").child(groupId).child("name");
                nameRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        String groupName = snapshot.getValue().toString();
                        AlertDialog.Builder b = new AlertDialog.Builder(JoinGroupActvity.this);
                        String report = "Congrats! You've joined " + groupName + "!";
                        b.setMessage(report);
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
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                    }
                });
            }
        });
    }
}
