package com.huluhive.user.twitterstarterapp;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.twitter.sdk.android.Twitter;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.SearchService;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.TweetUtils;
import com.twitter.sdk.android.tweetui.TweetView;
import com.twitter.sdk.android.tweetui.UserTimeline;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class SearchTweets extends ActionBarActivity {
        private String searchQuery ="#nepal";
        private String resultType="recent";
        private int count=20;
    private Long maxId;
    private String TAG=SearchTweets.class.getSimpleName();
    private boolean endOfresult;
    private List<Tweet> tweets;
    private LinearLayout myLayout;
    private ProgressBar mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tweets);
        mProgressBar=(ProgressBar)findViewById(R.id.progress_bar_search_tweets);
        myLayout = (LinearLayout) findViewById(R.id.view_search_tweets);
        call(searchQuery);

    }

    private void call(String searchQuery) {
        SearchService service= Twitter.getApiClient().getSearchService();
        service.tweets(searchQuery,null,null,null,resultType,count,null,null,maxId,true).enqueue(new Callback<Search>() {
            @Override
            public void success(Result<Search> result) {
                mProgressBar.setVisibility(View.INVISIBLE);
                Log.e(TAG,result.data.tweets.toString());
                tweets=result.data.tweets;
                for(final Tweet t:tweets){
                    TweetUtils.loadTweet(t.getId(), new Callback<Tweet>() {
                        @Override
                        public void success(Result<Tweet> result) {
                            Log.e(TAG,result.response.toString());
                            myLayout.addView(new TweetView(SearchTweets.this, t));
                        }
                        @Override
                        public void failure(TwitterException exception) {
                            Log.d("TwitterKit", "Load Tweet failure", exception);
                        }
                    });
                }
                if(tweets.size()>0){
                    maxId=tweets.get(tweets.size()-1).id-1;
                }else {
                    endOfresult=true;
                }
            }

            @Override
            public void failure(TwitterException exception) {

            }
        }) ;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu,menu);
      //  SearchManager searchManager= (SearchManager) getSystemService(SEARCH_SERVICE);
        final SearchView searchView= (SearchView) menu.findItem(R.id.search_menu).getActionView();
        searchView.setQueryHint("Enter Tweets");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                myLayout.removeAllViews();
                mProgressBar.setVisibility(View.VISIBLE);
                call(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
       // searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.search_menu){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    protected void onNewIntent(Intent intent) {
//        setIntent(intent);
//        handleIntent(intent);
//
//        super.onNewIntent(intent);
//    }
//
//    private void handleIntent(Intent intent) {
//            if(intent.getAction().equals(Intent.ACTION_SEARCH)){
//                searchQuery=intent.getStringExtra(SearchManager.QUERY);
//            }
    //}
}
