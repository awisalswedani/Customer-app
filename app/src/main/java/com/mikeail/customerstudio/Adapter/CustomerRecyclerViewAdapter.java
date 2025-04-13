package com.mikeail.customerstudio.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mikeail.customerstudio.CustomerDetails;
import com.mikeail.customerstudio.EditCustomers;
import com.mikeail.customerstudio.Models.CustomerModel;
import com.mikeail.customerstudio.R;

import java.util.ArrayList;

public class CustomerRecyclerViewAdapter extends RecyclerView.Adapter<CustomerRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private ArrayList<CustomerModel> arrayList;
    private Boolean isAdmin;

    public CustomerRecyclerViewAdapter(Context context, ArrayList<CustomerModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    public CustomerRecyclerViewAdapter(Context context, ArrayList<CustomerModel> arrayList, Boolean isAdmin) {
        this.context = context;
        this.arrayList = arrayList;
        this.isAdmin = isAdmin;
    }

    @NonNull
    @Override
    public CustomerRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.customer_data, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.name.setText(arrayList.get(position).getCustomerName());
        holder.age.setText(arrayList.get(position).getCustomerAge());
        holder.gender.setText(arrayList.get(position).getCustomerGender());
        holder.country.setText(arrayList.get(position).getCustomerCountry());
        holder.phone.setText(arrayList.get(position).getCustomerPhone());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, age, gender, country, phone;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            age = itemView.findViewById(R.id.age);
            gender = itemView.findViewById(R.id.gender);
            country = itemView.findViewById(R.id.country);
            phone = itemView.findViewById(R.id.phone);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isAdmin){
                        Intent intent = new Intent(context, EditCustomers.class);
                        intent.putExtra("KEY", arrayList.get(getAdapterPosition()).getCustomerKey());
                        context.startActivity(intent);
                    }
                    else{
                        Intent intent = new Intent(context, CustomerDetails.class);
                        intent.putExtra("KEY", arrayList.get(getAdapterPosition()).getCustomerKey());
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}
