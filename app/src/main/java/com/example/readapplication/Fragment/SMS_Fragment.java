package com.example.readapplication.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readapplication.Object.Message;
import com.example.readapplication.R;
import com.example.readapplication.Adapter.SMS_Adapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SMS_Fragment extends Fragment {
    private RecyclerView mRecycleView;
    private SMS_Adapter smsAdapter;

    private DatabaseReference reference;

    private List<Message> smsList;

    public SMS_Fragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sms, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        findView(view);
        initRecycleView();

        smsList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("SMS/").child(FirebaseAuth.getInstance().getCurrentUser().getUid() + "/");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                smsList.clear();

                for(DataSnapshot snap: snapshot.getChildren()) {
                    Message sms = snap.getValue(Message.class);
                    smsList.add(sms);
                }

                Collections.reverse(smsList);

                smsAdapter = new SMS_Adapter(smsList);
                mRecycleView.setAdapter(smsAdapter);
                smsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void initRecycleView() {
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void findView(View view) {
        mRecycleView = (RecyclerView) view.findViewById(R.id.SMS_RCV_All_SMS);
    }
}
