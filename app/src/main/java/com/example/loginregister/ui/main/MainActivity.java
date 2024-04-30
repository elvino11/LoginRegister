package com.example.loginregister.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.loginregister.R;
import com.example.loginregister.api.ApiConfig;
import com.example.loginregister.data.User;
import com.example.loginregister.data.UserPreference;
import com.example.loginregister.databinding.ActivityMainBinding;
import com.example.loginregister.response.LoginResponse;
import com.example.loginregister.ui.home.HomeActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private static final String TAG = "MainActivity";

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        isLogin();

        binding.btnLogin.setOnClickListener( view -> {
            if (binding.tieUsername.getText() != null && binding.tiePassword.getText() != null) {
                String username = String.valueOf(binding.tieUsername.getText());
                String password = String.valueOf(binding.tiePassword.getText());
                login(username, password);
            } else {
                Toast.makeText(this, "Lengkapi Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void isLogin() {
        UserPreference userPreference = new UserPreference(this);

        user = userPreference.getUser();
        if (user.isLogin()) {
            Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(homeIntent);
        }
    }

    private void login(String username, String password) {
        showLoading(true);
        Call<LoginResponse>  client = ApiConfig.getApiService().login(username, password);
        client.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                showLoading(false);
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().isStatus()) {
                            Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            
                            String username = response.body().getData().getUsername();
                            String name = response.body().getData().getName();
                            String id = response.body().getData().getId();
                            boolean isLogin = true;
                                    
                            Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(homeIntent);
                            
                            saveUser(username, name, id, isLogin);
                        } else {
                            Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                } else {
                    if (response.body() != null) {
                        Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "onFailure: " + response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                showLoading(false);
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void saveUser(String username, String name, String id, boolean isLogin) {

        UserPreference userPreference = new UserPreference(this);

        user.setName(name);
        user.setUsername(username);
        user.setId(id);
        user.setLogin(isLogin);
        userPreference.setUser(user);
    }

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
        }
    }
}