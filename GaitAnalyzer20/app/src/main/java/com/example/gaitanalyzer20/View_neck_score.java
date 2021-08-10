package com.example.gaitanalyzer20;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class View_neck_score extends AppCompatActivity {
    final Context context = this;

    View v1,v2;

    DatabaseReference databaseUsers1,databaseUsers,databaseUsers2,databaseUsers4;
    FirebaseAuth auth;
    FirebaseUser user;
    String id, month_count,Doctor;
    int spd1,spd2,spd3,spd4,spd5,spd6;
    int count;

    Button next;

    //fields
    TextView H_frwd_spd,H_bck_spd,V_frwd_spd,V_bck_spd,eyes_frwd,eyes_bkwd;
    TextView overall,condition,see_doc,physio,assist,greet,ex_list,rec;

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_neck_score);

        //assign fields
        H_frwd_spd = findViewById(R.id.gait_frwd_score);
        H_bck_spd = findViewById(R.id.gait_bck_score);
        V_frwd_spd = findViewById(R.id.vertical_frwd);
        V_bck_spd = findViewById(R.id.vertical_bkwd);
        eyes_frwd = findViewById(R.id.eyes_closed_frwd);
        eyes_bkwd = findViewById(R.id.eyes_closed_bkwd);
        next = findViewById(R.id.progression);


        overall = findViewById(R.id.gait_score);
        condition = findViewById(R.id.condition);
        see_doc = findViewById(R.id.medical_state);
        physio = findViewById(R.id.physio_need);
        assist = findViewById(R.id.assistance);
        greet = findViewById(R.id.textView);

        ex_list = findViewById(R.id.EX);
        rec = findViewById(R.id.Recommendation);

        //db
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        id = user.getUid();
        databaseUsers2 = FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("Neckdetails").child("Count").child("count");
        databaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("UserDetails");



        //menu assigning
        v1 =  findViewById(R.id.imageView1);
        v2 =  findViewById(R.id.imageView2);

        //Display greet msg
        getdata();

        //get current month
        getCount();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,Health_progression.class);
                startActivity(i);
            }
        });

        //menu defining for v1
        v1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu pm = new PopupMenu(View_neck_score.this, v);

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
                PopupMenu pm = new PopupMenu(View_neck_score.this, v);

                pm.getMenuInflater().inflate(R.menu.home_menu2, pm.getMenu());
                pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.X:
                                Toast.makeText(View_neck_score.this, "Signed out successfully!", Toast.LENGTH_SHORT).show();
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(context,MainActivity.class);
                                startActivity(intent);
                                finish();
                                return true;

                            case R.id.Y:
                                Toast.makeText(View_neck_score.this, "Redirecting to help page...", Toast.LENGTH_SHORT).show();
                                return true;
                        }
                        return true;
                    }
                });
                pm.show();
            }
        });

    }



    private void getCondition() {
        String frwd_string = overall.getText().toString();
        int total = Integer.parseInt(frwd_string);
        String x;

        //condition
        if(total==18)
            x="Normal";
        else if(total>=15 && total<18)
            x = "Onset";
        else if(total>=11 && total<15)
            x = "Mild";
        else if(total>=5 && total<11)
            x = "Moderate";
        else
            x="Severe";
        condition.setText(x);

        //doc & physio
        if(!x.equals("Normal")) {
            see_doc.setText("Yes");
            physio.setText("Yes");
        }
        else {
            see_doc.setText("No");
            physio.setText("No");
        }

        //assistance
        if(x.equals("Severe") || x.equals("Moderate"))
            assist.setText("Yes");
        else
            assist.setText("No");
        //recommendation
        if(Doctor.equals("Yes") || Doctor.equals("yes")){
            if(x.equals("Normal")) {
                rec.setText("Hi! You are perfectly alright and good to go!\nYou have already seen a doctor which is a good thing to mention.Do keep up the great work!");
            }else if(x.equals("Onset")){
                rec.setText("Hi! We feel that you are currently good to go\n You have visited a doctor before which is a good thing to mention\n We feel that you might start developing minor problems in the future.\n Do take up the exercises mentioned below to stay healthy!\n Have a good day!");
            }else if(x.equals("Mild")){
                rec.setText("Hi! We feel that you are currently facing very minor issues\n You have visited a doctor before which is a good thing to mention\n We feel that you might start developing certain problems in the future if not looked after.\n Do take up the exercises mentioned below to stay healthy!\n Have a good day!");
            }else if(x.equals("Moderate")){
                rec.setText("Hi! We feel that you are currently facing certain issues\n You have visited a doctor before which is a good thing to mention\n We feel that you might start developing major problems in the future if not looked after.\n Do take up the exercises mentioned below to stay healthy!\n Have a good day!");
            }else{
                rec.setText("Hi! We feel that you are currently facing major issues\n You have visited a doctor before which is a good thing to mention\n We suggest that you take continue to up the treatment given by your doctor.\n Do take up the exercises mentioned below to stay healthy!\n Have a good day!");
            }
        }else{
            if(x.equals("Normal")) {
                rec.setText("Hi! You are perfectly alright and good to go!\nDo take up regular medical checkups and stay healthy as always!");
            }else if(x.equals("Onset")){
                rec.setText("Hi! We feel that you are currently good to go\n You have not visited a doctor yet so we suggest you to visit one.\n We feel that you might start developing minor problems in the future.\n Do take up the exercises mentioned below to stay healthy!\n Have a good day!");
            }else if(x.equals("Mild")){
                rec.setText("Hi! We feel that you are currently facing very minor issues\n You have not visited a doctor yet so we suggest you to visit one.\n We feel that you might start developing certain problems in the future if not looked after.\n Do take up the exercises mentioned below to stay healthy!\n Have a good day!");
            }else if(x.equals("Moderate")){
                rec.setText("Hi! We feel that you are currently facing certain issues\n You have not visited a doctor yet so we suggest you to visit one.\n We feel that you might start developing major problems in the future if not looked after.\n Do take up the exercises mentioned below to stay healthy!\n Have a good day!");
            }else{
                rec.setText("Hi! We feel that you are currently facing major issues\n You have not visited a doctor yet so we suggest you to visit one.\n We suggest that you take continue to up the treatment given by your doctor.\n Do take up the exercises mentioned below to stay healthy!\n Have a good day!");
            }
        }

        //exercises
        if(x.equals("Mild") || x.equals("Onset")) {
            ex_list.setText("->    Flexion stretch\n->    Levator scapulae stretch\n->    Chin tuck");

        }else{
            ex_list.setText("->    Lateral flexion stretch\n->    Corner stretch\n->    Prone cobra stretch\n->   Back burn");
        }

    }

    private void getScore(){

        int total = spd1+spd2+spd3+spd4+spd5+spd6;
        overall.setText(String.valueOf(total));
        getDoc();

    }

    private void getDoc(){
        databaseUsers1 = FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("Lowerbackdetails").child(month_count).child("doctor_consulted");
        databaseUsers1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getValue() != null) {
                    Doctor = snapshot.getValue().toString();
                    getCondition();
                }else{
                    getCondition();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }


    private void getCount() {
        databaseUsers2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int no_of_month = 0;
                if (snapshot.exists() && snapshot.getValue() != null) {
                    no_of_month = ((Long) snapshot.getValue()).intValue();
                }
                count= no_of_month;

                setCount(no_of_month);
                month_count = "month"+count;
                getNeckData();
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(View_neck_score.this, "OOps couldn't fetch data",Toast.LENGTH_SHORT);
            }
        });

    }
    private void getNeckData(){

        databaseUsers1 = FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("Neckdetails").child(month_count);
        DatabaseReference dbRef = databaseUsers1.child("NeckData");

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                NeckData k1 = snapshot.getValue(NeckData.class);
                H_frwd_spd.setText(String.valueOf(k1.head_turn_horizontal_forward));
                H_bck_spd.setText(String.valueOf(k1.head_turn_horizontal_backward));
                V_frwd_spd.setText(String.valueOf(k1.head_turn_vertical_forward));
                V_bck_spd.setText(String.valueOf(k1.head_turn_vertical_backward));
                eyes_frwd.setText(String.valueOf(k1.eyes_closed_forward));
                eyes_bkwd.setText(String.valueOf(k1.eyes_closed_backward));

                spd1 = k1.head_turn_horizontal_backward;
                spd2 = k1.head_turn_horizontal_forward;
                spd3 = k1.head_turn_vertical_backward;
                spd4 = k1.head_turn_vertical_forward;
                spd5 = k1.eyes_closed_backward;
                spd6 = k1.eyes_closed_forward;
                getScore();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }
    private void getdata(){


        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserDetails value = snapshot.getValue(UserDetails.class);

                if(value!= null)
                    greet.setText("Welcome " + value.username);
                else
                    Toast.makeText(View_neck_score.this, "OOps couldn't fetch data",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(View_neck_score.this, "OOps couldn't fetch data",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
