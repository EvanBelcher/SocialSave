package com.yesand.socialsave;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseError;

public class JoinGroupActvity extends AppCompatActivity {

    private DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    private final String GROUP_KEY = "groupId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join__group__actvity);
        final EditText group = (EditText)findViewById(R.id.groupId);
        final Button confirm = (Button)findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String groupId = "-" + group.getText().toString();
                ResourceManager.getCurrUser().child(GROUP_KEY).setValue(groupId); //sets user's group

                DatabaseReference nameRef = dbRef.child("groups").child(groupId).child("name");
                nameRef.addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        String groupName = snapshot.getValue().toString();
                        String confirmation = "Congrats! You've joined " + groupName + "!";
                        Toast.makeText(JoinGroupActvity.this, confirmation, Toast.LENGTH_LONG).show();
                        //TO DO: change Toast to Dialog with a button that takes you to TeamFragment
                    }
                    @Override
                    public void onCancelled(DatabaseError error) { }
                });
            }
        });
    }
}
