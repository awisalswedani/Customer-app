package com.mikeail.customerstudio;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.FirebaseDatabase;
import com.mikeail.customerstudio.Models.CustomerModel;

public class AddCustomers extends AppCompatActivity {

    TextInputEditText customerName, customerAge, customerPhone, customerCompany;
    Button addButton;
    MaterialToolbar materialToolbar;
    RadioGroup gender;
    RadioButton selectedRadioButton;
    MaterialAutoCompleteTextView country;
    ArrayAdapter<String> countryAdapter;
    String Gender = "";
    Boolean gen = false;
    private Boolean isCountrySelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_customers);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        materialToolbar = findViewById(R.id.toolBar);
        materialToolbar.setNavigationOnClickListener(view -> {
            finish();
        });


        country = findViewById(R.id.country);
        countryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        country.setAdapter(countryAdapter);

        populateCountries();

        country.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position >= 0 && position < 6){
                    isCountrySelected = true;
                }
            }
        });

        customerName = findViewById(R.id.name);
        customerPhone = findViewById(R.id.phone);
        customerCompany = findViewById(R.id.company);
        customerAge = findViewById(R.id.age);
        gender = findViewById(R.id.radiogroup);
        addButton = findViewById(R.id.add);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customerName.getText().toString().isEmpty()) {
                    customerName.setError("Please Enter Customer Name");
                } else if (customerAge.getText().toString().isEmpty()) {
                    customerAge.setError("Please Enter the Customer Age");
                } else if (customerPhone.getText().toString().isEmpty()) {
                    customerPhone.setError("Please Enter the Customer Phone Number");
                } else if (customerCompany.getText().toString().isEmpty()) {
                    customerCompany.setText("No Company");
                } else if (!isCountrySelected) {
                    country.setError("Select Country First");
                } else if (gender.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "Please select Gender", Toast.LENGTH_SHORT).show();
                    gen = false;
                } else{

                    int selectedId = gender.getCheckedRadioButtonId();
                    selectedRadioButton = (RadioButton) findViewById(selectedId);
                    Gender = selectedRadioButton.getText().toString();
                    gen = true;

                    String get = FirebaseDatabase.getInstance().getReference("Customers").push().getKey();
                    FirebaseDatabase.getInstance().getReference("Customers")
                            .child(get)
                            .setValue(new CustomerModel(get,customerName.getText().toString(), Gender, customerAge.getText().toString(), customerPhone.getText().toString(), customerCompany.getText().toString(), country.getText().toString()))
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(AddCustomers.this, "This Customer has been Added", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                AlertMsg("Error", task.getException().toString());
                            }
                        }
                    });
                }
            }
        });

    }

    private void populateCountries() {
        countryAdapter.add("Bahrain");
        countryAdapter.add("Kuwait");
        countryAdapter.add("Oman");
        countryAdapter.add("Qatar");
        countryAdapter.add("Saudi Arabia");
        countryAdapter.add("UAE");
    }

    public void AlertMsg(String title, String message){
        AlertDialog.Builder alert = new AlertDialog.Builder(AddCustomers.this);
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert.show();
    }
}