package com.example.shop.account;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.shop.MainActivity;
import com.example.shop.R;
import com.example.shop.account.network.AccountService;
import com.example.shop.account.userscard.UserDTO;
import com.example.shop.account.userscard.UsersAdapter;
import com.example.shop.application.HomeApplication;
import com.example.shop.security.JwtSecurityService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsersActivity extends AppCompatActivity {

    private UsersAdapter adapter;
    private RecyclerView rcvUsers;

    JwtSecurityService jwtService = (JwtSecurityService) HomeApplication.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        rcvUsers = findViewById(R.id.rcvUsers);
        rcvUsers.setHasFixedSize(true);
        rcvUsers.setLayoutManager(new GridLayoutManager(this, 2,
                LinearLayoutManager.VERTICAL, false));

        AccountService.getInstance()
                .jsonApi()
                .users()
                .enqueue(new Callback<List<UserDTO>>() {
                    @Override
                    public void onResponse(Call<List<UserDTO>> call, Response<List<UserDTO>> response) {
                        if(response.isSuccessful())
                        {
                            adapter=new UsersAdapter(response.body());
                            rcvUsers.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<UserDTO>> call, Throwable t) {

                    }
                });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.users, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.m_logout:
                jwtService.deleteToken();
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}