package com.xwray.groupie.example.core;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        final Prefs prefs = Prefs.get(this);

        ViewGroup useAsyncContainer = findViewById(R.id.use_async);
        SwitchCompat useAsyncSwitch = useAsyncContainer.findViewById(R.id.the_switch);
        TextView useAsyncText = useAsyncContainer.findViewById(R.id.text);

        ViewGroup showBoundsContainer = findViewById(R.id.show_bounds);
        SwitchCompat showBoundsSwitch = showBoundsContainer.findViewById(R.id.the_switch);
        TextView showBoundsText = showBoundsContainer.findViewById(R.id.text);

        ViewGroup showOffsetsContainer = findViewById(R.id.show_offsets);
        SwitchCompat showOffsetsSwitch = showOffsetsContainer.findViewById(R.id.the_switch);
        TextView showOffsetsText = showOffsetsContainer.findViewById(R.id.text);

        useAsyncText.setText(R.string.use_async);
        useAsyncSwitch.setChecked(prefs.getUseAsync());

        showBoundsText.setText(R.string.show_bounds);
        showBoundsSwitch.setChecked(prefs.getShowBounds());

        showOffsetsText.setText(R.string.show_offsets);
        showOffsetsSwitch.setChecked(prefs.getShowOffsets());

        showBoundsSwitch.setOnCheckedChangeListener(
                (compoundButton, showBounds) -> prefs.setShowBounds(showBounds)
        );

        useAsyncSwitch.setOnCheckedChangeListener(
                (compoundButton, useAsync) -> prefs.setUseAsync(useAsync)
        );

        showOffsetsSwitch.setOnCheckedChangeListener(
                (compoundButton, showOffsets) -> prefs.setShowOffsets(showOffsets)
        );
    }
}
