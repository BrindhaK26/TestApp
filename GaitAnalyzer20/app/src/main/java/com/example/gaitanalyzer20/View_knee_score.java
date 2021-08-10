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

public class View_knee_score extends AppCompatActivity {
    final Context context = this;

    View v1,v2;
Button next;
    DatabaseReference databaseUsers1,databaseUsers,databaseUsers2,databaseUsers4;
    FirebaseAuth auth;
    FirebaseUser user;
    String id, month_count,Doctor;
    int spd1,spd2,spd3;
    int count;

    //fields
    TextView frwd_spd,bck_spd,gaitLevel,overall,condition,see_doc,physio,assist,greet,ex_list,rec;

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_knee_score);

        //assign fields
        frwd_spd = findViewById(R.id.gait_frwd_score);
        bck_spd = findViewById(R.id.gait_bck_score);
        gaitLevel = findViewById(R.id.gait_level);
        overall = findViewById(R.id.gait_score);
        condition = findViewById(R.id.condition);
        see_doc = findViewById(R.id.medical_state);
        physio = findViewById(R.id.physio_need);
        assist = findViewById(R.id.assistance);
        greet = findViewById(R.id.textView);
        ex_list = findViewById(R.id.EX);
        rec = findViewById(R.id.Recommendation);

        next = findViewById(R.id.progression);

        //db
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        id = user.getUid();
        databaseUsers2 = FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("Kneedetails").child("Count").child("count");
        databaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("UserDetails");



        //menu assigning
        v1 =  findViewById(R.id.imageView1);
        v2 =  findViewById(R.id.imageView2);

        //Display greet msg
        getdata();
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
                PopupMenu pm = new PopupMenu(View_knee_score.this, v);

                pm.getMenuInflater().inflate(R.menu.home_menu1, pm.getMenu());
                pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.home:
                                Intent i = new Intent(context,Ailment.class);
                                startActivity(i);
                                finish();
                                return true;
                            case R.id.archive:
                                Intent intent = new Intent(context,Health_profile.class);
                                startActivity(intent);
                                finish();
                                return true;

                            case R.id.delete:
                                Intent it = new Intent(context,View_history.class);
                                startActivity(it);
                                finish();
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
                PopupMenu pm = new PopupMenu(View_knee_score.this, v);

                pm.getMenuInflater().inflate(R.menu.home_menu2, pm.getMenu());
                pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.X:
                                Toast.makeText(View_knee_score.this, "Signed out successfully!", Toast.LENGTH_SHORT).show();
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(context,MainActivity.class);
                                startActivity(intent);
                                finish();
                                return true;

                            case R.id.Y:
                                Toast.makeText(View_knee_score.this, "Redirecting to help page...", Toast.LENGTH_SHORT).show();
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
        if(total==9)
            x="Normal";
        else if(total>=7 && total<9)
            x = "Onset";
        else if(total>=5 && total<7)
            x = "Mild";
        else if(total>=3 && total<5)
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
        if(x.equals("Mild") || x.equals("Onset")){
            ex_list.setText("->    Heel & calf stretch\n->    Quadriceps stretch\n->    Hamstring stretch\n->    Seated hip flexion\n->    4 way hip NJ");

        }else if(x.equals("Moderate")){
            ex_list.setText("->    Straight leg raises\n->    Prone straight leg raises\n->    Side leg raises\n->    Clamshells\n->    Bridging\n->    Seated long arc quad");

        }else{
            ex_list.setText("->    Straight leg raises\n->    Side leg raises\n->    Clamshells");
        }

    }

    private void getScore(){

        int total = spd1+spd2+spd3;
        overall.setText(String.valueOf(total));
        getDoc();

    }

    private void getDoc(){
        databaseUsers1 = FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("Kneedetails").child(month_count).child("doctor_consulted");
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
                getKneeData();
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(View_knee_score.this, "OOps couldn't fetch data",Toast.LENGTH_SHORT);
            }
        });

    }
    private void getKneeData(){

        databaseUsers1 = FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("Kneedetails").child(month_count);
        DatabaseReference dbRef = databaseUsers1.child("KneeData");

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                KneeData k1 = snapshot.getValue(KneeData.class);
                frwd_spd.setText(String.valueOf(k1.gait_speed_forward));
                bck_spd.setText(String.valueOf(k1.gait_speed_backward));
                gaitLevel.setText(String.valueOf(k1.gait_level_surface));
                spd1 = k1.gait_speed_forward;
                spd2 = k1.gait_speed_backward;
                spd3 = k1.gait_level_surface;
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
                    Toast.makeText(View_knee_score.this, "OOps couldn't fetch data",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(View_knee_score.this, "OOps couldn't fetch data",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
