package com.example.student.Frag;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.student.LoginStatus;
import com.example.student.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Feed extends Fragment {


    EditText feedback;
    Button subfeed;
    FirebaseFirestore db= FirebaseFirestore.getInstance();
    public Feed() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_feed, container, false);

        feedback = root.findViewById(R.id.feedView);
        subfeed = root.findViewById(R.id.feedSubmitbtn);



        subfeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalDate time = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    time = LocalDate.now();
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE;
                    dateTimeFormatter.format(time);
                }
                Map<String, Object> feed = new HashMap<>();
                feed.put(LoginStatus.name,feedback.getText().toString());
                String date = String.valueOf(time).replace("-","_");
                db.collection("Feed Back").document(date).set(feed).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(root.getContext(),"FeedBack Submitted",Toast.LENGTH_LONG).show();
                        Fragment fragment = new HomeFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.add(fragment,null);
                        fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                });
            }
        });

        return root;
    }
}
