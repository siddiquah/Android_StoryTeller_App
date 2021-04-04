package com.example.storytellerapp_sanjum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    DatabaseReference databaseReference;
    ArrayList<MyStory> stories;
    StoryAdapter adapter;

    String[] colors = new String[]{
        "#FFCDD2",
        "#F8BBD0",
        "#E1BEE7",
        "#D1C4E9",
        "#C5CAE9",
        "#BBDEFB",
        "#B3E5FC",
        "#B2EBF2",
        "#B2DFDB",
        "#C8E6C9",
        "#DCEDC8",
        "#F0F4C3",
        "#FFF9C4",
        "#FFECB3",
        "#FFE0B2",
        "#FFCCBC",
        "#D7CCC8",
        "#B0BEC5"
    };

    public String getRandomColor() {
        int rnd = new Random().nextInt(this.colors.length);
        return this.colors[rnd];
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView= findViewById(R.id.listView);
        databaseReference = FirebaseDatabase.getInstance().getReference("StoryTeller");
        stories = new ArrayList<MyStory>();
        adapter = new StoryAdapter(this, stories);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                stories.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    MyStory myStory = ds.getValue(MyStory.class);
                    myStory.setColor(getRandomColor());
                    stories.add(0, myStory);
                }
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(MainActivity.this,Next.class);
                        MyStory s = stories.get(position);
                        intent.putExtra("story", s);
                        startActivity(intent);
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchview, menu);
        MenuItem menuItem = menu.findItem(R.id.searchIcon);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search stories");

        ArrayList searchedStories = new ArrayList<MyStory>();
        StoryAdapter newAdapter = new StoryAdapter(this, searchedStories);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public void searchStories(String query) {
                String q = query.toLowerCase();
                searchedStories.clear();
                for(int i=0; i<stories.size(); i++) {
                    MyStory story = stories.get(i);
                    if(story.getTitle().toLowerCase().contains(q)) {
                        searchedStories.add(stories.get(i));
                    }
                }
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                searchStories(query);
                listView.setAdapter(newAdapter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchStories(newText);
                listView.setAdapter(newAdapter);
                return false;
            }

        });

        menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                searchedStories.clear();
                searchedStories.addAll(stories);
                listView.setAdapter(newAdapter);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}