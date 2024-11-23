package com.wlghost.sas.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.wlghost.sas.Helper.SessionManager;
import com.wlghost.sas.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private ImageSliderAdapter imageSliderAdapter;
    private ArrayList<Integer> images;
    private int currentImage = 0;
    private Handler handler;
    private Runnable imageSwitcher;

    private ImageView menuBtn;

    SessionManager sessionManager;

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        // Handle Navigation Item Clicks
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    Toast.makeText(MainActivity.this, "Home clicked", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_login) {
                    Intent loginIntent = new Intent(MainActivity.this, login_activity.class);
                    startActivity(loginIntent);
                } else if (id == R.id.nav_logout) {
                    if(sessionManager.isLoggedIn()){
                        sessionManager.logoutUser();
                        startActivity(new Intent(MainActivity.this, login_activity.class));
                        finish();
                        Toast.makeText(MainActivity.this, "Logout Successful", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Unknown item clicked", Toast.LENGTH_SHORT).show();
                }
                drawerLayout.closeDrawer(GravityCompat.START); // Close the drawer
                return true;
            }
        });

        // Set the navigation view initially hidden
        drawerLayout.closeDrawer(GravityCompat.START);


        // Find the ViewPager and Button
        viewPager = findViewById(R.id.imageSlider);
        Button seeMoreButton = findViewById(R.id.seeMoreButton);

        // Image list for slider
        images = new ArrayList<>();
        images.add(R.drawable.image1);  // Add your drawable image files here
        images.add(R.drawable.image2);
        images.add(R.drawable.image3);

        // Setup the ViewPager with ImageSliderAdapter
        imageSliderAdapter = new ImageSliderAdapter(this, images);
        viewPager.setAdapter(imageSliderAdapter);

        // Button to slide to the next image
        seeMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentImage++;
                if (currentImage >= images.size()) {
                    currentImage = 0;
                }
                viewPager.setCurrentItem(currentImage, true);
            }
        });

        // Auto image slider
        handler = new Handler();
        imageSwitcher = new Runnable() {
            @Override
            public void run() {
                currentImage++;
                if (currentImage >= images.size()) {
                    currentImage = 0;
                }
                viewPager.setCurrentItem(currentImage, true);
                handler.postDelayed(this, 3000); // Switch image every 3 seconds
            }
        };

    }

    @Override
    public void onBackPressed() {
        // Close the drawer when the back button is pressed, if it's open
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


}