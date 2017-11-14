package com.github.ghcli.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;



import com.github.ghcli.R;

public class HomePage extends AppCompatActivity {
    private static final String SELECTED_ITEM = "arg_selected_item";


    private BottomNavigationView navBar;
    private int mSelectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        navBar = (BottomNavigationView) findViewById(R.id.navigation);
        navBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectFragment(item);
                return true;
            }
        });

        MenuItem selectedItem;

        if (savedInstanceState != null) {
            mSelectedItem = savedInstanceState.getInt(SELECTED_ITEM, 0);
            selectedItem = navBar.getMenu().findItem(mSelectedItem);
        } else {
            selectedItem = navBar.getMenu().getItem(0);
        }
        selectFragment(selectedItem);
    }

    private void selectFragment(MenuItem item) {
        Fragment frag = null;
        // init corresponding fragment
        switch (item.getItemId()) {
            case R.id.menu_profile:
                frag = ProfileFragment.newInstance("test1","test2");
                break;
            case R.id.menu_followers:
                frag = FollowersFragment.newInstance("teste1","teste2");
                break;
        }

        // update selected item
        mSelectedItem = item.getItemId();

        // uncheck the other items.
        for (int i = 0; i< navBar.getMenu().size(); i++) {
            MenuItem menuItem = navBar.getMenu().getItem(i);
            menuItem.setChecked(menuItem.getItemId() == item.getItemId());
        }


        if (frag != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.container, frag, frag.getTag());
            ft.commit();
        }
    }

    //heavily based on https://github.com/segunfamisa/bottom-navigation-demo/blob/master/app/src/main/java/com/segunfamisa/sample/bottomnav/MainActivity.java
}
