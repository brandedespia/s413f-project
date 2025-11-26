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

public class PuzzleSelectActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private boolean isNightMode = true; // Default to match MainActivity's default

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_puzzle_select);

        // Get Night Mode state from intent
        isNightMode = getIntent().getBooleanExtra("isNightMode", true);

        View puzzleSelectMainView = findViewById(R.id.puzzle_select_main);
        if (puzzleSelectMainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(puzzleSelectMainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        // Apply theme colors
        applyTheme();

        // Initialize database
        dbHelper = new DatabaseHelper(this);

        // Get current puzzle progress to enable/disable puzzles
        int puzzleProgress = dbHelper.getPuzzleProgress();

        // Initialize menu button
        ImageButton menuButton = findViewById(R.id.menu_btn_puzzle_select);
        menuButton.setOnClickListener(v -> showPopupMenu(v));

        // Initialize Puzzle 1 button
        Button puzzle1Button = findViewById(R.id.puzzle_1_btn);
        puzzle1Button.setOnClickListener(v -> {
            Intent intent = new Intent(PuzzleSelectActivity.this, PuzzleActivity.class);
            intent.putExtra("isNightMode", isNightMode);
            startActivity(intent);
        });

        // Initialize Puzzle 2 button
        Button puzzle2Button = findViewById(R.id.puzzle_2_btn);
        if (puzzleProgress >= 2) {
            // User has completed puzzle 1, enable puzzle 2
            puzzle2Button.setEnabled(true);
            puzzle2Button.setOnClickListener(v -> {
                Intent intent = new Intent(PuzzleSelectActivity.this, PuzzleActivity2.class);
                intent.putExtra("isNightMode", isNightMode);
                startActivity(intent);
            });
        } else {
            // User hasn't completed puzzle 1 yet
            puzzle2Button.setEnabled(false);
            puzzle2Button.setAlpha(0.5f);
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
                Toast.makeText(PuzzleSelectActivity.this, "Returning home...", Toast.LENGTH_SHORT).show();
                // Navigate to MainActivity and clear back stack
                Intent intent = new Intent(PuzzleSelectActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("isNightMode", isNightMode);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.menu_settings) {
                Toast.makeText(PuzzleSelectActivity.this, "Opening settings...", Toast.LENGTH_SHORT).show();
                // Navigate to MainActivity and show Settings layout
                Intent intent = new Intent(PuzzleSelectActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("showSettings", true);
                intent.putExtra("isNightMode", isNightMode);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.menu_about) {
                Toast.makeText(PuzzleSelectActivity.this, "Opening about...", Toast.LENGTH_SHORT).show();
                // Navigate to MainActivity and show About layout
                Intent intent = new Intent(PuzzleSelectActivity.this, MainActivity.class);
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
        View mainView = findViewById(R.id.puzzle_select_main);
        if (mainView != null) {
            mainView.setBackgroundColor(backgroundColor);
        }

        // Apply theme to content layout
        LinearLayout contentLayout = findViewById(R.id.puzzle_select_content_layout);
        if (contentLayout != null) {
            contentLayout.setBackgroundColor(backgroundColor);
        }

        // Apply theme to title
        TextView titleTextView = findViewById(R.id.puzzle_select_title);
        if (titleTextView != null) {
            titleTextView.setTextColor(textColor);
        }
    }
}

