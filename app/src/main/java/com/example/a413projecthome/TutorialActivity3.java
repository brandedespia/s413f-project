package com.example.a413projecthome;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TutorialActivity3 extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private boolean isNightMode = true; // Default to match MainActivity's default

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tutorial3);

        // Get Night Mode state from intent
        isNightMode = getIntent().getBooleanExtra("isNightMode", true);

        View tutorialMainView = findViewById(R.id.tutorial_main_3);
        if (tutorialMainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(tutorialMainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        // Apply theme colors
        applyTheme();

        // Initialize database
        dbHelper = new DatabaseHelper(this);

        // Update tutorial progress to 3 when starting Lesson 3
        int currentProgress = dbHelper.getTutorialProgress();
        if (currentProgress < 3) {
            dbHelper.updateTutorialProgress(3);
        }

        // Initialize menu button
        ImageButton menuButton = findViewById(R.id.menu_btn_tutorial_3);
        menuButton.setOnClickListener(v -> showPopupMenu(v));

        // Initialize back button
        Button backButton = findViewById(R.id.tutorial_back_btn_3);
        backButton.setOnClickListener(v -> {
            // Navigate back to TutorialActivity2 (Lesson 2)
            Intent intent = new Intent(TutorialActivity3.this, TutorialActivity2.class);
            intent.putExtra("isNightMode", isNightMode);
            startActivity(intent);
            finish();
        });

        // Initialize next/finish button
        Button nextButton = findViewById(R.id.tutorial_next_btn_3);
        nextButton.setOnClickListener(v -> {
            // Update tutorial progress to 4 only if current progress is less than 4
            int progress = dbHelper.getTutorialProgress();
            if (progress < 4) {
                dbHelper.updateTutorialProgress(4);
                Toast.makeText(TutorialActivity3.this, "Progress saved! Lesson 3 completed!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(TutorialActivity3.this, "Lesson 3 completed!", Toast.LENGTH_SHORT).show();
            }

            // Navigate to MainActivity and clear back stack
            Intent intent = new Intent(TutorialActivity3.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("isNightMode", isNightMode);
            startActivity(intent);
            finish();
        });
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.menu_home) {
                Toast.makeText(TutorialActivity3.this, "Returning home...", Toast.LENGTH_SHORT).show();
                // Navigate to MainActivity and clear back stack
                Intent intent = new Intent(TutorialActivity3.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("isNightMode", isNightMode);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.menu_settings) {
                Toast.makeText(TutorialActivity3.this, "Opening settings...", Toast.LENGTH_SHORT).show();
                // Navigate to MainActivity and show Settings layout
                Intent intent = new Intent(TutorialActivity3.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("showSettings", true);
                intent.putExtra("isNightMode", isNightMode);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.menu_about) {
                Toast.makeText(TutorialActivity3.this, "Opening about...", Toast.LENGTH_SHORT).show();
                // Navigate to MainActivity and show About layout
                Intent intent = new Intent(TutorialActivity3.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("showAbout", true);
                intent.putExtra("isNightMode", isNightMode);
                startActivity(intent);
                finish();
                return true;
            }
            return false;
        });

        popupMenu.show();
    }

    private void applyTheme() {
        int backgroundColor = isNightMode ? Color.parseColor("#2B2B2B") : Color.LTGRAY;
        int textColor = isNightMode ? Color.WHITE : Color.BLACK;

        // Apply theme to main background
        View mainView = findViewById(R.id.tutorial_main_3);
        if (mainView != null) {
            mainView.setBackgroundColor(backgroundColor);
        }

        // Apply theme to content layout
        LinearLayout contentLayout = findViewById(R.id.tutorial_content_layout_3);
        if (contentLayout != null) {
            contentLayout.setBackgroundColor(backgroundColor);
        }

        // Apply theme to title
        TextView titleTextView = findViewById(R.id.tutorial_title_3);
        if (titleTextView != null) {
            titleTextView.setTextColor(textColor);
        }

        // Apply theme to description
        TextView descTextView = findViewById(R.id.tutorial_description_3);
        if (descTextView != null) {
            descTextView.setTextColor(textColor);
        }
    }
}

