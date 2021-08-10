package com.example.gaitanalyzer20;

import android.Manifest;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.common.collect.Range;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class Knee_ailment extends AppCompatActivity {

    private static final int PICK_FROM_GALLERY = 2;
    final Context context = this;
    //Menu views
    View v1,v2;

    DatabaseReference databaseUsers,databaseUsers1,databaseUsers2;
    FirebaseAuth auth;
    FirebaseUser user;
    String id,imagePath;

    //initialize fields
    int count;
    TextView greet;
    TextView text;
    EditText side,illness,injury,severity,walk_problem,walk_aid,doc;
    Button next,selectPic;
    AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.knee_ailment);

        //assign fields
        side = findViewById(R.id.knee_ans1);
        injury = findViewById(R.id.knee_ans2);
        severity = findViewById(R.id.knee_ans3);
        walk_problem = findViewById(R.id.knee_ans4);
        walk_aid = findViewById(R.id.knee_ans5);
        doc = findViewById(R.id.knee_ans6);
        next = findViewById(R.id.button10);
        illness = findViewById(R.id.illnes_duration2);
        greet = findViewById(R.id.textView);
        selectPic = findViewById(R.id.Choose_pic_knee);

        text = findViewById(R.id.test);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        id = user.getUid();
        databaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("Kneedetails");
        databaseUsers1 = FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("UserDetails");
        databaseUsers2 = FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("Kneedetails").child("Count").child("count");
        getCount();
        //menu assigning
        v1 = (ImageView) findViewById(R.id.imageView1);
        v2 = (ImageView)findViewById(R.id.imageView2);

        //Display greet msg
        getdata();

        //menu defining for v1
        v1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu pm = new PopupMenu(Knee_ailment.this, v);

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
                PopupMenu pm = new PopupMenu(Knee_ailment.this, v);

                pm.getMenuInflater().inflate(R.menu.home_menu2, pm.getMenu());
                pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.X:
                                Toast.makeText(Knee_ailment.this, "Signed out successfully!", Toast.LENGTH_SHORT).show();
                                FirebaseAuth.getInstance().signOut();

                                Intent intent = new Intent(context,MainActivity.class);
                                startActivity(intent);
                                finish();
                                return true;

                            case R.id.Y:
                                Toast.makeText(Knee_ailment.this, "Redirecting to help page...", Toast.LENGTH_SHORT).show();
                                return true;
                        }
                        return true;
                    }
                });
                pm.show();

            }
        });
        ActivityResultLauncher<Intent> getActionImage= registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Here, no request code
                        Intent data = result.getData();
                        Uri imageUri = data.getData();

                        imagePath = getPathFromUri(context,imageUri);
                        //text.setText(imagePath);
                        databaseUsers.child("ImagePath").child("path").setValue(imagePath);
                    }
                });

        selectPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(Knee_ailment.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Intent i = new Intent();
                    i.setType("image/*");
                    i.setAction(Intent.ACTION_GET_CONTENT);

                    getActionImage.launch(Intent.createChooser(i,"Select Image"));

                }else{
                    requestGalleryPermission();
                }
            }
        });
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        //validate fields
        awesomeValidation.addValidation(this,R.id.knee_ans1, "^Left$|^Right$|^Both$|^left$|^right$|^both$", R.string.invalid);
        awesomeValidation.addValidation(this, R.id.illnes_duration2, Range.closed(0,100), R.string.invalid_duration);
        awesomeValidation.addValidation(this,R.id.knee_ans2, "^Yes$|^No$|^yes$|^no$", R.string.invalid);
        awesomeValidation.addValidation(this,R.id.knee_ans3,  Range.closed(0,3), R.string.invalid);
        awesomeValidation.addValidation(this,R.id.knee_ans4, "^Yes$|^No$|^yes$|^no$", R.string.invalid);
        awesomeValidation.addValidation(this,R.id.knee_ans5, "^Yes$|^No$|^yes$|^no$", R.string.invalid);
        awesomeValidation.addValidation(this,R.id.knee_ans6, "^Yes$|^No$|^yes$|^no$", R.string.invalid);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imagePath!=null) {
                    if (awesomeValidation.validate()) {
                        addKneeDetails();
                        String Doc_seen = doc.getText().toString();
                        Intent intent;
                        //if (count <= 6) {
                            if (Doc_seen.equals("Yes") || Doc_seen.equals("yes")) {
                                intent = new Intent(Knee_ailment.this, Treatment_details.class);
                                intent.putExtra("activity", "Knee_ailment");
                            } else {
                                //intent = new Intent(context, Treatment_details.class);
                                intent = new Intent(context, Start_record_knee1.class);
                            }
                      //  } else {
                        //    intent = new Intent(context, Ailment.class);
                       // }
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(context, "Please Enter the correct details", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(Knee_ailment.this,"Please first select a photo that clearly shows your face",Toast.LENGTH_LONG).show();
                }
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
                count = no_of_month;
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(Knee_ailment.this, "OOps couldn't fetch data",Toast.LENGTH_SHORT).show();
            }
        });


    }
    private void addKneeDetails() {
           count++;
      /*  if(count>6){
            Toast.makeText(Knee_ailment.this,"Oops your treatment period of 6 months is over! Hope you are better than before now! Have a good day! Bye",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(context, Ailment.class);
            startActivity(intent);
            finish();
        }

        else {*/
            String current_month = "month" + count;
            String Side = side.getText().toString();
            String Injury = injury.getText().toString();
            String Walk_prob = walk_problem.getText().toString();
            String Age_string = illness.getText().toString();
            int Illness = Integer.parseInt(Age_string);
            String mobile_string = severity.getText().toString();
            int Severity = Integer.parseInt(mobile_string);
            String Walk_aid = walk_aid.getText().toString();
            String Doc = doc.getText().toString();

            KneeDetails u1 = new KneeDetails(Illness, Side, Injury, Severity, Walk_prob, Walk_aid, Doc);
            databaseUsers.child(current_month).setValue(u1);
            databaseUsers.child("Count").child("count").setValue(count);
        //}
    }
    private void getdata() {
        databaseUsers1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserDetails value = snapshot.getValue(UserDetails.class);
                if(value!= null)
                    greet.setText("Welcome " + value.username);
                else
                    Toast.makeText(Knee_ailment.this, "OOps couldn't fetch data",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                    Toast.makeText(Knee_ailment.this, "OOps couldn't fetch data",Toast.LENGTH_SHORT).show();
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

    private void requestGalleryPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
            new AlertDialog.Builder(this).setTitle("Permission needed")
                    .setMessage("This permission is needed for the app to choose your photo")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(Knee_ailment.this,new String []{Manifest.permission.READ_EXTERNAL_STORAGE},PICK_FROM_GALLERY);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();

        }else{
            ActivityCompat.requestPermissions(this,new String []{Manifest.permission.READ_EXTERNAL_STORAGE},PICK_FROM_GALLERY);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @org.jetbrains.annotations.NotNull String[] permissions, @NonNull @org.jetbrains.annotations.NotNull int[] grantResults) {
        if(requestCode == PICK_FROM_GALLERY){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
