package com.github.ghcli.ViewHolder;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.ghcli.R;

import butterknife.BindView;

public class FollowersViewHolder extends RecyclerView.ViewHolder{
    private TextView login;
    private Button follow;

    public FollowersViewHolder(View itemView) {
        super(itemView);
        this.login = (TextView) itemView.findViewById(R.id.follower_name);
        this.follow = (Button) itemView.findViewById(R.id.follow);

        this.login.setTextColor(Color.BLACK);
    }

    public TextView getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login.setText(login);
    }
}
