package com.example.gaitanalyzer20;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

public class Treatment_details extends AppCompatActivity {
    final Context context = this;
    int count;
    EditText treatment_duration,surgery,precautions,physio,physio_details;
    AutoCompleteTextView diagnosis;
    Button next;
    AwesomeValidation awesomeValidation;
    TextView greet;
    //Menu views
    View v1, v2;

    DatabaseReference databaseUsers, databaseUsers1, databaseUsers2;
    FirebaseAuth auth;
    FirebaseUser user;
    String id;
    String[] ailment_list = { "Arthritis","Osteoporosis", "Disc prolapse","Spinal problem","Tuberculosis"};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.treatment_details);

        Intent intent = getIntent();
        String activity = intent.getStringExtra("activity");

        //field assigning
        greet = findViewById(R.id.textView);

        treatment_duration = findViewById(R.id.treatment_duration);
        surgery = findViewById(R.id.surgery);
        precautions = findViewById(R.id.precautions);
        physio = findViewById(R.id.physio);
        physio_details = findViewById(R.id.physio_details);
        diagnosis = findViewById(R.id.diagnosis);
        next = findViewById(R.id.treatment_next);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_singlechoice,ailment_list);


        //Set the number of characters the user must type before the drop down list is shown
        diagnosis.setThreshold(1);
        //Set the adapter
        diagnosis.setAdapter(adapter);

        //menu assigning
        v1 = (ImageView) findViewById(R.id.imageView1);
        v2 = (ImageView) findViewById(R.id.imageView2);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        id = user.getUid();
        databaseUsers1 = FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("UserDetails");
        if (activity.equals("Lower_back_ailment")) {
            databaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("Lowerbackdetails");
            databaseUsers2 = FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("Lowerbackdetails").child("Count").child("count");
        } else if (activity.equals("Neck_ailment")) {
            databaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("Neckdetails");
            databaseUsers2 = FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("Neckdetails").child("Count").child("count");
        } else {
            databaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("Kneedetails");
            databaseUsers2 = FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("Kneedetails").child("Count").child("count");
        }

        getCount();
        //Display greet msg
        getdata();


        //menu defining for v1
        v1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu pm = new PopupMenu(Treatment_details.this, v);

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
                PopupMenu pm = new PopupMenu(Treatment_details.this, v);

                pm.getMenuInflater().inflate(R.menu.home_menu2, pm.getMenu());
                pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.X:
                                Toast.makeText(Treatment_details.this, "Signed out successfully!", Toast.LENGTH_SHORT).show();
                                FirebaseAuth.getInstance().signOut();

                                Intent intent = new Intent(context, MainActivity.class);
                                startActivity(intent);
                                finish();
                                return true;

                            case R.id.Y:
                                Toast.makeText(Treatment_details.this, "Redirecting to help page...", Toast.LENGTH_SHORT).show();
                                return true;
                        }
                        return true;
                    }
                });
                pm.show();

            }
        });
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(this, R.id.treatment_duration, Range.closed(0,100), R.string.invalid_duration);
        awesomeValidation.addValidation(this,R.id.diagnosis, RegexTemplate.NOT_EMPTY, R.string.invalid);

        awesomeValidation.addValidation(this,R.id.surgery, "^Yes$|^No$|^yes$|^no$", R.string.invalid);
        awesomeValidation.addValidation(this,R.id.precautions, "^Yes$|^No$|^yes$|^no$", R.string.invalid);
        awesomeValidation.addValidation(this,R.id.physio, "^Yes$|^No$|^yes$|^no$", R.string.invalid);
        if(physio.getText().toString().equals("Yes")){
            awesomeValidation.addValidation(this,R.id.physio_details, RegexTemplate.NOT_EMPTY, R.string.invalid);
        }
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(awesomeValidation.validate()){
                    addTreatmentDetails();
                    Intent intent = new Intent(context, Start_record_knee1.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(context, "Please Enter the correct details", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addTreatmentDetails() {
        String current_month = "month" + count;
        String Surgery =  surgery.getText().toString();
        String a = treatment_duration.getText().toString();
        int Treatment_duration = Integer.parseInt(a);
        String Precaution = precautions.getText().toString();
        String Physio = physio.getText().toString();
        String Physio_details = physio_details.getText().toString();
        String Diagnosis = diagnosis.getText().toString();

        TreatmentData u1 = new TreatmentData(Treatment_duration,Diagnosis,Surgery,Precaution,Physio,Physio_details);
        databaseUsers.child(current_month).child("TreatmentDetails").setValue(u1);
    }

    private void getCount() {
        databaseUsers2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int no_of_month = 0;
                if (snapshot.exists() && snapshot.getValue() != null) {
                    no_of_month = ((Long) snapshot.getValue()).intValue();
                }
                count = no_of_month;
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(Treatment_details.this, "OOps couldn't fetch data",Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void getdata() {
        databaseUsers1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserDetails value = snapshot.getValue(UserDetails.class);
                if(value!= null)
                    greet.setText("Welcome " + value.username);
                else
                    Toast.makeText(Treatment_details.this, "OOps couldn't fetch data",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(Treatment_details.this, "OOps couldn't fetch data",Toast.LENGTH_SHORT).show();
            }
        });
        }
}
