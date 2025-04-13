package com.mikeail.customerstudio;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikeail.customerstudio.Models.CustomerModel;

public class CustomerDetails extends AppCompatActivity {

    TextInputEditText customerName, customerGender, customerAge;
    MaterialToolbar materialToolbar;
    String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        materialToolbar = findViewById(R.id.toolBar);
        materialToolbar.setNavigationOnClickListener(view -> {
            finish();
        });

        customerName = findViewById(R.id.name);
        customerGender = findViewById(R.id.gender);
        customerAge = findViewById(R.id.age);
        if (getIntent().hasExtra("KEY")) {
            key = getIntent().getStringExtra("KEY");
        }

        fetchShowCustomer(key);
        changeStatus(false);
    }

    private void fetchShowCustomer(String key) {
        FirebaseDatabase.getInstance().getReference("Customers")
                .child(key)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        CustomerModel model = snapshot.getValue(CustomerModel.class);
                        customerName.setText(model.getCustomerName());
                        customerGender.setText(model.getCustomerGender());
                        customerAge.setText(model.getCustomerAge());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void changeStatus(boolean val) {
        customerName.setClickable(val);
        customerName.setFocusable(val);
        customerAge.setClickable(val);
        customerAge.setFocusable(val);
    }
}