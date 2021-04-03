package com.example.storytellerapp_sanjum;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toolbar;

public class Next extends AppCompatActivity {

    TextView txt;
    ScrollView scrollView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next2);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);


        txt = findViewById(R.id.next_txt);
        scrollView = findViewById(R.id.page_scroll);

        MyStory story = (MyStory) getIntent().getSerializableExtra("story");
        txt.setText(story.getStory());

        setTitle(story.getTitle());


        int[] colors = {Color.parseColor("#ffffff"),Color.parseColor(story.getColor())};
        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TL_BR, colors);

        scrollView.setBackground(gd);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}