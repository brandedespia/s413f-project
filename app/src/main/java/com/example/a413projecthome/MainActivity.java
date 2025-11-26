package com.example.a413projecthome;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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




public class MainActivity extends AppCompatActivity {

    private LinearLayout tutorLayout;
    private LinearLayout puzzleLayout;
    private LinearLayout mainLayout;
    private LinearLayout aboutLayout;
    private LinearLayout settingsLayout;
    private boolean isAboutVisible = false;
    private boolean isSettingsVisible = false;
    private boolean isNightMode = true; // Start with Night Mode enabled

    // Store references to all text views for theme changes
    private TextView titleTextView;
    private TextView descTextView;
    private TextView settingsTitleTextView;
    private TextView nightModeTitleTextView;
    private TextView nightModeDescTextView;

    // Database helper
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialize database
        dbHelper = new DatabaseHelper(this);
        // Trigger database creation by getting readable database
        dbHelper.getReadableDatabase();

        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        // Initialize layouts
        mainLayout = findViewById(R.id.main);
        tutorLayout = findViewById(R.id.Tutor_layout);
        puzzleLayout = findViewById(R.id.Puzzle_layout);

        // Read progress from database and update TextViews
        int[] progress = dbHelper.getProgress();
        int tutorialProgress = progress[1]; // tprog
        int puzzleProgress = progress[0]; // pprog

        TextView tutProgressTextView = findViewById(R.id.Tut_progress);
        TextView puzProgressTextView = findViewById(R.id.Puz_progress);

        tutProgressTextView.setText("Your progress: " + tutorialProgress);
        puzProgressTextView.setText("Your progress: " + puzzleProgress);

        // Initialize Tutorial Continue button
        findViewById(R.id.Tut_Con_Btn).setOnClickListener(v -> {
            int currentTutorialProgress = dbHelper.getTutorialProgress();
            Intent intent;

            // Navigate based on tutorial progress
            if (currentTutorialProgress == 1) {
                intent = new Intent(MainActivity.this, TutorialActivity.class);
            } else if (currentTutorialProgress == 2) {
                intent = new Intent(MainActivity.this, TutorialActivity2.class);
            } else if (currentTutorialProgress == 3) {
                intent = new Intent(MainActivity.this, TutorialActivity3.class);
            } else {
                // If progress is beyond available lessons, show a message
                Toast.makeText(MainActivity.this, "You've completed all available lessons!", Toast.LENGTH_SHORT).show();
                return;
            }

            intent.putExtra("isNightMode", isNightMode);
            startActivity(intent);
        });

