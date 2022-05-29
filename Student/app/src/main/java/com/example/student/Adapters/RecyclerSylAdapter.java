package com.example.student.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.student.ItemListener;
import com.example.student.Models.SingleTextmodel;
import com.example.student.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

    public class RecyclerSylAdapter extends RecyclerView.Adapter<RecyclerSylAdapter.Viewhold> {

        private Context context;
        private ArrayList<SingleTextmodel> sylModelArrayList;
        ItemListener itemListener;
        public int Itemposition;

        // Constructor
        public RecyclerSylAdapter(Context context, ArrayList<SingleTextmodel> notiModelArray) {
            this.context = context;
            this.sylModelArrayList = notiModelArray;
        }

        @NonNull
        @Override
        public RecyclerSylAdapter.Viewhold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // to inflate the layout for each item of recycler view.
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.syl_list_group, parent, false);
            return new RecyclerSylAdapter.Viewhold(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Viewhold holder, int position) {
            SingleTextmodel model = sylModelArrayList.get(position);
            holder.title.setText(model.gettitle());
            holder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Itemposition = position;
                    itemListener.onItemClick(position);
                }
            });
        }

        public String getItemName(){
            String name;
            name = sylModelArrayList.get(Itemposition).gettitle();
            return name;
        }

        @Override
        public int getItemCount() {
            // this method is used for showing number
            // of card items in recycler view.
            return sylModelArrayList.size();
        }

        public void setOnItemClickListener(ItemListener itemListener){
            this.itemListener = itemListener;
        }

        public class Viewhold extends RecyclerView.ViewHolder {
            private final TextView title;

            public Viewhold(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.sylName);
            }
        }
    }

