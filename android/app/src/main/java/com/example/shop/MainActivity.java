package com.example.shop;

import androidx.annotation.NonNull;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.example.shop.account.LoginActivity;
import com.example.shop.account.RegisterActivity;
import com.example.shop.account.UsersActivity;
import com.example.shop.application.HomeApplication;
import com.example.shop.constants.Urls;
import com.example.shop.account.dto.LoginDTO;
import com.example.shop.network.ImageRequester;
import com.example.shop.security.JwtSecurityService;
import com.example.shop.service.NetworkService;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView tvInfo;
    private TextInputLayout txtFieldEmail;

    private ImageRequester imageRequester;
    private NetworkImageView myImage;
    // One Preview Image
    ImageView IVPreviewImage;

    // constant to compare
    // the activity result code
    int SELECT_PICTURE = 200;
    JwtSecurityService jwtService = (JwtSecurityService) HomeApplication.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvInfo=findViewById(R.id.tvInfo);
        txtFieldEmail=findViewById(R.id.txtFieldEmail);

        imageRequester = ImageRequester.getInstance();
        myImage = findViewById(R.id.myimg);
        String urlImg = Urls.BASE+"/images/1.jpg";
        imageRequester.setImageFromUrl(myImage, urlImg);

        IVPreviewImage = findViewById(R.id.IVPreviewImage);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
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
            case R.id.m_login:
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                return true;
            case R.id.m_users:

                if (jwtService.getToken() == null)
                {
                    intent = new Intent(this, LoginActivity.class);
                }
                else  {
                    intent = new Intent(this, UsersActivity.class);
                }
                startActivity(intent);
                return true;

            case R.id.m_logout:
                jwtService.deleteToken();
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void handleLogin(View view) {
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

    public void onClickRequest(View view) {
        LoginDTO model = new LoginDTO();
        model.setEmail("semen@gmail.com");
//        tvInfo.setText("Сало");
        NetworkService.getInstance()
                .getJSONApi()
                .login(model)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
//                        Post post = response.body();
//
//                        tvInfo.append(post.getId() + "\n");
//                        tvInfo.append(post.getUserId() + "\n");
//                        tvInfo.append(post.getTitle() + "\n");
//                        tvInfo.append(post.getBody() + "\n");
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {

                        tvInfo.append("Error occurred while getting request!");
                        t.printStackTrace();
                    }
                });
    }

    // this function is triggered when user
    // selects the image from the imageChooser
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
                    String sImage= Base64.encodeToString(bytes,Base64.DEFAULT);
                    String app=sImage+"";
                }
            }
        }
    }
}