package com.example.shop.account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.shop.R;
import com.example.shop.account.dto.AccountResponseDTO;
import com.example.shop.account.dto.RegisterDTO;
import com.example.shop.account.dto.ValidationRegisterDTO;
import com.example.shop.account.network.AccountService;
import com.example.shop.application.HomeApplication;
import com.example.shop.security.JwtSecurityService;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText txtEmail;
    EditText txtFirstName;
    EditText txtSecondName;
    ImageView IVPreviewImage;
    EditText txtPhone;
    EditText txtPassword;
    EditText txtConfirmPassword;

    int SELECT_PICTURE = 200;

    String stringImgB64;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        txtEmail = findViewById(R.id.txtEmail);
        txtFirstName = findViewById(R.id.txtFirstName);
        txtSecondName = findViewById(R.id.txtSecondName);
        txtPhone = findViewById(R.id.txtPhone);
        IVPreviewImage = findViewById(R.id.txtViewPhoto);
        txtPassword = findViewById(R.id.txtPassword);
        txtConfirmPassword = findViewById(R.id.txtConfirmPassword);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
//            case R.id.m_register:
//                intent = new Intent(this, RegisterActivity.class);
//                startActivity(intent);
//                return true;
            case R.id.m_login:
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void selectImageClick(View view) {
        //txtFieldEmail.setError("Маємо проблему");

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                Uri uri = data.getData();
                if (null != uri) {
                    Bitmap bitmap= null;
                    IVPreviewImage.setImageURI(uri);
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // initialize byte stream
                    ByteArrayOutputStream stream=new ByteArrayOutputStream();
                    // compress Bitmap
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                    // Initialize byte array
                    byte[] bytes=stream.toByteArray();
                    // get base64 encoded string
                    stringImgB64 = Base64.encodeToString(bytes,Base64.DEFAULT);
                    //String app=sImage+"";
                }
            }
        }
    }

    public void handleClick(View view) {
        //textFieldEmail.setError("Вкажіть пошту");

        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setEmail(txtEmail.getText().toString());
        registerDTO.setFirstName(txtFirstName.getText().toString());
        registerDTO.setSecondName(txtSecondName.getText().toString());
        registerDTO.setPhoto(stringImgB64);
        registerDTO.setPhone(txtPhone.getText().toString());
        registerDTO.setPassword(txtPassword.getText().toString());
        registerDTO.setConfirmPassword(txtConfirmPassword.getText().toString());

        AccountService.getInstance()
                .jsonApi()
                .register(registerDTO)
                .enqueue(new Callback<AccountResponseDTO>() {
                    @Override
                    public void onResponse(Call<AccountResponseDTO> call, Response<AccountResponseDTO> response) {
                        if (response.isSuccessful())
                        {
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }else {
                            try {
                                showErrorsServer(response.errorBody().string());
                            } catch (Exception e) {
                                System.out.println("------Error response parse body-----");
                            }
                        }

//                        tvInfo.setText("response is good");
                    }

                    @Override
                    public void onFailure(Call<AccountResponseDTO> call, Throwable t) {
                        String str = t.toString();
                        int a =12;
                    }
                });
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
        txtEmail.setError(str);
    }


}