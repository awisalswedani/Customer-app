package com.mikeail.customerstudio.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.mikeail.customerstudio.Models.UserModel;
import com.mikeail.customerstudio.R;

import java.util.ArrayList;

public class UserRecyclerViewAdapter extends RecyclerView.Adapter<UserRecyclerViewAdapter.ViewHolder>{
    private Context context;
    private ArrayList<UserModel> arrayList;

    public UserRecyclerViewAdapter(Context context, ArrayList<UserModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_data, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.userName.setText(arrayList.get(position).getUserName());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        MaterialCardView obj;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.name);
            obj = itemView.findViewById(R.id.obj);
            obj.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(context)
                            .setTitle("Notitfication")
                            .setMessage("Are you sure you want to Delete this ?")
                            .setPositiveButton("Delete", (dialogInterface, i) -> {
                                deleteUserAccount(arrayList.get(getAdapterPosition()).getUserKey(), arrayList.get(getAdapterPosition()).getUserEmail(), arrayList.get(getAdapterPosition()).getUserPass());
                            })
                            .setNegativeButton("Cancel", (dialogInterface, i) -> {
                            })
                            .show();
                }
            });
        }
    }
    private void deleteUserAccount(String userKey, String email, String password){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseAuth.getInstance().getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                deleteUserData(userKey);
                            }
                        }
                    });
                }
            }
        });
    }

    private void deleteUserData(String userKey){
        FirebaseDatabase.getInstance().getReference("Users").child(userKey)
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context, "User has been Deleted", Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                    }
                });
    }
}
