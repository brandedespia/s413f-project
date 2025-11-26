package com.example.a413projecthome;

import android.app.AlertDialog;
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

public class PuzzleActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private boolean isNightMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_puzzle);

        isNightMode = getIntent().getBooleanExtra("isNightMode", true);

        View puzzleMainView = findViewById(R.id.puzzle_main);
        if (puzzleMainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(puzzleMainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        applyTheme();

        dbHelper = new DatabaseHelper(this);

        ImageButton menuButton = findViewById(R.id.menu_btn_puzzle);
        menuButton.setOnClickListener(v -> showPopupMenu(v));

        // Initialize puzzle buttons with correct/incorrect logic
        for (int i = 1; i <= 14; i++) {
            int buttonId = getResources().getIdentifier("puzzle_btn_" + i, "id", getPackageName());
            ImageButton button = findViewById(buttonId);
            final int buttonNumber = i;

            // Buttons 11, 12, 13 are correct choices
            if (buttonNumber == 11 || buttonNumber == 12 || buttonNumber == 13) {
                button.setOnClickListener(v -> showSuccessDialog());
            } else {
                // All other buttons are incorrect
                button.setOnClickListener(v -> {
                    Toast.makeText(PuzzleActivity.this, "Incorrect! Try again.", Toast.LENGTH_SHORT).show();
                });
            }
        }
    }

    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Success!");
        builder.setMessage("Puzzle Cleared!");
        builder.setPositiveButton("OK", (dialog, which) -> {
            // Navigate to PuzzleActivity2 (Puzzle 2)
            Intent intent = new Intent(PuzzleActivity.this, PuzzleActivity2.class);
            intent.putExtra("isNightMode", isNightMode);
            startActivity(intent);
            finish();
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.menu_home) {
                Toast.makeText(PuzzleActivity.this, "Returning home...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PuzzleActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("isNightMode", isNightMode);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.menu_settings) {
                Toast.makeText(PuzzleActivity.this, "Opening settings...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PuzzleActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("showSettings", true);
                intent.putExtra("isNightMode", isNightMode);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.menu_about) {
                Toast.makeText(PuzzleActivity.this, "Opening about...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PuzzleActivity.this, MainActivity.class);
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

        View mainView = findViewById(R.id.puzzle_main);
        if (mainView != null) {
            mainView.setBackgroundColor(backgroundColor);
        }

        LinearLayout contentLayout = findViewById(R.id.puzzle_content_layout);
        if (contentLayout != null) {
            contentLayout.setBackgroundColor(backgroundColor);
        }

        TextView titleTextView = findViewById(R.id.puzzle_title);
        if (titleTextView != null) {
            titleTextView.setTextColor(textColor);
        }

        TextView descTextView = findViewById(R.id.puzzle_description);
        if (descTextView != null) {
            descTextView.setTextColor(textColor);
        }
    }
}
