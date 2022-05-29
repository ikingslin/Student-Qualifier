 package com.example.student;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

import static java.security.AccessController.getContext;

public class Register extends AppCompatActivity {
    //<----------------------------------------FireBase---------------------------------------->
    //FirebaseDatabase database;
    final FirebaseStorage storage = FirebaseStorage.getInstance();
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    //DatabaseReference myRef = database.getReference();
    StorageReference dispic;
    private FirebaseAuth mAuth;
    CollectionReference user;
    DocumentReference docRef;
    //<----------------------------------------FireBase---------------------------------------->

    private static final int PICK_IMAGE = 100;
    Uri dpuri;
    public int flag=0;
    final int PIC_CROP =2;

    //<----------------------------------------Views---------------------------------------->
    ImageButton vDispic;
    Button signup;
    public EditText name,studid,pass,passcheck,login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        vDispic = findViewById(R.id.dp);
        signup = findViewById(R.id.signupbtn);

        mAuth = FirebaseAuth.getInstance();

        name = findViewById(R.id.nametxt);
        studid = findViewById(R.id.studidtxt);
        pass = findViewById(R.id.passtxt);
        passcheck = findViewById(R.id.rechecktxt);
        login = findViewById(R.id.logindialog);
        dispic = storage.getReference();

        user = db.collection("users");


        vDispic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();

            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!studid.getText().toString().equals("") && !name.getText().toString().equals("")
                        && !pass.getText().toString().equals("") && !passcheck.getText().toString().equals("")) {
                    Map<String, Object> data1 = new HashMap<>();
                    data1.put("Name", name.getText().toString());

                    if (pass.getText().toString().equals(passcheck.getText().toString())) {
                        if(studid.getText().toString().contains("181ct")&&studid.getText().toString().length()==8){
                            if(pass.getText().toString().length()>=8){
                                flag = 1;
                            }
                            else{
                                Toast.makeText(Register.this,"Password must have atleast 8 characters",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(Register.this,"Enter proper ID",Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Register.this, "Password does not match", Toast.LENGTH_LONG).show();
                    }


                    Object obj = new String(studid.getText().toString());
                    try {
                        docRef = db.collection("users").document("" + studid.getText().toString());
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        flag = 0;
                                        Toast.makeText(Register.this, "User Account Exists", Toast.LENGTH_LONG).show();
                                    } else {
                                        if (name.getText() != null && studid.getText() != null && pass.getText() != null && passcheck.getText() != null && flag == 1) {
                                            flag = 1;
                                            data1.put("Password", pass.getText().toString());
                                            user.document(studid.getText().toString()).set(data1);
                                            uploadImage();
                                            Intent intent = new Intent(Register.this, Login.class);
                                            startActivity(intent);
                                        } else {
                                            flag = 0;
                                            Toast.makeText(Register.this, "Registeration Failed", Toast.LENGTH_SHORT).show();
                                        }


                                    }
                                }
                            }
                        });


                    } catch (Exception e) {
                        flag = 0;
                    }
                }
            }
        });
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);

    }
    public void performCrop(){
        try {

            CropImage.activity().setAspectRatio(1,1).setFixAspectRatio(true).setGuidelines(CropImageView.Guidelines.ON).start(this);
            CropImage.activity(dpuri).start(this);
        }
        catch(Exception anfe){
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            try {
                dpuri = data.getData();
                performCrop();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        else if(requestCode == PIC_CROP){
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                this.dpuri = result.getUri();
                vDispic.setImageURI(dpuri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
    private void uploadImage()
    {
        if (dpuri != null) {
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading DP...");
            progressDialog.show();

            StorageReference ref
                    = dispic.child("DP/" + studid.getText().toString());


            ref.putFile(dpuri)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(Register.this,
                                                    "Image Uploaded!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            progressDialog.dismiss();
                            Toast
                                    .makeText(Register.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int)progress + "%");
                                }
                            });
        }
    }

    public void goLogin(View view){
        docRef = null;

        Intent intent = new Intent(Register.this, Login.class);
        startActivity(intent);
    }
}
