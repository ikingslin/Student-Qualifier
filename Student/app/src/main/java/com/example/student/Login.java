package com.example.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference docRef;
    private FirebaseAuth mAuth;

    EditText studid,pass;
    Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        studid = findViewById(R.id.studid);
        pass = findViewById(R.id.pass);
        login = findViewById(R.id.loginbtn);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // do your stuff
        } else {
            signInAnonymously();
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!studid.getText().toString().equals("") && !pass.getText().toString().equals("")) {
                    docRef = null;
                    String str = studid.getText().toString();
                    docRef = db.collection("users").document("" + str);
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                Map<String, Object> data1 = new HashMap<>();
                                String name = "";
                                try {
                                    data1 = document.getData();

                                    for (Map.Entry<String, Object> entry : data1.entrySet()) {
                                        if (entry.getKey().contains("Name")) {
                                            Object nobj = new String();
                                            nobj = entry.getValue();
                                            name = entry.getValue().toString();
                                            String nstr = nobj.toString();
                                            LoginStatus.name = nstr;
                                        }
                                        if (entry.getKey().equals("Password")) {
                                            if (entry.getValue().toString().equals(pass.getText().toString())) {
                                                Log.d("Logged", "Inside");
                                                LoginStatus.loggedin = 1;
                                                LoginStatus.id = str;
                                                Intent intent = new Intent(Login.this, MainActivity.class);
                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(Login.this, "Enter Correct Details", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }

                        }
                    });

                }
            }
        });
    }

    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void goSignup(View view){
        Intent intent = new Intent(Login.this, Register.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}