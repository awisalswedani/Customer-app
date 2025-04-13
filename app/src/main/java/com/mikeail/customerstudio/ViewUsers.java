package com.mikeail.customerstudio;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikeail.customerstudio.Adapter.UserRecyclerViewAdapter;
import com.mikeail.customerstudio.Models.UserModel;

import java.util.ArrayList;

public class ViewUsers extends AppCompatActivity {

    RecyclerView recyclerView;
    UserRecyclerViewAdapter recyclerViewAdapter;
    ArrayList<UserModel> arrayList;
    SwipeRefreshLayout swipeRefresh;
    MaterialToolbar materialToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_users);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        materialToolbar = findViewById(R.id.toolBar);
        materialToolbar.setNavigationOnClickListener(view -> {
            finish();
        });

        recyclerView = findViewById(R.id.recyclerView);
        arrayList = new ArrayList<>();
        swipeRefresh = findViewById(R.id.swipeRefresh);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                arrayList.clear();
                fetchUsers();
                swipeRefresh.setRefreshing(false);
            }
        });

        fetchUsers();
    }

    public void showAll(ArrayList<UserModel> arrayList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ViewUsers.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerViewAdapter = new UserRecyclerViewAdapter(ViewUsers.this, arrayList);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private void fetchUsers() {
        FirebaseDatabase.getInstance().getReference("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    arrayList.add(snap.getValue(UserModel.class));
                }
                showAll(arrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}