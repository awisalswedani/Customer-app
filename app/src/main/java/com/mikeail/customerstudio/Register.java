package com.mikeail.customerstudio;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.mikeail.customerstudio.Models.UserModel;

public class Register extends AppCompatActivity {

    TextInputEditText name, email, pass, cpass;
    TextView signin;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        cpass = findViewById(R.id.confirmpass);

        signin = findViewById(R.id.signin);
        register = findViewById(R.id.register);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().isEmpty()) {
                    name.setError("Please Enter Name");
                } else if (email.getText().toString().isEmpty()) {
                    email.setError("Please Enter Email");
                } else if (pass.getText().toString().isEmpty()) {
                    pass.setError("Please Enter Password");
                } else if (cpass.getText().toString().isEmpty()) {
                    cpass.setError("Please Enter Password again to Confirm");
                } else if (!pass.getText().toString().equals(cpass.getText().toString())) {
                    pass.setError("Both Passwords must be the same");
                    cpass.setError("Both Passwords must be the same");
                } else {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                saveName(name.getText().toString());
                                saveUser(name.getText().toString(), email.getText().toString(), pass.getText().toString());
                            } else {
                                AlertMsg("Error", task.getException().toString());
                            }
                        }
                    });
                }
            }
        });
    }

    public void saveName(String name) {
        UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.updateProfile(userProfileChangeRequest).addOnCompleteListener(task -> {
            });
        }
    }

    private void saveUser(String name, String email, String password) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference("Users")
                .child(user.getUid())
                .setValue(new UserModel(user.getUid(), name, email, password))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(Register.this, ViewCustomers.class);
                            startActivity(intent);
                            finish();
                        } else {
                            AlertMsg("Error", task.getException().toString());
                        }
                    }
                });
    }

    public void AlertMsg(String title, String message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(Register.this);
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