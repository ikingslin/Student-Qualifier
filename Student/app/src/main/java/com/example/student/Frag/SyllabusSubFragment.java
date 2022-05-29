package com.example.student.Frag;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.student.ItemListener;
import com.example.student.R;
import com.example.student.Adapters.RecyclerSylAdapter;
import com.example.student.Display;
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

public class SyllabusSubFragment extends Fragment {

    FirebaseStorage storage = FirebaseStorage.getInstance();
    static StorageReference getRef;
    StorageReference getData;
    String sempath,convey;
    public SyllabusSubFragment(String str,String convey) {
        sempath=str;
        this.convey = convey;
    }


    private static RecyclerView notify;
    View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /*if(convey.equals("Syllabus")) {
            root = inflater.inflate(R.layout.fragment_listing, container, false);
        }
        else{
            root = inflater.inflate(R.layout.fragment_question, container, false);
        }*/
        root = inflater.inflate(R.layout.fragment_listing, container, false);
        FirebaseAuth.getInstance();
        ArrayList<SingleTextmodel> list= new ArrayList<>();
        try {
            getSyllabus();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        notify = root.findViewById(R.id.recsyl);
        return root;
    }

    public void getSyllabus() {
        if(convey.equals("Syllabus")) {
            getRef = storage.getReference().child("Syllabus/").child(sempath);
        }
        else if(convey.equals("Question")){
            getRef = storage.getReference().child("Question Bank/").child(sempath);
        }


        ArrayList<SingleTextmodel> list = new ArrayList<>();
        HashMap<String, List<String>> get = new HashMap<>();
        getRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference prefix : listResult.getItems()) {
                    list.add(new SingleTextmodel(prefix.getName().replace(".pdf","")));
                    try {
                        ItemListener itemListener = null;
                        RecyclerSylAdapter sylAdapter = new RecyclerSylAdapter(root.getContext(), list);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(root.getContext(), LinearLayoutManager.VERTICAL, false);
                        notify.setLayoutManager(new LinearLayoutManager(root.getContext()));
                        notify.setAdapter(sylAdapter);



                        sylAdapter.setOnItemClickListener(new ItemListener() {
                            @Override
                            public void onItemClick(int pos) {
                                Fragment fragment = null;
                                if(convey.equals("Syllabus")) {
                                    fragment = new Display(sempath,sylAdapter.getItemName(),"Syllabus");
                                }
                                else if(convey.equals("Question")){
                                    fragment = new Display(sempath,sylAdapter.getItemName(),"Question");
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
