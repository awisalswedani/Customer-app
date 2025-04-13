package com.mikeail.customerstudio;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText email, pass;
    Button login;
    TextView register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);

        register.setOnClickListener(view -> changeClass(Login.this, Register.class));

        login.setOnClickListener(view -> {
//                progressDialog.show();
            if (email.getText().toString().isEmpty()) {
                email.setError("Please Enter Email Address");
            } else if (pass.getText().toString().isEmpty()) {
                pass.setError("Please Enter Password");
            } else {
                String e = email.getText().toString();
                String p = pass.getText().toString();
                if(e.toLowerCase().equals("admin") && p.equals("123456")){
                    startActivity(new Intent(Login.this, DashboardAdmin.class));
                }
                else{
                    checkLogin(e, p);
                }
            }
        });
    }
    public static void changeClass(Context context, Class<?> intentClass) {
        Intent intent = new Intent(context, intentClass);
        context.startActivity(intent);
    }

    private void checkLogin(String email, String pass) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    changeClass(Login.this, ViewCustomers.class);
                } else {
                    Toast.makeText(Login.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}