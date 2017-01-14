package com.yesand.socialsave.user.login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yesand.socialsave.MainActivity;
import com.yesand.socialsave.R;
import com.yesand.socialsave.ResourceManager;
import com.yesand.socialsave.user.create.SignUpActivity;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth fbAuth = FirebaseAuth.getInstance();
    private DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        final EditText login_email = (EditText) findViewById(R.id.login_email);
        final EditText login_password = (EditText) findViewById(R.id.login_password);
        final Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = login_email.getText().toString().trim();
                final String password = login_password.getText().toString().trim();
                fbAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String key = email.replace('.', '-');
                            ResourceManager.setCurrUser(dbRef.child("users").child(key));
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        final Button signup = (Button) findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