        // Initialize Tutorial Select Lesson button
        findViewById(R.id.Tut_select_Btn).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LessonSelectActivity.class);
            intent.putExtra("isNightMode", isNightMode);
            startActivity(intent);
        });

        // Initialize Tutorial Reset Progress button
        findViewById(R.id.Tut_reset_Btn).setOnClickListener(v -> {
            // Reset tutorial progress to 1
            dbHelper.updateTutorialProgress(1);
            Toast.makeText(MainActivity.this, "Tutorial progress reset to 1", Toast.LENGTH_SHORT).show();

            // Update the progress display immediately
            TextView tutResetProgressTextView = findViewById(R.id.Tut_progress);
            tutResetProgressTextView.setText("Your progress: 1");
        });

        // Initialize Puzzle Continue button
        findViewById(R.id.Puz_Con_Btn).setOnClickListener(v -> {
            int currentPuzzleProgress = dbHelper.getPuzzleProgress();
            Intent intent;

            if (currentPuzzleProgress == 1) {
                intent = new Intent(MainActivity.this, PuzzleActivity.class);
            } else if (currentPuzzleProgress == 2) {
                intent = new Intent(MainActivity.this, PuzzleActivity2.class);
            } else {
                Toast.makeText(MainActivity.this, "You've completed all available puzzles!", Toast.LENGTH_SHORT).show();
                return;
            }

            intent.putExtra("isNightMode", isNightMode);
            startActivity(intent);
        });

        // Initialize Puzzle Select Lesson button
        findViewById(R.id.Puz_select_Btn).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PuzzleSelectActivity.class);
            intent.putExtra("isNightMode", isNightMode);
            startActivity(intent);
        });

        // Initialize Puzzle Reset Progress button
        findViewById(R.id.Puz_reset_Btn).setOnClickListener(v -> {
            // Reset puzzle progress to 1
            dbHelper.updatePuzzleProgress(1);
            Toast.makeText(MainActivity.this, "Puzzle progress reset to 1", Toast.LENGTH_SHORT).show();

            // Update the progress display immediately
            TextView puzResetProgressTextView = findViewById(R.id.Puz_progress);
            puzResetProgressTextView.setText("Your progress: 1");
        });

        // Initialize menu button
        ImageButton menuButton = findViewById(R.id.menu_btn);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });

        // Check if we need to show Settings or About layout from intent
        Intent intent = getIntent();
        if (intent != null) {
            // Check if isNightMode was passed from another activity
            if (intent.hasExtra("isNightMode")) {
                isNightMode = intent.getBooleanExtra("isNightMode", true);
            }

            boolean showSettings = intent.getBooleanExtra("showSettings", false);
            boolean showAbout = intent.getBooleanExtra("showAbout", false);

            if (showSettings) {
                showSettingsLayout();
            } else if (showAbout) {
                showAboutLayout();
            }
        }

        // Apply Night Mode theme after processing Intent extras
        applyTheme();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Refresh progress display when returning from tutorial
        int[] progress = dbHelper.getProgress();
        int tutorialProgress = progress[1]; // tprog
        int puzzleProgress = progress[0]; // pprog

        TextView tutProgressTextView = findViewById(R.id.Tut_progress);
        TextView puzProgressTextView = findViewById(R.id.Puz_progress);

        tutProgressTextView.setText("Your progress: " + tutorialProgress);
        puzProgressTextView.setText("Your progress: " + puzzleProgress);
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.menu_home) {
                    // Handle Home action
                    // Toast.makeText(MainActivity.this, "Home selected", Toast.LENGTH_SHORT).show();
                    showHomeLayout();
                    return true;
                } else if (itemId == R.id.menu_settings) {
                    // Handle Settings action
                    // Toast.makeText(MainActivity.this, "Settings selected", Toast.LENGTH_SHORT).show();
                    showSettingsLayout();
                    return true;
                } else if (itemId == R.id.menu_about) {
                    // Handle About action
                    // Toast.makeText(MainActivity.this, "About selected", Toast.LENGTH_SHORT).show();
                    showAboutLayout();
                    return true;
                }
                return false;
            }
        });

        popupMenu.show();
    }

    private void showAboutLayout() {
        if (isAboutVisible) {
            return; // Already showing about
        }

        // Hide the original two layouts and settings
        tutorLayout.setVisibility(View.GONE);
        puzzleLayout.setVisibility(View.GONE);
        if (settingsLayout != null) {
            settingsLayout.setVisibility(View.GONE);
        }
        isSettingsVisible = false;

        // Create the About layout if it doesn't exist
        if (aboutLayout == null) {
            aboutLayout = new LinearLayout(this);
            aboutLayout.setOrientation(LinearLayout.VERTICAL);
            aboutLayout.setGravity(Gravity.CENTER);
            aboutLayout.setPadding(48, 48, 48, 48);

            // Set layout parameters to match the combined width of the two original layouts
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    2.0f // weight of 2 to occupy space of both original layouts
            );
            aboutLayout.setLayoutParams(params);
            // Set initial background color based on current theme
            aboutLayout.setBackgroundColor(isNightMode ? Color.parseColor("#2B2B2B") : Color.LTGRAY);

            // Create first TextView - Title
            titleTextView = new TextView(this);
            titleTextView.setText("About This App");
            titleTextView.setTextSize(24);
            titleTextView.setTextColor(isNightMode ? Color.WHITE : Color.BLACK);
            titleTextView.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            titleParams.setMargins(0, 0, 0, 32);
            titleTextView.setLayoutParams(titleParams);

            // Create second TextView - Description
            descTextView = new TextView(this);
            descTextView.setText("This is a mah-jong learning application.\nVersion 1.0\n\nDeveloped for S413F Project");
            descTextView.setTextSize(16);
            descTextView.setTextColor(isNightMode ? Color.WHITE : Color.BLACK);
            descTextView.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams descParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            descTextView.setLayoutParams(descParams);

            // Add TextViews to About layout
            aboutLayout.addView(titleTextView);
            aboutLayout.addView(descTextView);

            // Add About layout to main layout
            mainLayout.addView(aboutLayout);
        } else {
            aboutLayout.setVisibility(View.VISIBLE);
        }

        isAboutVisible = true;
    }

    private void showHomeLayout() {
        if (!isAboutVisible && !isSettingsVisible) {
            return; // Already showing home
        }

        // Hide about and settings layouts
        if (aboutLayout != null) {
            aboutLayout.setVisibility(View.GONE);
        }
        if (settingsLayout != null) {
            settingsLayout.setVisibility(View.GONE);
        }

        // Show original layouts
        tutorLayout.setVisibility(View.VISIBLE);
        puzzleLayout.setVisibility(View.VISIBLE);

        isAboutVisible = false;
        isSettingsVisible = false;
    }

    private void showSettingsLayout() {
        if (isSettingsVisible) {
            return; // Already showing settings
        }

        // Hide the original two layouts and about
        tutorLayout.setVisibility(View.GONE);
        puzzleLayout.setVisibility(View.GONE);
        if (aboutLayout != null) {
            aboutLayout.setVisibility(View.GONE);
        }
        isAboutVisible = false;

        // Create the Settings layout if it doesn't exist
        if (settingsLayout == null) {
            settingsLayout = new LinearLayout(this);
            settingsLayout.setOrientation(LinearLayout.VERTICAL);
            settingsLayout.setGravity(Gravity.START);
            settingsLayout.setPadding(48, 48, 48, 48);

            // Set layout parameters to match the combined width of the two original layouts
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    2.0f // weight of 2 to occupy space of both original layouts
            );
            settingsLayout.setLayoutParams(params);
            // Set initial background color based on current theme
            settingsLayout.setBackgroundColor(isNightMode ? Color.parseColor("#2B2B2B") : Color.LTGRAY);

            // Create Settings Title TextView
            settingsTitleTextView = new TextView(this);
            settingsTitleTextView.setText("Settings");
            settingsTitleTextView.setTextSize(24);
            settingsTitleTextView.setTextColor(Color.WHITE);
            settingsTitleTextView.setGravity(Gravity.START);
            LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            titleParams.setMargins(0, 0, 0, 48);
            settingsTitleTextView.setLayoutParams(titleParams);

            // Create a container for the Night Mode preference
            LinearLayout preferenceContainer = new LinearLayout(this);
            preferenceContainer.setOrientation(LinearLayout.HORIZONTAL);
            preferenceContainer.setGravity(Gravity.CENTER_VERTICAL);
            preferenceContainer.setPadding(0, 16, 0, 16);
            LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            preferenceContainer.setLayoutParams(containerParams);

            // Create a vertical LinearLayout for title and description
            LinearLayout textContainer = new LinearLayout(this);
            textContainer.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams textContainerParams = new LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1.0f
            );
            textContainer.setLayoutParams(textContainerParams);

            // Create Night Mode title TextView
            nightModeTitleTextView = new TextView(this);
            nightModeTitleTextView.setText("Night Mode");
            nightModeTitleTextView.setTextSize(18);
            nightModeTitleTextView.setTextColor(Color.WHITE);
            LinearLayout.LayoutParams nightModeTitleParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            nightModeTitleParams.setMargins(0, 0, 0, 8);
            nightModeTitleTextView.setLayoutParams(nightModeTitleParams);

            // Create Night Mode description TextView
            nightModeDescTextView = new TextView(this);
            nightModeDescTextView.setText("Turn the application to night mode.");
            nightModeDescTextView.setTextSize(14);
            nightModeDescTextView.setTextColor(Color.LTGRAY);
            LinearLayout.LayoutParams nightModeDescParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            nightModeDescTextView.setLayoutParams(nightModeDescParams);

            // Add title and description to text container
            textContainer.addView(nightModeTitleTextView);
            textContainer.addView(nightModeDescTextView);

            // Create CheckBox
            CheckBox nightModeCheckBox = new CheckBox(this);
            nightModeCheckBox.setChecked(isNightMode);
            LinearLayout.LayoutParams checkBoxParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            nightModeCheckBox.setLayoutParams(checkBoxParams);

            // Set checkbox listener
            nightModeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    isNightMode = isChecked;
                    applyTheme();
                }
            });

            // Add text container and checkbox to preference container
            preferenceContainer.addView(textContainer);
            preferenceContainer.addView(nightModeCheckBox);

            // Add all views to settings layout
            settingsLayout.addView(settingsTitleTextView);
            settingsLayout.addView(preferenceContainer);

            // Add Settings layout to main layout
            mainLayout.addView(settingsLayout);
        } else {
            settingsLayout.setVisibility(View.VISIBLE);
        }

        isSettingsVisible = true;
    }

    private void applyTheme() {
        int backgroundColor = isNightMode ? Color.parseColor("#2B2B2B") : Color.LTGRAY;
        int textColor = isNightMode ? Color.WHITE : Color.BLACK;
        int descColor = isNightMode ? Color.WHITE : Color.BLACK;

        // Apply theme to main layout
        mainLayout.setBackgroundColor(backgroundColor);

        // Apply theme to About layout background and TextViews
        if (aboutLayout != null) {
            aboutLayout.setBackgroundColor(backgroundColor);
        }
        if (titleTextView != null) {
            titleTextView.setTextColor(textColor);
        }
        if (descTextView != null) {
            descTextView.setTextColor(descColor);
        }

        // Apply theme to Settings layout background and TextViews
        if (settingsLayout != null) {
            settingsLayout.setBackgroundColor(backgroundColor);
        }
        if (settingsTitleTextView != null) {
            settingsTitleTextView.setTextColor(textColor);
        }
        if (nightModeTitleTextView != null) {
            nightModeTitleTextView.setTextColor(textColor);
        }
        if (nightModeDescTextView != null) {
            nightModeDescTextView.setTextColor(descColor);
        }

        // Apply theme to Tutor and Puzzle layouts (background and text)
        if (tutorLayout != null) {
            tutorLayout.setBackgroundColor(backgroundColor);
            applyThemeToLayout(tutorLayout, textColor);
        }
        if (puzzleLayout != null) {
            puzzleLayout.setBackgroundColor(backgroundColor);
            applyThemeToLayout(puzzleLayout, textColor);
        }

        // Toast.makeText(this, "Night Mode " + (isNightMode ? "enabled" : "disabled"), Toast.LENGTH_SHORT).show();
    }

    private void applyThemeToLayout(ViewGroup layout, int textColor) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof TextView) {
                ((TextView) child).setTextColor(textColor);
            } else if (child instanceof ViewGroup) {
                applyThemeToLayout((ViewGroup) child, textColor);
            }
        }
    }
}

