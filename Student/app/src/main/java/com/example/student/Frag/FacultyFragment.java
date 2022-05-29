package com.example.student.Frag;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.student.ItemListener;
import com.example.student.R;
import com.example.student.Adapters.RecyclerSylAdapter;
import com.example.student.SyllabusDisplay;
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

public class FacultyFragment extends Fragment {
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference getRef;
    StorageReference getData;
    List<String> clist = new ArrayList<>();
    public FacultyFragment() {
        // Required empty public constructor
    }


    private static RecyclerView notify;
    View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_faculty, container, false);
        FirebaseAuth.getInstance();
        ArrayList<SingleTextmodel> list= new ArrayList<>();
        try {
            getFaculty();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        notify = root.findViewById(R.id.recfac);
        return root;
    }

    public void getFaculty() {
        getRef = storage.getReference().child("Faculty/");

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
                                Fragment fragment = new SyllabusDisplay("Faculty",sylAdapter.getItemName());
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


                //send(new ExpModel(list, inner));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Karen", "Failed");
            }
        });

        /*try {
            for (int i = 0; i < list.length; i++) {
                try {
                    expandableListDetail.put(list[i], inner[i]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/

    }
}

