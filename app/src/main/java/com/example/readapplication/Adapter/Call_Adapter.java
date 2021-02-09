package com.example.readapplication.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readapplication.Object.Call;

import com.example.readapplication.R;

import java.util.List;

public class Call_Adapter extends RecyclerView.Adapter<Call_Adapter.ViewHolderList> {
    List<Call> callList;

    public Call_Adapter(List<Call> callList) {
        this.callList = callList;
    }

    @NonNull
    @Override
    public Call_Adapter.ViewHolderList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.call_item, parent, false);
        final Call_Adapter.ViewHolderList viewHolder = new Call_Adapter.ViewHolderList(view);

        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull Call_Adapter.ViewHolderList holder, int position) {
        holder.call_LBL_from.setText(callList.get(position).getCallNumber());
        holder.call_LBL_time.setText(callList.get(position).getCallStartTime());
        holder.call_LBL_duration.setText(callList.get(position).getDuration());

        if (callList.get(position).isExist()) {
            holder.call_LBL_exists.setText("YES " + " ( " + callList.get(position).getCallName() + " ) ");
        } else {
            holder.call_LBL_exists.setText("NO");
        }
    }

    @Override
    public int getItemCount() {
        return callList.size();
    }

    public class ViewHolderList extends RecyclerView.ViewHolder {
        private TextView call_LBL_from;
        private TextView call_LBL_exists;
        private TextView call_LBL_time;
        private TextView call_LBL_duration;

        public ViewHolderList(@NonNull View itemView) {
            super(itemView);

            call_LBL_from = itemView.findViewById(R.id.Call_LBL_From);
            call_LBL_exists = itemView.findViewById(R.id.Call_LBL_Exists);
            call_LBL_time = itemView.findViewById(R.id.Call_LBL_Time);
            call_LBL_duration = itemView.findViewById(R.id.Call_LBL_Duration);
        }
    }
}
