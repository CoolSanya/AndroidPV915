package com.example.shop.account.userscard;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.shop.R;

public class UserCardViewHolder extends RecyclerView.ViewHolder {
    private View view;
    public ImageView userimg;
    public TextView useremail;
    public UserCardViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view=itemView;
        userimg=itemView.findViewById(R.id.userimg);
        useremail=itemView.findViewById(R.id.useremail);
    }

    public View getView() {
        return view;
    }
}
