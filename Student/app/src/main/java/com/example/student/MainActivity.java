package com.example.student;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageButton;
import android.widget.TextView;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    final FirebaseStorage storage = FirebaseStorage.getInstance();
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    DocumentReference docRef;

    private AppBarConfiguration mAppBarConfiguration;
    ImageButton drawImage;
    TextView drawName,drawId;

    Uri disp;
    String[] con;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        try {
            includesForDownloadFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent email = new Intent(Intent.ACTION_SENDTO);
                email.setData(Uri.parse("mailto:maheshwari@drngpasc.ac.in"));

                startActivity(Intent.createChooser(email, "Choose an Email client :"));
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        drawImage = headerView.findViewById(R.id.drawimage);
        drawId = headerView.findViewById(R.id.drawid);
        drawName = headerView.findViewById(R.id.drawname);

        drawName.setText(LoginStatus.name);
        drawId.setText(LoginStatus.id);

        drawImage.setImageURI(disp);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_syllabus, R.id.nav_faculty,R.id.nav_question,R.id.nav_exam,R.id.nav_fee,R.id.nav_feed)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.bringToFront();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        navController.navigate(R.id.nav_home);
                        drawer.close();
                        break;
                    case R.id.nav_syllabus:
                        navController.navigate(R.id.nav_syllabus);
                        drawer.close();
                        break;
                    case R.id.nav_faculty:
                        navController.navigate(R.id.nav_faculty);
                        drawer.close();
                        break;
                    case R.id.nav_question:
                        navController.navigate(R.id.nav_question);
                        drawer.close();
                        break;
                    case R.id.nav_exam:
                        navController.navigate(R.id.nav_exam);
                        drawer.close();
                        break;
                    case R.id.nav_fee:
                        navController.navigate(R.id.nav_fee);
                        drawer.close();
                        break;
                    case R.id.nav_feed:
                        navController.navigate(R.id.nav_feed);
                        drawer.close();
                        break;
                }

                return false;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.action_exit){
            finishAffinity();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }



    public void includesForDownloadFiles() throws IOException {
        StorageReference storageRef = storage.getReference();
        StorageReference pathReference = storageRef.child("DP/"+LoginStatus.id);


        // [START download_to_memory]
        StorageReference islandRef = storageRef.child("DP/"+LoginStatus.id);

        final long ONE_MEGABYTE = 1024 * 1024;
        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                try {
                    File localFile = File.createTempFile("" + LoginStatus.id, "jpg");
                    Bitmap bm = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    drawImage.setImageBitmap(bm);
                }
                catch(Exception e){

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

}