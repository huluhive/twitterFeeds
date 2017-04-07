package com.huluhive.user.twitterstarterapp;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

public class SearchActivity extends AppCompatActivity {
    private static final String TAG = SearchActivity.class.getSimpleName();
    private TweetTimelineListAdapter adapter;
    private ProgressBar mProgressBar;
    private String search_query;
    private ListView tweetList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        tweetList = (ListView) findViewById(R.id.tweet_list);
        search_query=getIntent().getExtras().getString(MainActivity.USERNAME);
        setAdapter(search_query);

    }

    private void setAdapter(String search_query) {
        TextView emptyview=(TextView)findViewById(R.id.empty_view);
        UserTimeline userTimeline = new UserTimeline.Builder()
                .screenName(search_query)
                .build();
        Log.e(TAG,userTimeline.toString());

        adapter = new TweetTimelineListAdapter.Builder(this)
                .setTimeline(userTimeline)
                .build();

        Log.e(TAG,adapter.toString());
        tweetList.setAdapter(adapter);
        tweetList.setEmptyView(emptyview);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu,menu);
        final SearchView searchView= (SearchView) menu.findItem(R.id.search_menu).getActionView();
        searchView.setQueryHint("Enter Tweets");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                setAdapter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.search_menu){
            return true;
        }else if(id==R.id.searchtweet){
            startActivity(new Intent(SearchActivity.this,SearchTweets.class));
        }
        return super.onOptionsItemSelected(item);
    }
}

