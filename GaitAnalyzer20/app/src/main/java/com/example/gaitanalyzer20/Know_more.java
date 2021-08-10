package com.example.gaitanalyzer20;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.common.collect.Range;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class Know_more extends AppCompatActivity {

    final Context context = this;
    AwesomeValidation awesomeValidation;

    // fields initializing
    EditText u_name,name,gender,age,mobile,job;
    Button next;
    TextView greet;


    //Menu views
    View v1,v2;

    DatabaseReference databaseUsers,databaseUsers1;
    FirebaseAuth auth;
    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.know_more);

        //field assigning
        next = findViewById(R.id.button5);
        name = findViewById(R.id.Name);
        gender = findViewById(R.id.Gender);
        age = findViewById(R.id.dob);
        mobile= findViewById(R.id.mobile);
        job = findViewById(R.id.occupation);
        u_name = findViewById(R.id.U_Name);
        greet = findViewById(R.id.textView);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        id = auth.getCurrentUser().getUid();
        databaseUsers = FirebaseDatabase.getInstance().getReference("Users");


        //Display greet msg
       // getdata();

        //menu assigning
        v1 = (ImageView) findViewById(R.id.imageView1);
        v2 = (ImageView)findViewById(R.id.imageView2);

        //menu defining for v1
        v1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu pm = new PopupMenu(Know_more.this, v);

                pm.getMenuInflater().inflate(R.menu.home_menu1, pm.getMenu());
                pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.home:
                                Intent i = new Intent(context,Ailment.class);
                                startActivity(i);
                                finish();
                                //Toast.makeText(Ailment.this, "Clicked Home", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.archive:
                                Intent intent = new Intent(context,Health_profile.class);
                                startActivity(intent);
                                finish();
                                // Toast.makeText(Ailment.this, "Clicked Profile", Toast.LENGTH_SHORT).show();
                                return true;

                            case R.id.delete:
                                Intent it = new Intent(context,View_history.class);
                                startActivity(it);
                                finish();
                                //Toast.makeText(Ailment.this, "Clicked View history", Toast.LENGTH_SHORT).show();
                                return true;
                        }
                        return true;
                    }
                });
                pm.show();

            }
        });

        //menu defining for v2
        v2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu pm = new PopupMenu(Know_more.this, v);

                pm.getMenuInflater().inflate(R.menu.home_menu2, pm.getMenu());
                pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.X:
                                Toast.makeText(Know_more.this, "Signed out successfully!", Toast.LENGTH_SHORT).show();
                                FirebaseAuth.getInstance().signOut();

                                Intent intent = new Intent(context,MainActivity.class);
                                startActivity(intent);
                                finish();
                                return true;

                            case R.id.Y:
                                Toast.makeText(Know_more.this, "Redirecting to help page...", Toast.LENGTH_SHORT).show();
                                return true;
                        }
                        return true;
                    }
                });
                pm.show();

            }
        });

        // assign validation variable
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        //validate fields
        awesomeValidation.addValidation(this,R.id.U_Name, RegexTemplate.NOT_EMPTY, R.string.invalid_name);
        awesomeValidation.addValidation(this,R.id.Name, RegexTemplate.NOT_EMPTY, R.string.invalid_name);
        awesomeValidation.addValidation(this, R.id.mobile, "^[6-9]\\d{9}$", R.string.invalid_phone);
        awesomeValidation.addValidation(this, R.id.dob, Range.closed(0,100), R.string.invalid_age);

        awesomeValidation.addValidation(this, R.id.occupation, RegexTemplate.NOT_EMPTY, R.string.invalid_occupation);
        awesomeValidation.addValidation(this, R.id.Gender, "^male$|^female$|^other$|^Male$|^Female$|^Other$", R.string.invalid_gender);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (awesomeValidation.validate()) {
                    addUser();
                    Intent intent = new Intent(context, Ailment.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(context, "Please Enter the correct details", Toast.LENGTH_SHORT).show();

                }
            }

        });
    }

    private void addUser() {
        int count = 0;
        String uname = u_name.getText().toString();
        String Name = name.getText().toString();
        String Gender = gender.getText().toString();
        String Age_string = age.getText().toString();
        int Age = Integer.parseInt(Age_string);
        String mobile_string = mobile.getText().toString();
        Long Mobile = Long.valueOf(mobile_string);
        String Job = job.getText().toString();
        UserDetails u1 = new UserDetails(Name,uname,Mobile,Age,Gender,Job);
        databaseUsers.child(id).child("UserDetails").setValue(u1);
        databaseUsers.child(id).child("Kneedetails").child("Count").child("count").setValue(count);
        databaseUsers.child(id).child("Lowerbackdetails").child("Count").child("count").setValue(count);
        databaseUsers.child(id).child("Neckdetails").child("Count").child("count").setValue(count);
    }

}
