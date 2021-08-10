package com.example.gaitanalyzer20;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class Start_record_neck6 extends AppCompatActivity {


    ImageView rec;
    TextView greet;

    int score1,score2,score3,score4,score5,score6;
    String extra1,extra2,extra3,extra4,extra5;
    Float speed;
    int score;

    View v1,v2;
    private final int CAMERA_PERMISSION = 1;
    final Context context = this;

    DatabaseReference databaseUsers1,databaseUsers,databaseUsers2,databaseUsers4;
    FirebaseAuth auth;
    FirebaseUser user;
    String id, imagePath, month_count;
    int count;

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_record_neck6);

        rec = findViewById(R.id.rec_neck6);
        greet = findViewById(R.id.textView);

        //Output = findViewById(R.id.output);
        extra1 = getIntent().getStringExtra("spd1");
        extra2= getIntent().getStringExtra("spd2");
        extra3 = getIntent().getStringExtra("spd3");
        extra4 = getIntent().getStringExtra("spd4");
        extra5 = getIntent().getStringExtra("spd5");



        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        id = user.getUid();

        databaseUsers2 = FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("Neckdetails").child("Count").child("count");
        databaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("UserDetails");
        databaseUsers4 = FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("Neckdetails").child("ImagePath").child("path");

        //get the stored image
        getImgPath();

        getCount();

        //menu assigning
        v1 =  findViewById(R.id.imageView1);
        v2 =  findViewById(R.id.imageView2);

        //Display greet msg
        getdata();


        //menu defining for v1
        v1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu pm = new PopupMenu(Start_record_neck6.this, v);

                pm.getMenuInflater().inflate(R.menu.home_menu1, pm.getMenu());
                pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.home:
                                //Toast.makeText(Start_record_neck1.this, "Clicked Home", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(context,Ailment.class);
                                startActivity(i);
                                finish();
                                return true;
                            case R.id.archive:
                                //Toast.makeText(Start_record_neck1.this, "Clicked Profile", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(context,Health_profile.class);
                                startActivity(intent);
                                finish();
                                return true;

                            case R.id.delete:
                                Intent in = new Intent(context,View_history.class);
                                startActivity(in);
                                finish();
                                //Toast.makeText(context, "Clicked View history", Toast.LENGTH_SHORT).show();
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
                PopupMenu pm = new PopupMenu(Start_record_neck6.this, v);

                pm.getMenuInflater().inflate(R.menu.home_menu2, pm.getMenu());
                pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.X:
                                Toast.makeText(Start_record_neck6.this, "Signed out successfully!", Toast.LENGTH_SHORT).show();
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(context,MainActivity.class);
                                startActivity(intent);
                                finish();
                                return true;

                            case R.id.Y:
                                Toast.makeText(Start_record_neck6.this, "Redirecting to help page...", Toast.LENGTH_SHORT).show();
                                return true;
                        }
                        return true;
                    }
                });
                pm.show();
            }
        });

        ActivityResultLauncher<Intent> getAction= registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Here, no request code
                        Intent data = result.getData();
                        Uri uri = data.getData();
                        String path = getPathFromUri(context,uri);

                        if (! Python.isStarted()) {
                            Python.start(new AndroidPlatform(context));
                        }
                        Python py = Python.getInstance();
                        PyObject pyobj = py.getModule("gait_speed");
                        PyObject obj = pyobj.callAttr("main",path,imagePath);

                        speed = obj.toFloat();

                        if(speed<=2)
                            score = 3;
                        else if(speed>2 && speed<=3.5)
                            score =  2;
                        else if(speed>3.5 && speed<=4.5)
                            score= 1;
                        else {
                            score = 0;
                        }

                        setNeckData();
                        Intent intent = new Intent(context,View_neck_score.class);
                        startActivity(intent);
                        finish();
                    }
                });

        rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imagePath!=null) {
                    if (ContextCompat.checkSelfPermission(Start_record_neck6.this,
                            Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);

                        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
                            getAction.launch(takeVideoIntent);
                        } else {
                            Toast.makeText(Start_record_neck6.this, "Error...", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        requestStoragePermission();
                    }
                }else{
                    Toast.makeText(context,"Please select an image of yours first!",Toast.LENGTH_LONG).show();
                }
            }

        });

    }

    private void setNeckData() {
        score1 = Integer.parseInt(extra1);
        score2 = Integer.parseInt(extra2);
        score3 = Integer.parseInt(extra3);
        score4 = Integer.parseInt(extra4);
        score5 = Integer.parseInt(extra5);

        databaseUsers1 = FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("Neckdetails").child(month_count);
        DatabaseReference dbRef = databaseUsers1.child("NeckData");
        NeckData l1= new NeckData(score1,score2,score3,score4,score5,score6);
        dbRef.setValue(l1);
    }

    private void getImgPath() {
        databaseUsers4.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) { ;
                if (snapshot.exists() && snapshot.getValue() != null) {
                    imagePath = snapshot.getValue().toString();

                }else{
                    String x = "NULL";
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(Start_record_neck6.this, "OOps couldn't fetch image path",Toast.LENGTH_SHORT).show();
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

            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(Start_record_neck6.this, "OOps couldn't fetch data",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void requestStoragePermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)){
            new AlertDialog.Builder(this).setTitle("Permission needed")
                    .setMessage("This permission is needed for the app to capture video")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(Start_record_neck6.this,new String []{Manifest.permission.CAMERA},CAMERA_PERMISSION);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();

        }else{
            ActivityCompat.requestPermissions(this,new String []{Manifest.permission.CAMERA},CAMERA_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @org.jetbrains.annotations.NotNull String[] permissions, @NonNull @org.jetbrains.annotations.NotNull int[] grantResults) {
        if(requestCode == CAMERA_PERMISSION){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void getdata() {

        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserDetails value = snapshot.getValue(UserDetails.class);
                if(value!= null) {
                    greet.setText("Welcome " + value.username);
                }
                else
                    Toast.makeText(Start_record_neck6.this, "OOps couldn't fetch data",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(Start_record_neck6.this, "OOps couldn't fetch data",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public static String getPathFromUri(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

}
