package com.mikeail.customerstudio;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikeail.customerstudio.Models.CustomerModel;

public class EditCustomers extends AppCompatActivity {

    TextInputEditText customerName, customerGender, customerAge, customerPhone, customerCompany;
    MaterialAutoCompleteTextView customerCountry;
    ArrayAdapter<String> countryAdapter;
    Boolean edi = false;
    Button btnedit, btndelete;
    MaterialToolbar materialToolbar;
    String key;
    FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_customers);
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
        customerPhone = findViewById(R.id.phone);
        customerCompany = findViewById(R.id.company);

        customerCountry = findViewById(R.id.country);
        countryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        customerCountry.setAdapter(countryAdapter);

        populateCountries();

        btnedit = findViewById(R.id.editbtn);
        btndelete = findViewById(R.id.deletebtn);

        changeStatus(false);
        db = FirebaseDatabase.getInstance();

        if (getIntent().hasExtra("KEY")) {
            key = getIntent().getStringExtra("KEY");
        }

        fetchShowCustomer(key);

        btnedit.setOnClickListener(view -> {
            if (!edi) {
                changeStatus(true);
                btnedit.setText("Update");
                edi = true;
            } else if (edi) {
                new AlertDialog.Builder(EditCustomers.this)
                        .setTitle("Notification")
                        .setMessage("Are you sure you want to Update this ?")
                        .setPositiveButton("Update", (dialogInterface, i) -> {
                            db.getReference("Customers").child(key).setValue(new CustomerModel(key, customerName.getText().toString(), customerGender.getText().toString(), customerAge.getText().toString(), customerPhone.getText().toString(), customerCompany.getText().toString(), customerCountry.getText().toString())).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        changeStatus(false);
                                        edi = false;
                                        btnedit.setText("Edit");
                                        setResult(RESULT_OK);
                                        finish();
                                        Toast.makeText(EditCustomers.this, "Customer Data Successfully Updated", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        })
                        .setNegativeButton("Cancel", (dialogInterface, i) -> {
                            changeStatus(false);
                        })
                        .show();

                new Handler().postDelayed(() -> {
                    changeStatus(false);
                    edi = false;
                    btnedit.setText("Edit");
                }, 10000);
            }
        });

        btndelete.setOnClickListener(view -> {
            new AlertDialog.Builder(EditCustomers.this)
                    .setTitle("Notitfication")
                    .setMessage("Are you sure you want to Delete this ?")
                    .setPositiveButton("Delete", (dialogInterface, i) -> {
                        db.getReference("Customers").child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(EditCustomers.this, "Customer has been Deleted", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK);
                                finish();

                            }
                        });
                    })
                    .setNegativeButton("Cancel", (dialogInterface, i) -> {
                    })
                    .show();

            changeStatus(false);
        });
    }

    private void populateCountries() {
        countryAdapter.clear();
        countryAdapter.add("Bahrain");
        countryAdapter.add("Kuwait");
        countryAdapter.add("Oman");
        countryAdapter.add("Qatar");
        countryAdapter.add("Saudi Arabia");
        countryAdapter.add("UAE");
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
                        customerPhone.setText(model.getCustomerPhone());
                        customerCompany.setText(model.getCustomerCompany());

                        String selectedCountry = model.getCustomerCountry();
                        int position = countryAdapter.getPosition(selectedCountry);
                        if (position != -1) {
                            customerCountry.setText(countryAdapter.getItem(position), false);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void changeStatus(boolean val) {
        customerName.setEnabled(val);
        customerAge.setEnabled(val);
        customerPhone.setEnabled(val);
        customerCompany.setEnabled(val);
        customerCountry.setEnabled(val);
    }
}