package com.yesand.socialsave.user.create;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yesand.socialsave.Constants;
import com.yesand.socialsave.R;
import com.yesand.socialsave.ResourceManager;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth fbAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener fbAuthListener;
    private DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    public final static String TAG = SignUpActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_);
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        fbAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out:");
                }
            }
        };

        final EditText name = (EditText) findViewById(R.id.UserName);
        final EditText email = (EditText) findViewById(R.id.UserEmail);
        final EditText password = (EditText) findViewById(R.id.userPassword);
        final EditText confirmPw = (EditText) findViewById(R.id.UserConfirmPassword);
        final EditText id = (EditText) findViewById(R.id.bankId);

        Button next = (Button) findViewById(R.id.nextButton);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userpw = password.getText().toString();
                String pw2 = confirmPw.getText().toString();
                if (!TextUtils.equals(userpw, pw2)) {
                    Toast.makeText(SignUpActivity.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                } else {
                    final String username = name.getText().toString();
                    final String useremail = email.getText().toString();
                    final String usernessie = id.getText().toString();

                    fbAuth.createUserWithEmailAndPassword(useremail, userpw).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                //noinspection ConstantConditions,ThrowableResultOfMethodCallIgnored
                                Constants.error(task.getException().getMessage(), false, SignUpActivity.this);
                            } else {
                                User user = new User(username, userpw, usernessie);
                                String key = useremail.replace('.', '-');
                                ResourceManager.setCurrUser(dbRef.child("users").child(key));
                                ResourceManager.getCurrUser().setValue(user);
                                Intent intent = new Intent(getApplicationContext(), MakingGoals.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        fbAuth.addAuthStateListener(fbAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (fbAuthListener != null) {
            fbAuth.removeAuthStateListener(fbAuthListener);
        }
    }
}
