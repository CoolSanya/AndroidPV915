package com.example.shop.account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.shop.R;
import com.example.shop.account.dto.AccountResponseDTO;
import com.example.shop.account.dto.EditUserDTO;
import com.example.shop.account.dto.RegisterDTO;
import com.example.shop.account.dto.ValidationRegisterDTO;
import com.example.shop.account.network.AccountService;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditUserActivity extends AppCompatActivity {
    EditText txtFirstName;
    EditText txtSecondName;
    ImageView IVPreviewImage;
    EditText txtPhone;

    int SELECT_PICTURE = 200;

    String stringImgB64;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        txtFirstName = findViewById(R.id.txtEditFirstName);
        txtSecondName = findViewById(R.id.txtEditSecondName);
        txtPhone = findViewById(R.id.txtEditPhone);
        IVPreviewImage = findViewById(R.id.txtViewPhoto);




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
        EditUserDTO editUserDTO = new EditUserDTO();
        editUserDTO.setFirstName(txtFirstName.getText().toString());
        editUserDTO.setSecondName(txtSecondName.getText().toString());
        editUserDTO.setPhoto(stringImgB64);
        editUserDTO.setPhone(txtPhone.getText().toString());

        AccountService.getInstance()
                .jsonApi()
                .editUser(editUserDTO)
                .enqueue(new Callback<AccountResponseDTO>() {
                    @Override
                    public void onResponse(Call<AccountResponseDTO> call, Response<AccountResponseDTO> response) {
                        if (response.isSuccessful())
                        {
                            Intent intent = new Intent(EditUserActivity.this, UsersActivity.class);
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
        txtFirstName.setError(str);
    }
}