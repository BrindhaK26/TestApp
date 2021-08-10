package com.example.gaitanalyzer20;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.util.Patterns;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.Range;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.Random;



public class Registration extends AppCompatActivity{
    //Initialize variable
    Button bt_reg;
    EditText email,pwd,confirm_pwd,captcha;
    CheckBox checkbox;
    private FirebaseAuth auth;
    AwesomeValidation awesomeValidation;
    TextView t1;
    int verificationCode;
    boolean flag= false;
    Long CaptchaValue;
    final Context context = this;
    //String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        //Assign variable

        bt_reg =  findViewById(R.id.button);

        email = findViewById(R.id.et_email);
        pwd = findViewById(R.id.password1_pg2);
        confirm_pwd = findViewById(R.id.password2_pg2);
        checkbox = findViewById(R.id.checkBox);
        t1 = findViewById(R.id.verify_code);
        RandomNum_Generator();
        t1.setText(String.valueOf(verificationCode));
        captcha = findViewById(R.id.verify_val);

        //assign auth variable
        auth = FirebaseAuth.getInstance();


        // assign validation variable
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        //validate fields


        awesomeValidation.addValidation(this,R.id.et_email, Patterns.EMAIL_ADDRESS, R.string.invalid_email);

        String regexPassword = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+!=])(?=\\S+$).{4,}$";
        awesomeValidation.addValidation(this,R.id.password1_pg2, regexPassword, R.string.invalid_password);
        awesomeValidation.addValidation(this,R.id.password2_pg2, R.id.password1_pg2, R.string.invalid_confirm_password);



        bt_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if (captcha.getText().toString() != null) {
                        String value = captcha.getText().toString();
                        CaptchaValue = Long.parseLong(value);
                        code_verifier(CaptchaValue);

                        if (awesomeValidation.validate() && flag) {
                            if (checkbox.isChecked()) {
                                String txt_email = email.getText().toString();
                                String txt_pwd = confirm_pwd.getText().toString();
                                registerUser(txt_email,txt_pwd);
                            } else {
                                Toast.makeText(context, "Please accept the terms and conditions", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(context, "Validation Failed", Toast.LENGTH_SHORT).show();

                        }
                    }
                } catch (Exception e)
                {
                    Toast.makeText(context, "Please enter the verification code", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }
//register a new user in firebase
    private void registerUser(String email, String pwd) {
        auth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(Registration.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    FirebaseUser user = auth.getCurrentUser();
                   // id = auth.getCurrentUser().getUid();
                    Toast.makeText(Registration.this,"Registration Successful! Please re-login to use the app !",Toast.LENGTH_LONG).show();
                    updateUI(user);
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(context , MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(Registration.this,"Registration Failed!",Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    private void updateUI(FirebaseUser user) {
    }

    public void RandomNum_Generator() {

        Random random = new Random();
        int x = random.nextInt(50000);
        verificationCode =  x;
    }
    public void code_verifier(Long y){

        if (y == verificationCode) {
            flag = true;
        } else {
            flag = false;
        }

    }

}
