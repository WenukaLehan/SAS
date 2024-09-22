package com.wlghost.sas;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private ImageSliderAdapter imageSliderAdapter;
    private ArrayList<Integer> images;
    private int currentImage = 0;
    private Handler handler;
    private Runnable imageSwitcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        handler.postDelayed(imageSwitcher, 3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(imageSwitcher);  // Stop the slider when activity is destroyed
    }
}