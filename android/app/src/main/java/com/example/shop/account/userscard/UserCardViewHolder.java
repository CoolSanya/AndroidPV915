package com.example.shop.account.userscard;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.shop.R;

public class UserCardViewHolder extends RecyclerView.ViewHolder {
    private View view;
    public ImageView userimg;
    public TextView useremail;
    public Button btnEdit;
    public UserCardViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view=itemView;
        userimg=itemView.findViewById(R.id.userimg);
        useremail=itemView.findViewById(R.id.useremail);
        btnEdit = itemView.findViewById(R.id.btnEdit);
    }

    public View getView() {
        return view;
    }
}
