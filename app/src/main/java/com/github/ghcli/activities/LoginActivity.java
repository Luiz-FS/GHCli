package com.github.ghcli.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.github.ghcli.R;
import com.github.ghcli.models.GitHubUser;
import com.github.ghcli.service.ServiceGenerator;
import com.github.ghcli.service.clients.IGitHubUser;
import com.github.ghcli.util.Authentication;
import com.github.ghcli.util.Connection;
import com.github.ghcli.util.Message;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String KEY_USER = "user";

    private GitHubUser user;

    @BindView(R.id.email) EditText email;
    @BindView(R.id.password) EditText password;
    @BindView(R.id.sign_in) Button signIn;
    @BindView(R.id.layout_error_connection) LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String credentials = Authentication.getToken(getApplicationContext());

        if (credentials == null) {
            setContentView(R.layout.activity_login);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            ButterKnife.bind(this);

            if (!Connection.isOnline(getApplicationContext())) {
                Connection.snackbarWifi(findViewById(R.id.content_login), getApplicationContext());
                signIn.setEnabled(false);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (!Connection.isOnline(getApplicationContext())) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                signIn.setEnabled(true);
                                linearLayout.setVisibility(View.INVISIBLE);
                            }
                        });

                    }
                }).start();
            }

        // If credentials already exists, get user and go to home page
        } else {
            loadUser(credentials);
        }
    }

    @OnClick(R.id.sign_in)
    public void login() {
        String email = this.email.getText().toString();
        String password = this.password.getText().toString();

        final String credentials = Credentials.basic(email, password);

        loadUser(credentials);
    }

    @OnClick(R.id.close)
    public void closeApp() {
        finish();
    }

    private void loadUser(final String credentials) {
        IGitHubUser gitHubUserClient = ServiceGenerator.createService(IGitHubUser.class);
        Call<GitHubUser> call = gitHubUserClient.getUser(credentials);

        call.enqueue(new Callback<GitHubUser>() {
            @Override
            public void onResponse(@NonNull Call<GitHubUser> call,
                                   @NonNull Response<GitHubUser> response) {
                // If username and password are valid information
                // save the token and go to home page
                if (response.isSuccessful()) {
                    Authentication.saveToken(getApplicationContext(), credentials);
                    user = response.body();
                    Intent intent = new Intent(getApplicationContext(), HomePage.class);
                    intent.putExtra(KEY_USER, user);
                    startActivity(intent);
                    finish();
                } else {
                    Message.showSnackbar(getString(R.string.login_failed), findViewById(R.id.content_login));
                }
            }

            @Override
            public void onFailure(@NonNull Call<GitHubUser> call, @NonNull Throwable t) {
                Message.showSnackbar(getString(R.string.login_failed), findViewById(R.id.content_login));
            }
        });
    }
}
