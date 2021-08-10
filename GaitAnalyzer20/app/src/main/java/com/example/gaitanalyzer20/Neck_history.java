package com.example.gaitanalyzer20;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class Neck_history extends AppCompatActivity {

    final Context context = this;
    TextView greet;
    LinearLayout linearLayout;

    DatabaseReference databaseUsers1,databaseUsers,databaseUsers2,databaseUsers4;
    FirebaseAuth auth;
    FirebaseUser user;
    String id, month_count;
    int count;
    int score1,score2,score3,score4,score5,score6;

    public void setCount(int count) {
        this.count = count;
    }
    View v1,v2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.neck_history);

        linearLayout = (LinearLayout) findViewById(R.id.Neck_history_layout);

        greet = findViewById(R.id.textView);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        id = user.getUid();
        databaseUsers2 = FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("Neckdetails").child("Count").child("count");
        databaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("UserDetails");
        //databaseUsers4 = FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("Kneedetails").child("ImagePath").child("path");

        getdata();

        //menu assigning
        v1 = (ImageView) findViewById(R.id.imageView1);
        v2 = (ImageView)findViewById(R.id.imageView2);

        //menu defining for v1
        v1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu pm = new PopupMenu(Neck_history.this, v);

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
                PopupMenu pm = new PopupMenu(Neck_history.this, v);

                pm.getMenuInflater().inflate(R.menu.home_menu2, pm.getMenu());
                pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.X:
                                Toast.makeText(Neck_history.this, "Signed out successfully!", Toast.LENGTH_SHORT).show();
                                FirebaseAuth.getInstance().signOut();

                                Intent intent = new Intent(Neck_history.this,MainActivity.class);
                                startActivity(intent);
                                finish();
                                return true;

                            case R.id.Y:
                                Toast.makeText(Neck_history.this, "Redirecting to help page...", Toast.LENGTH_SHORT).show();
                                return true;
                        }

                        return true;
                    }
                });
                pm.show();

            }
        });
        getCount();

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

                setCount(no_of_month);
                month_count = "month" + count;
                if (count > 0){

                    getNeckData();
                }
                else {
                    TextView t1 = new TextView(Neck_history.this);
                    t1.setText("Oops! you have no record!");
                    t1.setTextSize(22);
                    t1.setGravity(Gravity.CENTER);
                    linearLayout.addView(t1);

                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(Neck_history.this, "Oops couldn't fetch data",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getNeckData() {
        for(int i=1;i<=count;i++) {
            String month = "month"+i;
            databaseUsers1 = FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("Neckdetails").child(month);
            DatabaseReference dbRef = databaseUsers1.child("NeckData");
            int finalI = i;
            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    NeckData l1 = snapshot.getValue(NeckData.class);
                    score1 = l1.head_turn_horizontal_forward;
                    score2 = l1.head_turn_horizontal_backward;
                    score3 = l1.head_turn_vertical_forward;
                    score4 = l1.head_turn_vertical_backward;
                    score5 = l1.eyes_closed_forward;
                    score6 = l1.eyes_closed_backward;

                    //final int it = finalI;
                    setData(finalI);

                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }
    }

    private void setData(int i) {
        int total = score1+score2+score3+score4+score5;
        String x;
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
        String topic="Trial "+i;
        String text = "Walking Forward with horizontal head turns:     "+score1+"/3\nWalking Backward with horizontal head turns:   "+score2+"/3\nWalking forward with vertical head turns:     "+score3+"/3\nWalking backward with vertical head turns:     "+score4+"/3\nWalking Forward with eyes closed:    "+score5+"/3\nWalking backward with eyes closed:   "+score6+"/3\n\nTotal score:  "+total+"/18\nCondition: "+x+"\n";
        TextView textView1 = new TextView(this);
        textView1.setText(topic);
        textView1.setTextSize(22);
        textView1.setTextColor(Color.BLACK);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10,10,10,10);
        textView1.setLayoutParams(params);

        linearLayout.addView(textView1);
        TextView textView2 = new TextView(this);
        textView2.setText(text);
        textView2.setTextSize(18);
        textView2.setTextColor(Color.BLACK);
        textView1.setPadding(10,5,10,5);
        linearLayout.addView(textView2);

    }

    private void getdata(){
        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserDetails value = snapshot.getValue(UserDetails.class);
                if(value!= null)
                    greet.setText("Welcome " + value.username);
                else
                    Toast.makeText(Neck_history.this, "OOps couldn't fetch data",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(Neck_history.this, "OOps couldn't fetch data",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
