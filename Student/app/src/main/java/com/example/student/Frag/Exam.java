package com.example.student.Frag;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.student.Adapters.RecyclerAdapter;
import com.example.student.Adapters.RecyclerSylAdapter;
import com.example.student.Models.SingleTextmodel;
import com.example.student.Models.TwoTextModel;
import com.example.student.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Exam extends Fragment {

    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference docRef;
    TextView first,second,third,fourth, fifth,sixth,day;
    CollectionReference colRef;

    private ArrayList<TwoTextModel> twoTextModelArrayList;
    private ArrayList<SingleTextmodel> oneTextModelArrayList;
    private static RecyclerView examcontext,examtitle,exammain;

    View root;
    public Exam() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_exam, container, false);
        FirebaseAuth.getInstance();

        examcontext = root.findViewById(R.id.recexam);
        examtitle = root.findViewById(R.id.recexamtitle);
        //exammain = root.findViewById(R.id.recexammain);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        try {
            getnot();

        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onActivityCreated(savedInstanceState);
    }

    public void getnot() throws IOException {
        ArrayList<TwoTextModel> contextModel = new ArrayList<>();
        ArrayList<SingleTextmodel> titleModel = new ArrayList<>();
        colRef = db.collection("Examination");
        colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    RecyclerSylAdapter notiAdapter;
                    int i=0;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> data1 = new HashMap<>();
                        try {
                            String tstr = document.getId();
                            if (!tstr.isEmpty()) {
                                titleModel.add(new SingleTextmodel(tstr));
                                notiAdapter = new RecyclerSylAdapter(root.getContext(), titleModel);
                                examtitle.setLayoutManager(new LinearLayoutManager(root.getContext()));
                                examtitle.setAdapter(notiAdapter);
                            }
                            data1 = document.getData();
                            String  cstr = "", dstr = "";
                            int j=0;
                            RecyclerAdapter subAdapter;
                            for (Map.Entry<String, Object> entry : data1.entrySet()) {
                                /*if (entry.getKey().contains("Content") || entry.getKey().contains("Title")) {
                                    i++;
                                    Object nobj = new String();
                                    if (entry.getKey().contains("Content")) {
                                        nobj = entry.getValue();
                                        cstr = nobj.toString();
                                    }
                                    if (entry.getKey().contains("Title")) {
                                        nobj = entry.getValue();
                                        tstr = nobj.toString();
                                    }*/

                                Object sobj = new String();
                                Object dobj = new String();
                                sobj = entry.getKey();
                                dobj = entry.getValue();
                                cstr = sobj.toString();
                                dstr = dobj.toString();
                                if (!cstr.isEmpty() &&!dstr.isEmpty()) {
                                    contextModel.add(new TwoTextModel( cstr, dstr));
                                    subAdapter = new RecyclerAdapter(root.getContext(), contextModel,"Exam");
                                    examcontext.setLayoutManager(new LinearLayoutManager(root.getContext()));
                                    examcontext.setAdapter(subAdapter);
                                }
                            }

                        }
                        catch(Exception e){

                        }
                    }
                } else {
                    Log.d("implem", "Error getting documents: ", task.getException());
                }
            }
        });
    }
}
