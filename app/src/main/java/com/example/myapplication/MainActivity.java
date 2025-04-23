package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.*;

import java.util.Random;

public class MainActivity extends Activity {

    private static final int PICK_IMAGE = 1;

    EditText inputText;
    Button selectImageBtn, processBtn;
    ProgressBar loader;
    ImageView imagePreview;
    TextView textPreview, newsStatus;

    Uri selectedImageUri = null;
    String enteredText = "";

    enum NewsType {
        FAKE_NEWS,
        VALID_NEWS
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputText = findViewById(R.id.inputText);
        selectImageBtn = findViewById(R.id.selectImageBtn);
        processBtn = findViewById(R.id.processBtn);
        loader = findViewById(R.id.loader);
        imagePreview = findViewById(R.id.imagePreview);
        textPreview = findViewById(R.id.textPreview);
        newsStatus = new TextView(this);
        newsStatus.setTextSize(20);
        newsStatus.setTextColor(Color.WHITE);
        newsStatus.setPadding(20, 20, 20, 20);
        newsStatus.setVisibility(View.GONE);

        // Add newsStatus dynamically to layout
        RelativeLayout rootLayout = findViewById(R.id.rootLayout);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, R.id.textPreview);
        params.setMargins(40, 20, 40, 20);
        rootLayout.addView(newsStatus, params);

        selectImageBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE);
        });

        processBtn.setOnClickListener(v -> {
            enteredText = inputText.getText().toString().trim();
            loader.setVisibility(View.VISIBLE);
            imagePreview.setVisibility(View.GONE);
            textPreview.setVisibility(View.GONE);
            newsStatus.setVisibility(View.GONE);

            // simulate processing
            new Handler().postDelayed(() -> {
                loader.setVisibility(View.GONE);

                if (selectedImageUri != null) {
                    imagePreview.setVisibility(View.VISIBLE);
                    imagePreview.setImageURI(selectedImageUri);
                }

                if (!enteredText.isEmpty()) {
                    textPreview.setVisibility(View.VISIBLE);
                    textPreview.setText(enteredText);
                }

                // Randomly select news type
              //  NewsType type = new Random().nextBoolean() ? NewsType.FAKE_NEWS : NewsType.VALID_NEWS;
                NewsType type = NewsType.FAKE_NEWS;
                if (type == NewsType.FAKE_NEWS) {
                    newsStatus.setBackgroundColor(Color.parseColor("#E53935")); // red
                    newsStatus.setText("🚨 FAKE NEWS DETECTED!");
                } else {
                    newsStatus.setBackgroundColor(Color.parseColor("#43A047")); // green
                    newsStatus.setText("✅ Valid News Verified");
                }

                newsStatus.setVisibility(View.VISIBLE);

                // Keep loader running

            }, 2000);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
        }
    }
}
