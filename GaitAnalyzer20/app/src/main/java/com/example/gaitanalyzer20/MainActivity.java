package com.example.gaitanalyzer20;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import com.chaquo.python.PyObject;
//import com.chaquo.python.Python;
//import com.chaquo.python.android.AndroidPlatform;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    //Initialize variable
    EditText email,pwd;
    Button bt_reg,bt_signIn;
    private FirebaseAuth auth;
    final Context context = this;
    TextView forgot_pwd;
    DatabaseReference databaseUsers;
    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Call the onclick functions
        addListenerOnRegButton();
        addListenerOnSignButton();

        //Assign variable
        email = findViewById(R.id.Email);
        pwd = findViewById(R.id.Password_pg1);
        bt_reg = (Button) findViewById(R.id.button3);
        bt_signIn = (Button) findViewById(R.id.button2);
        auth = FirebaseAuth.getInstance();
        forgot_pwd = findViewById(R.id.forgot_pwd);

        databaseUsers = FirebaseDatabase.getInstance().getReference("Users");

        forgot_pwd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText resetMail = new EditText(v.getContext());
                final AlertDialog.Builder pwdResetDialog = new AlertDialog.Builder(v.getContext());
                pwdResetDialog.setTitle("Reset Password");
                pwdResetDialog.setMessage("Enter the registered mail id to get the password reset link");
                pwdResetDialog.setView(resetMail);
                pwdResetDialog.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String email = resetMail.getText().toString();
                        auth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(MainActivity.this,"The reset link has been sent to your registered mail id.Please click on that to reset your password",Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Toast.makeText(MainActivity.this,e.getMessage() +" Reset password unsuccessful!",Toast.LENGTH_LONG).show();

                            }
                        });
                    }
                });
                pwdResetDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                pwdResetDialog.create().show();
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

    private void reload() {
    }

    // func onclick of register button
    public void addListenerOnRegButton() {
        bt_reg = (Button) findViewById(R.id.button3);
        bt_reg.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(MainActivity.this, Registration.class);
                startActivity(intent);
                finish();
            }

        });

    }
    // func on click of sign in button
    public void addListenerOnSignButton(){
        bt_signIn = (Button) findViewById(R.id.button2);
        bt_signIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if(email.getText().length()>0 && pwd.getText().length()>0){
                    String txt_email = email.getText().toString();
                    String txt_pwd = pwd.getText().toString();
                    LoginUser(txt_email,txt_pwd);

                }
                else{
                    String popup="Please fill all the fields";
                    Toast.makeText(getApplicationContext(),popup,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void LoginUser(String email, String pwd) {
        auth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    id = Objects.requireNonNull(auth.getCurrentUser()).getUid();
                    databaseUsers.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.getValue() != null) {
                                //user exists, do something
                                Intent intent = new Intent(MainActivity.this, Ailment.class);
                                startActivity(intent);
                                finish();
                            } else {
                                //user does not exist, do something else
                                Intent intent = new Intent(MainActivity.this, Know_more.class);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });

                } else {
                    Toast.makeText(MainActivity.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }
}