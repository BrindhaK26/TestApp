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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class Health_profile extends AppCompatActivity {

    private static final int PICK_FROM_GALLERY = 2;
    final Context context = this;
    //initialize fields
    TextView Name,u_name, Age,mobile, Gender,job;
    ImageView profilePic,editName,editUserName,editMobile;
    Button editProPic;



    //Menu views
    View v1,v2;

    DatabaseReference databaseUsers1;
    FirebaseAuth auth;
    FirebaseUser user;
    StorageReference storageReference;

    String id;
    TextView greet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_profile);

        //assign the fields
        Name = findViewById(R.id.profile_Name);
        u_name = findViewById(R.id.profile_u_name);
        Age = findViewById(R.id.profile_age);
        mobile = findViewById(R.id.profile_mobile);
        Gender = findViewById(R.id.profile_gender);
        job = findViewById(R.id.profile_occupation);



        editProPic = findViewById(R.id.update_profile_pic);
        editMobile = findViewById(R.id.edit_contact);
        editName = findViewById(R.id.edit_name);
        editUserName = findViewById(R.id.edit_user_name);

        profilePic = findViewById(R.id.ProfilePic);


        greet = findViewById(R.id.greet_msg);


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        id = user.getUid();
        databaseUsers1 = FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("UserDetails");
        storageReference = FirebaseStorage.getInstance().getReference();

        StorageReference profileRef = storageReference.child("users/"+id+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profilePic);
            }
        });

        //greet msg display
        getDataForUsername();

        //get profile details
         getdata();

        //menu assigning
        v1 = (ImageView) findViewById(R.id.imageView1);
        v2 = (ImageView)findViewById(R.id.imageView2);

        //menu defining for v1
        v1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu pm = new PopupMenu(Health_profile.this, v);

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
                PopupMenu pm = new PopupMenu(Health_profile.this, v);

                pm.getMenuInflater().inflate(R.menu.home_menu2, pm.getMenu());
                pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.X:
                                Toast.makeText(Health_profile.this, "Signed out successfully!", Toast.LENGTH_SHORT).show();
                                FirebaseAuth.getInstance().signOut();

                                Intent intent = new Intent(context,MainActivity.class);
                                startActivity(intent);
                                finish();
                                return true;

                            case R.id.Y:
                                Toast.makeText(Health_profile.this, "Redirecting to help page...", Toast.LENGTH_SHORT).show();
                                return true;
                        }
                        return true;
                    }
                });
                pm.show();

            }
        });

        //edit Name

        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText change_name = new EditText(v.getContext());
                final android.app.AlertDialog.Builder change_name_Dialog = new android.app.AlertDialog.Builder(v.getContext());
                change_name_Dialog.setTitle("Update your Name");
                change_name_Dialog.setMessage("Enter your new name");
                change_name_Dialog.setView(change_name);
                change_name_Dialog.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseUsers1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                UserDetails value = snapshot.getValue(UserDetails.class);
                                if(value!= null) {
                                    String uname = String.valueOf(value.username);
                                    String Name = change_name.getText().toString();
                                    String Gender = String.valueOf(value.gender);
                                    String Age_string = String.valueOf(value.age);
                                    int Age = Integer.parseInt(Age_string);
                                    String mobile_string = String.valueOf(value.contact);
                                    Long Mobile = Long.valueOf(mobile_string);
                                    String Job = String.valueOf(value.occupation);
                                    UserDetails u1 = new UserDetails(Name,uname,Mobile,Age,Gender,Job);
                                    databaseUsers1.setValue(u1);
                                    getdata();
                                }
                                else
                                    Toast.makeText(Health_profile.this, "OOps couldn't fetch data",Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                                Toast.makeText(Health_profile.this, "OOps couldn't fetch data",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                change_name_Dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                change_name_Dialog.create().show();
            }
        });

        //edit User Name

        editUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText change_userName = new EditText(v.getContext());
                final android.app.AlertDialog.Builder change_user_name_Dialog = new android.app.AlertDialog.Builder(v.getContext());
                change_user_name_Dialog.setTitle("Update your User Name");
                change_user_name_Dialog.setMessage("Enter your new UserName");
                change_user_name_Dialog.setView(change_userName);
                change_user_name_Dialog.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseUsers1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                UserDetails value = snapshot.getValue(UserDetails.class);
                                if(value!= null) {
                                    String uname = change_userName.getText().toString();
                                    String Name = String.valueOf(value.name);
                                    String Gender = String.valueOf(value.gender);
                                    String Age_string = String.valueOf(value.age);
                                    int Age = Integer.parseInt(Age_string);
                                    String mobile_string = String.valueOf(value.contact);
                                    Long Mobile = Long.valueOf(mobile_string);
                                    String Job = String.valueOf(value.occupation);
                                    UserDetails u1 = new UserDetails(Name,uname,Mobile,Age,Gender,Job);
                                    databaseUsers1.setValue(u1);
                                    getdata();
                                }
                                else
                                    Toast.makeText(Health_profile.this, "OOps couldn't fetch data",Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                                Toast.makeText(Health_profile.this, "OOps couldn't fetch data",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                change_user_name_Dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                change_user_name_Dialog.create().show();
            }
        });

        //edit Contact number

        editMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText change_contact = new EditText(v.getContext());
                final android.app.AlertDialog.Builder change_contact_Dialog = new android.app.AlertDialog.Builder(v.getContext());
                change_contact_Dialog.setTitle("Update your Contact number");
                change_contact_Dialog.setMessage("Enter your new Contact");
                change_contact_Dialog.setView(change_contact);
                change_contact_Dialog.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseUsers1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                UserDetails value = snapshot.getValue(UserDetails.class);
                                if(value!= null) {
                                    String uname = String.valueOf(value.username);;
                                    String Name = String.valueOf(value.name);
                                    String Gender = String.valueOf(value.gender);
                                    String Age_string = String.valueOf(value.age);
                                    int Age = Integer.parseInt(Age_string);
                                    String mobile_string = change_contact.getText().toString();
                                    Long Mobile = Long.valueOf(mobile_string);
                                    String Job = String.valueOf(value.occupation);
                                    UserDetails u1 = new UserDetails(Name,uname,Mobile,Age,Gender,Job);
                                    databaseUsers1.setValue(u1);
                                    getdata();
                                }
                                else
                                    Toast.makeText(Health_profile.this, "OOps couldn't fetch data",Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                                Toast.makeText(Health_profile.this, "OOps couldn't fetch data",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                change_contact_Dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                change_contact_Dialog.create().show();
            }
        });


        ActivityResultLauncher<Intent> getAction= registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Here, no request code
                        Intent data = result.getData();
                        Uri imageUri = data.getData();
                        uploadImageToFirebase(imageUri);

                    }
                });

        //update profile pic
        editProPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(Health_profile.this,Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Intent i = new Intent();
                    i.setType("image/*");
                    i.setAction(Intent.ACTION_GET_CONTENT);

                        getAction.launch(Intent.createChooser(i,"Select Image"));

                }else{
                    requestGalleryPermission();
                }
            }
        });

    }

    private void uploadImageToFirebase(Uri imageUri) {
        //upload image to firebase
        StorageReference fileRef = storageReference.child("users/"+id+"/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profilePic);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(Health_profile.this,"Upload Failed! Try again later.",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void requestGalleryPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
            new AlertDialog.Builder(this).setTitle("Permission needed")
                    .setMessage("This permission is needed for the app to set your profile photo")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(Health_profile.this,new String []{Manifest.permission.READ_EXTERNAL_STORAGE},PICK_FROM_GALLERY);
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
    private void getdata() {
        databaseUsers1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserDetails value = snapshot.getValue(UserDetails.class);
                if(value!= null) {


                    u_name.setText(String.valueOf(value.username));
                    Name.setText(String.valueOf(value.name));
                    Age.setText(String.valueOf(value.age));
                    mobile.setText(String.valueOf(value.contact));
                    Gender.setText(String.valueOf(value.gender));
                    job.setText(String.valueOf(value.occupation));
                }
                else
                    Toast.makeText(Health_profile.this, "OOps couldn't fetch data",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(Health_profile.this, "OOps couldn't fetch data",Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getDataForUsername() {
        databaseUsers1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserDetails value = snapshot.getValue(UserDetails.class);
                if(value!= null) {
                    greet.setText("Welcome " + value.username);
                }
                else
                    Toast.makeText(Health_profile.this, "OOps couldn't fetch data",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(Health_profile.this, "OOps couldn't fetch data",Toast.LENGTH_SHORT).show();
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
