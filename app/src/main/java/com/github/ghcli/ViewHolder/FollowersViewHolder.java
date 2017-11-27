package com.github.ghcli.ViewHolder;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.ghcli.R;
import com.github.ghcli.service.clients.IGitHubUser;
import com.github.ghcli.util.Authentication;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import retrofit2.Call;

public class FollowersViewHolder extends RecyclerView.ViewHolder {
    private final String FOLLOW = "FOLLOW";
    private final String UNFOLLOW = "UNFOLLOW";
    private TextView login;
    private Button follow;
    private ImageView avatar;
    private View view;
    private Context context;
    private IGitHubUser iGitHubUser;
    private boolean isFollowing;

    public FollowersViewHolder(View itemView, IGitHubUser iGitHubUser, Context context) {
        super(itemView);
        this.login = (TextView) itemView.findViewById(R.id.follower_name);
        this.follow = (Button) itemView.findViewById(R.id.follow);
        this.avatar = (ImageView) itemView.findViewById(R.id.follower_avatar);
        this.view = itemView;
        this.context = context;
        this.iGitHubUser = iGitHubUser;
        this.login.setTextColor(Color.BLACK);
        setActionButton();
    }

    private void isFollowing() {
        Call<Void> isFollowing = iGitHubUser.isFollowing(Authentication.getToken(context), login.getText().toString());
        try {
            if (isFollowing.execute().headers().get("status").equals("204 No Content")) {
                this.isFollowing = true;
                this.follow.setText(UNFOLLOW);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setActionButton() {
        this.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (isFollowing) {
                        Call<Void> unfollow = iGitHubUser.unfollow(
                                Authentication.getToken(context),
                                login.getText().toString());

                        if (unfollow.execute().headers().get("status").equals("204 No Content")) {
                            follow.setText(FOLLOW);
                            isFollowing = false;
                        }
                    } else {
                        Call<Void> following = iGitHubUser.follow(
                                Authentication.getToken(context),
                                login.getText().toString());

                        if (following.execute().headers().get("status").equals("204 No Content")) {
                            follow.setText(UNFOLLOW);
                            isFollowing = true;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public TextView getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login.setText(login);
        isFollowing();
    }

    public ImageView getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar_url) {
        Picasso.with(view.getContext()).load(avatar_url).into(this.avatar);
    }
}
