package com.example.student.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.student.Models.TwoTextModel;
import com.example.student.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.Viewholder> {

    private Context context;
    private ArrayList<TwoTextModel> twoTextModelArrayList;
    private String convey;
    public RecyclerAdapter(Context context, ArrayList<TwoTextModel> twoTextModelArray,String convey) {
        this.context = context;
        this.twoTextModelArrayList = twoTextModelArray;
        this.convey = convey;
    }

    @NonNull
    @Override
    public RecyclerAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(convey.equals("Exam")) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exam_card, parent, false);
            return new Viewholder(view);
        }
        if(convey.equals("Notification")){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_cardview, parent, false);
            return new Viewholder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.Viewholder holder, int position) {
        TwoTextModel model = twoTextModelArrayList.get(position);
        holder.title.setText(model.gettitle());
        holder.content.setText(model.getcontent());
    }

    @Override
    public int getItemCount() {
        return twoTextModelArrayList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView content;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.Title);
            content = itemView.findViewById(R.id.Content);
        }
    }
}