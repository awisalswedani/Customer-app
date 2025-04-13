package com.mikeail.customerstudio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikeail.customerstudio.Adapter.CustomerRecyclerViewAdapter;
import com.mikeail.customerstudio.Models.CustomerModel;

import java.util.ArrayList;

public class ViewCustomers extends AppCompatActivity {

    RecyclerView recyclerView;
    CustomerRecyclerViewAdapter recyclerViewAdapter;
    ArrayList<CustomerModel> arrayList;
    ExtendedFloatingActionButton floatingbtn;
    SwipeRefreshLayout swipeRefresh;
    public static Boolean isAdmin = false;
    MaterialAutoCompleteTextView country, gender;
    ArrayAdapter<String> countryAdapter, genderAdapter;
    LinearLayout linear, lineara;
    Button clearFilters;
    MaterialToolbar materialToolbar;

    // Request code for starting the EditActivity
    private static final int REQUEST_CODE_EDIT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_customers);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        materialToolbar = findViewById(R.id.toolBar);
        materialToolbar.setNavigationOnClickListener(view -> {
            finish();
        });

        if (getIntent().hasExtra("ADMIN")) {
            isAdmin = getIntent().getBooleanExtra("ADMIN", false);
        }

        recyclerView = findViewById(R.id.recyclerView);
        arrayList = new ArrayList<>();
        floatingbtn = findViewById(R.id.floatingbtn);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        linear = findViewById(R.id.linear2);
        lineara = findViewById(R.id.lineara);
        clearFilters = findViewById(R.id.clear);

        if (isAdmin) {
            floatingbtn.setVisibility(View.VISIBLE);
        }

        country = findViewById(R.id.country);
        countryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        country.setAdapter(countryAdapter);

        gender = findViewById(R.id.gender);
        genderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        gender.setAdapter(genderAdapter);

        populateData();

        country.setOnItemClickListener((adapterView, view, i, l) -> {
            linear.setVisibility(View.VISIBLE);
            showSpecificCustomers(country.getText().toString(), gender.getText().toString());
        });

        gender.setOnItemClickListener((adapterView, view, i, l) -> {
            linear.setVisibility(View.VISIBLE);
            showSpecificCustomers(country.getText().toString(), gender.getText().toString());
        });

        floatingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewCustomers.this, AddCustomers.class));
            }
        });

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                arrayList.clear();
                if (!country.getText().toString().isEmpty() && !gender.getText().toString().isEmpty()) {
                    showSpecificCustomers(country.getText().toString(), gender.getText().toString());
                    swipeRefresh.setRefreshing(false);
                } else {
                    fetchCustomers();
                    swipeRefresh.setRefreshing(false);
                }
            }
        });

        fetchCustomers();

        clearFilters.setOnClickListener(view -> {
            country.setText("");
            gender.setText("");
            populateData();
            linear.setVisibility(View.GONE);
            fetchCustomers();
        });
    }

    private void showSpecificCustomers(String con, String gen) {
        arrayList.clear();
        FirebaseDatabase.getInstance().getReference("Customers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    CustomerModel customer = snap.getValue(CustomerModel.class);
                    if ((con.isEmpty() || customer.getCustomerCountry().equals(con)) &&
                            (gen.isEmpty() || customer.getCustomerGender().equals(gen))) {
                        arrayList.add(customer);
                    }
                }
                showAll(arrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void populateData() {
        countryAdapter.clear();
        genderAdapter.clear();

        countryAdapter.add("Bahrain");
        countryAdapter.add("Kuwait");
        countryAdapter.add("Oman");
        countryAdapter.add("Qatar");
        countryAdapter.add("Saudi Arabia");
        countryAdapter.add("UAE");

        genderAdapter.add("Male");
        genderAdapter.add("Female");
    }

    public void showAll(ArrayList<CustomerModel> arrayList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ViewCustomers.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerViewAdapter = new CustomerRecyclerViewAdapter(ViewCustomers.this, arrayList, isAdmin);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private void fetchCustomers() {
        arrayList.clear();
        FirebaseDatabase.getInstance().getReference("Customers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    arrayList.add(snap.getValue(CustomerModel.class));
                }
                showAll(arrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_EDIT && resultCode == RESULT_OK) {
            recyclerViewAdapter.notifyDataSetChanged();
        }

    }
}