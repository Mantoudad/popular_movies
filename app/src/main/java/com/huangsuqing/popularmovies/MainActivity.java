package com.huangsuqing.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    // used to maintain the selected option item
    static int activeId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        if(activeId == 0)
            activeId = R.id.action_sort_popularity;
        else
            menu.findItem(activeId).setChecked(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MainActivityFragment mainActivityFragment = MainActivityFragment.instance;
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_sort_popularity) {
            MainActivityFragment.sortOrder = "popular";
        } else if(id == R.id.action_sort_top_rated) {
            MainActivityFragment.sortOrder = "top_rated";
        } else if(id == R.id.action_sort_now_playing) {
            MainActivityFragment.sortOrder = "now_playing";
        } else if(id == R.id.action_sort_upcoming) {
            MainActivityFragment.sortOrder = "upcoming";
        }

        item.setChecked(true);
        // update MainActivityFragment with the selected item
        if (id == R.id.action_sort_popularity || id == R.id.action_sort_top_rated
                || id == R.id.action_sort_now_playing || id == R.id.action_sort_upcoming) {
            mainActivityFragment.updateUI(false);
            activeId = id;
        } else if(id == R.id.action_favorites) {
            mainActivityFragment.updateUI(true);
            activeId = id;
        }

        return super.onOptionsItemSelected(item);
    }
}
