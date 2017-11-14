package com.github.ghcli.services;

import com.github.ghcli.models.UserGitModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by davi on 14/11/2017.
 */

public interface GitService {

    public static final String BASE_URL = "https://api.github.com/";

    @GET("users/{username}")
    Call<UserGitModel> getUserGit(@Path("username") String username);
}
