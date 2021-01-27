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

import com.example.readapplication.Adapter.Call_Adapter;
import com.example.readapplication.Object.Call;
import com.example.readapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Call_Fragment extends Fragment {
    private RecyclerView mRecycleView;
    private Call_Adapter callAdapter;

    private DatabaseReference reference;

    private List<Call> callList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_call, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        findView(view);
        initRecycleView();

        callList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("CALL/").child(FirebaseAuth.getInstance().getCurrentUser().getUid() + "/");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                callList.clear();

                for (DataSnapshot snap : snapshot.getChildren()) {
                    Call call = snap.getValue(Call.class);
                    callList.add(call);
                }

                Collections.reverse(callList);

                callAdapter = new Call_Adapter(callList);
                mRecycleView.setAdapter(callAdapter);
                callAdapter.notifyDataSetChanged();
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
        mRecycleView = (RecyclerView) view.findViewById(R.id.CALL_RCV_All_CALL);
    }
}
