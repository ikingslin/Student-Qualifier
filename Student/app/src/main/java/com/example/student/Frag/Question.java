package com.example.student.Frag;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.student.ItemListener;
import com.example.student.R;
import com.example.student.Adapters.RecyclerSylAdapter;
import com.example.student.Models.SingleTextmodel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Question extends Fragment {
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference getRef;
    String semq ="";
    String conveyq = "";
    static String path = "";
    public Question(String q,String con) {
            semq = q;
            conveyq = con;
    }
    public Question(){

    }

    private RecyclerView notify;
    View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            root = inflater.inflate(R.layout.fragment_question, container, false);


        FirebaseAuth.getInstance();
        try {
            getSyllabus();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        notify = root.findViewById(R.id.recques);
        return root;
    }

    public void getSyllabus() {
        if(conveyq.equals("Inner")) {
            getRef = storage.getReference().child("Question Bank/").child(semq+"/");
        }
        else{
            getRef = storage.getReference().child("Question Bank/");
        }

        ArrayList<SingleTextmodel> list = new ArrayList<>();
        HashMap<String, List<String>> get = new HashMap<>();
        getRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference prefix : listResult.getPrefixes()) {
                    list.add(new SingleTextmodel(prefix.getName()));
                    try {
                        ItemListener itemListener = null;
                        RecyclerSylAdapter sylAdapter = new RecyclerSylAdapter(root.getContext(), list);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(root.getContext(), LinearLayoutManager.VERTICAL, false);
                        notify.setLayoutManager(new LinearLayoutManager(root.getContext()));
                        notify.setAdapter(sylAdapter);

                        sylAdapter.setOnItemClickListener(new ItemListener() {
                            @Override
                            public void onItemClick(int pos) {

                                Fragment fragment;
                                if(conveyq.equals("Inner")){
                                    fragment = new SyllabusSubFragment(path+sylAdapter.getItemName(),"Question");
                                }
                                else{
                                    path = sylAdapter.getItemName()+"/";
                                    fragment = new Question(sylAdapter.getItemName(),"Inner");

                                }
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.add(fragment,null);
                                fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                            }
                        });

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Karen", "Failed");
            }
        });
    }
}

