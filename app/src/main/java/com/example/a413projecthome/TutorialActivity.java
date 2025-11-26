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

public class TutorialActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private boolean isNightMode = true; // Default to match MainActivity's default

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tutorial);

        // Get Night Mode state from intent
        isNightMode = getIntent().getBooleanExtra("isNightMode", false);

        View tutorialMainView = findViewById(R.id.tutorial_main);
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

        // Initialize menu button
        ImageButton menuButton = findViewById(R.id.menu_btn_tutorial);
        menuButton.setOnClickListener(v -> showPopupMenu(v));

        // Initialize next button
        Button nextButton = findViewById(R.id.tutorial_next_btn);
        nextButton.setOnClickListener(v -> {
            // Update tutorial progress to 2 only if current progress is less than 2
            int currentProgress = dbHelper.getTutorialProgress();
            if (currentProgress < 2) {
                dbHelper.updateTutorialProgress(2);
                Toast.makeText(TutorialActivity.this, "Progress saved! Moving to lesson 2...", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(TutorialActivity.this, "Moving to lesson 2...", Toast.LENGTH_SHORT).show();
            }

            // Navigate directly to lesson 2
            Intent intent = new Intent(TutorialActivity.this, TutorialActivity2.class);
            intent.putExtra("isNightMode", isNightMode);
            startActivity(intent);
            finish(); // Close current activity
        });
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.menu_home) {
                Toast.makeText(TutorialActivity.this, "Returning home...", Toast.LENGTH_SHORT).show();
                // Navigate to MainActivity and clear back stack
                Intent intent = new Intent(TutorialActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("isNightMode", isNightMode);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.menu_settings) {
                Toast.makeText(TutorialActivity.this, "Opening settings...", Toast.LENGTH_SHORT).show();
                // Navigate to MainActivity and show Settings layout
                Intent intent = new Intent(TutorialActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("showSettings", true);
                intent.putExtra("isNightMode", isNightMode);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.menu_about) {
                Toast.makeText(TutorialActivity.this, "Opening about...", Toast.LENGTH_SHORT).show();
                // Navigate to MainActivity and show About layout
                Intent intent = new Intent(TutorialActivity.this, MainActivity.class);
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
        View mainView = findViewById(R.id.tutorial_main);
        if (mainView != null) {
            mainView.setBackgroundColor(backgroundColor);
        }

        // Apply theme to content layout
        LinearLayout contentLayout = findViewById(R.id.tutorial_content_layout);
        if (contentLayout != null) {
            contentLayout.setBackgroundColor(backgroundColor);
        }

        // Apply theme to title
        TextView titleTextView = findViewById(R.id.tutorial_title);
        if (titleTextView != null) {
            titleTextView.setTextColor(textColor);
        }

        // Apply theme to description
        TextView descTextView = findViewById(R.id.tutorial_description);
        if (descTextView != null) {
            descTextView.setTextColor(textColor);
        }
    }
}
