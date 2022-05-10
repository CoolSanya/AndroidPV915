package com.example.shop;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shop.account.LoginActivity;
import com.example.shop.account.RegisterActivity;
import com.example.shop.account.UsersActivity;
import com.example.shop.application.HomeApplication;

public class BaseActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
//        menu.setGroupVisible(R.id.group_anonimus, !HomeApplication.getInstance().isAuth());
//        menu.setGroupVisible(R.id.group_auth, HomeApplication.getInstance(). isAuth());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.m_home:
                try {
                    intent = new Intent(BaseActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                catch(Exception ex) {
                    System.out.println("Problem "+ ex.getMessage());
                }
                return true;

            case R.id.m_register:
                try {
                    intent = new Intent(BaseActivity.this, RegisterActivity.class);
                    startActivity(intent);
                    finish();
                }
                catch(Exception ex) {
                    System.out.println("Problem "+ ex.getMessage());
                }
                return true;
            case R.id.m_login:
                try {
                    intent = new Intent(BaseActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                catch(Exception ex) {
                    System.out.println("Problem "+ ex.getMessage());
                }
                return true;
            case R.id.m_users:
                try {
                    intent = new Intent(BaseActivity.this, UsersActivity.class);
                    startActivity(intent);
                    finish();
                }
                catch(Exception ex) {
                    System.out.println("Problem "+ ex.getMessage());
                }
                return true;
            case R.id.m_logout:
                try {
                    HomeApplication.getInstance().deleteToken();
                    intent = new Intent(BaseActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                catch(Exception ex) {
                    System.out.println("Problem "+ ex.getMessage());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
