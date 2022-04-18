package com.example.shop.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.example.shop.R;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shop.MainActivity;
import com.example.shop.account.dto.AccountResponseDTO;
import com.example.shop.account.dto.LoginDTO;
import com.example.shop.account.dto.ValidationRegisterDTO;
import com.example.shop.account.network.AccountService;
import com.example.shop.application.HomeApplication;
import com.example.shop.security.JwtSecurityService;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private TextView tvInfo;
    private TextInputLayout textFieldEmail;
    private TextInputEditText txtEmail;

    private TextInputLayout textFieldPassword;
    private TextInputEditText txtPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tvInfo = findViewById(R.id.tvInfo);
        textFieldEmail = findViewById(R.id.textFieldEmail);
        txtEmail = findViewById(R.id.txtEmail);

        textFieldPassword = findViewById(R.id.textFieldPassword);
        txtPassword = findViewById(R.id.txtPassword);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.m_register:
                intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                return true;
            case R.id.m_users:
                intent = new Intent(this, UsersActivity.class);
                startActivity(intent);
                return true;
//            case R.id.m_login:
//                intent = new Intent(this, MainActivity.class);
//                startActivity(intent);
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void handleClick(View view) {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail(txtEmail.getText().toString());
        loginDTO.setPassword(txtPassword.getText().toString());

        if (!validationFields(loginDTO))
            return;
        AccountService.getInstance()
                .jsonApi()
                .login(loginDTO)
                .enqueue(new Callback<AccountResponseDTO>() {
                    @Override
                    public void onResponse(Call<AccountResponseDTO> call, Response<AccountResponseDTO> response) {
                        if (response.isSuccessful()) {
                            AccountResponseDTO data = response.body();

                            JwtSecurityService jwtService = (JwtSecurityService) HomeApplication.getInstance();
                            jwtService.saveJwtToken(data.getToken());

                            //tvInfo.setText("response is good");
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            try {
                                showErrorsServer(response.errorBody().string());
                            } catch (Exception e) {
                                System.out.println("------Error response parse body-----");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AccountResponseDTO> call, Throwable t) {
                        String str = t.toString();
                        int a = 12;
                    }
                });
    }

    private boolean validationFields(LoginDTO loginDTO) {
        textFieldEmail.setError("");
        if (loginDTO.getEmail().equals("")) {
            textFieldEmail.setError("Вкажіть пошту");
            return false;
        }

        textFieldPassword.setError("");
        if (loginDTO.getPassword().equals("")) {
            textFieldPassword.setError("Вкажіть пароль");
            return false;
        }

        return true;
    }

    private void showErrorsServer(String json) {
        Gson gson = new Gson();
        ValidationRegisterDTO result = gson.fromJson(json, ValidationRegisterDTO.class);
        String str = "";
        if (result.getErrors().getEmail() != null) {
            for (String item : result.getErrors().getEmail()) {
                str += item + "\n";
            }
        }
        textFieldEmail.setError(str);

        str = "";
        if (result.getErrors().getFirstName() != null) {
            for (String item : result.getErrors().getFirstName()) {
                str += item + "\n";
            }
        }
        textFieldPassword.setError(str);
    }
}
