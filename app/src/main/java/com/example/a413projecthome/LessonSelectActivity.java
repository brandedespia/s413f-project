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

public class LessonSelectActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private boolean isNightMode = true; // Default to match MainActivity's default

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lesson_select);

        // Get Night Mode state from intent
        isNightMode = getIntent().getBooleanExtra("isNightMode", false);

        View lessonSelectMainView = findViewById(R.id.lesson_select_main);
        if (lessonSelectMainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(lessonSelectMainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        // Apply theme colors
        applyTheme();

        // Initialize database
        dbHelper = new DatabaseHelper(this);

        // Get current tutorial progress to enable/disable lessons
        int tutorialProgress = dbHelper.getTutorialProgress();

        // Initialize menu button
        ImageButton menuButton = findViewById(R.id.menu_btn_lesson_select);
        menuButton.setOnClickListener(v -> showPopupMenu(v));

        // Initialize Lesson 1 button
        Button lesson1Button = findViewById(R.id.lesson_1_btn);
        lesson1Button.setOnClickListener(v -> {
            Intent intent = new Intent(LessonSelectActivity.this, TutorialActivity.class);
            intent.putExtra("isNightMode", isNightMode);
            startActivity(intent);
        });

        // Initialize Lesson 2 button
        Button lesson2Button = findViewById(R.id.lesson_2_btn);
        if (tutorialProgress >= 2) {
            // User has completed lesson 1, enable lesson 2
            lesson2Button.setEnabled(true);
            lesson2Button.setOnClickListener(v -> {
                Intent intent = new Intent(LessonSelectActivity.this, TutorialActivity2.class);
                intent.putExtra("isNightMode", isNightMode);
                startActivity(intent);
            });
        } else {
            // User hasn't completed lesson 1 yet
            lesson2Button.setEnabled(false);
            lesson2Button.setAlpha(0.5f);
        }

        // Initialize Lesson 3 button
        Button lesson3Button = findViewById(R.id.lesson_3_btn);
        if (tutorialProgress >= 3) {
            // User has completed lesson 2, enable lesson 3
            lesson3Button.setEnabled(true);
            lesson3Button.setOnClickListener(v -> {
                Intent intent = new Intent(LessonSelectActivity.this, TutorialActivity3.class);
                intent.putExtra("isNightMode", isNightMode);
                startActivity(intent);
            });
        } else {
            // User hasn't completed lesson 2 yet
            lesson3Button.setEnabled(false);
            lesson3Button.setAlpha(0.5f);
        }

        // Initialize Back to Home button
        Button backButton = findViewById(R.id.back_to_home_btn);
        backButton.setOnClickListener(v -> finish());
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.menu_home) {
                Toast.makeText(LessonSelectActivity.this, "Returning home...", Toast.LENGTH_SHORT).show();
                // Navigate to MainActivity and clear back stack
                Intent intent = new Intent(LessonSelectActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("isNightMode", isNightMode);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.menu_settings) {
                Toast.makeText(LessonSelectActivity.this, "Opening settings...", Toast.LENGTH_SHORT).show();
                // Navigate to MainActivity and show Settings layout
                Intent intent = new Intent(LessonSelectActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("showSettings", true);
                intent.putExtra("isNightMode", isNightMode);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.menu_about) {
                Toast.makeText(LessonSelectActivity.this, "Opening about...", Toast.LENGTH_SHORT).show();
                // Navigate to MainActivity and show About layout
                Intent intent = new Intent(LessonSelectActivity.this, MainActivity.class);
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
        View mainView = findViewById(R.id.lesson_select_main);
        if (mainView != null) {
            mainView.setBackgroundColor(backgroundColor);
        }

        // Apply theme to content layout
        LinearLayout contentLayout = findViewById(R.id.lesson_select_content_layout);
        if (contentLayout != null) {
            contentLayout.setBackgroundColor(backgroundColor);
        }

        // Apply theme to title
        TextView titleTextView = findViewById(R.id.lesson_select_title);
        if (titleTextView != null) {
            titleTextView.setTextColor(textColor);
        }
    }
}
