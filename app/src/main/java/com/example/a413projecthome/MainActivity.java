package com.example.a413projecthome;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

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

        // Apply Night Mode theme on startup
        applyTheme();

        // Initialize menu button
        ImageButton menuButton = findViewById(R.id.menu_btn);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });
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
                    Toast.makeText(MainActivity.this, "Home selected", Toast.LENGTH_SHORT).show();
                    showHomeLayout();
                    return true;
                } else if (itemId == R.id.menu_settings) {
                    // Handle Settings action
                    Toast.makeText(MainActivity.this, "Settings selected", Toast.LENGTH_SHORT).show();
                    showSettingsLayout();
                    return true;
                } else if (itemId == R.id.menu_about) {
                    // Handle About action
                    Toast.makeText(MainActivity.this, "About selected", Toast.LENGTH_SHORT).show();
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

