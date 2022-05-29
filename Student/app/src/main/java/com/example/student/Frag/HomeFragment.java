package com.example.student.Frag;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.student.Models.TwoTextModel;
import com.example.student.R;
import com.example.student.Adapters.RecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class HomeFragment extends Fragment {
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference docRef;
    TextView first,second,third,fourth, fifth,sixth,day;
    CollectionReference colRef;

    private static ArrayList<TwoTextModel> twoTextModelArrayList;
    private static RecyclerView notify;

    View root;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);


        first = root.findViewById(R.id.firstp);
        second = root.findViewById(R.id.secondp);
        third = root.findViewById(R.id.thirdp);
        fourth = root.findViewById(R.id.fourthp);
        fifth = root.findViewById(R.id.fifthp);
        sixth = root.findViewById(R.id.sixthp);
        day = root.findViewById(R.id.dayoftime);
        notify = root.findViewById(R.id.rechome);
        FirebaseAuth.getInstance();





        Calendar cal = Calendar.getInstance();
        String str = cal.getDisplayName(Calendar.DAY_OF_WEEK,Calendar.LONG, Locale.getDefault());
        docRef = db.collection("TimeTable").document(""+ str);
        day.setText(str);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                Map<String, Object> data1 = new HashMap<>();
                try {
                    data1 = document.getData();

                    for (Map.Entry<String, Object> entry : data1.entrySet()) {
                        switch (entry.getKey().toString()){
                            case "First":
                                first.setText(entry.getValue().toString());
                                break;
                            case "Second":
                                second.setText(entry.getValue().toString());
                                break;
                            case "Third":
                                third.setText(entry.getValue().toString());
                                break;
                            case "Fourth":
                                fourth.setText(entry.getValue().toString());
                                break;
                            case "Fifth":
                                fifth.setText(entry.getValue().toString());
                                break;
                            case "Sixth":
                                sixth.setText(entry.getValue().toString());
                                break;
                            default:
                                break;
                        }

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return root;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayList<TwoTextModel> send= new ArrayList<>();
        try {
            send = getnot();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<TwoTextModel> getnot() throws IOException {
        ArrayList<TwoTextModel> inget = new ArrayList<>();
        colRef = db.collection("info");
        colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> data1 = new HashMap<>();
                        try {
                            data1 = document.getData();
                            int i = 0;
                            String cstr = "", tstr = "";
                            for (Map.Entry<String, Object> entry : data1.entrySet()) {
                                if (entry.getKey().contains("Content") || entry.getKey().contains("Title")) {
                                    i++;
                                    Object nobj = new String();
                                    if (entry.getKey().contains("Content")) {
                                        nobj = entry.getValue();
                                        cstr = nobj.toString();
                                    }
                                    if (entry.getKey().contains("Title")) {
                                        nobj = entry.getValue();
                                        tstr = nobj.toString();
                                    }

                                    if (!cstr.isEmpty() && !tstr.isEmpty()) {
                                        inget.add(new TwoTextModel(tstr, cstr));
                                        RecyclerAdapter notiAdapter = new RecyclerAdapter(root.getContext(), inget,"Notification");
                                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(root.getContext(), LinearLayoutManager.VERTICAL, false);

                                        notify.setLayoutManager(new LinearLayoutManager(root.getContext()));
                                        notify.setAdapter(notiAdapter);
                                    }

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
        return inget;
    }

}