package sigit.dbmovieretofitcardview.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sigit.dbmovieretofitcardview.R;
import sigit.dbmovieretofitcardview.adapter.SearchAdapter;
import sigit.dbmovieretofitcardview.model.Search;
import sigit.dbmovieretofitcardview.model.SearchResponse;
import sigit.dbmovieretofitcardview.rest.ApiClient;
import sigit.dbmovieretofitcardview.rest.ApiInterface;

/**
 * Created by sigit on 10/08/17.
 */

public class DetailSearchActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static final String API_KEY = "cbbfb3a08172212bc7d32daa19bd7d42";

    String query;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);
        final Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swiprefresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(this);

        RecyclerView.LayoutManager mLayourManager = new GridLayoutManager(this.getApplicationContext(),2);
        recyclerView.setLayoutManager(mLayourManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        setSupportActionBar(toolbar);

        if (getSupportActionBar()!= null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            Bundle extra = getIntent().getExtras();
            query = extra.getString("query");

            getSupportActionBar().setTitle("Search of Result"+query);

            fetch(true);
        }else {
            startActivity(new Intent(DetailSearchActivity.this, MainActivity.class));
            finish();
        }

    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void fetch(boolean get){
        if(get){
            //initial retrofit builder & interface model gson class
            ApiInterface apiInterface =
                    ApiClient.getClient().create(ApiInterface.class);
            Call<Search> call =apiInterface.getSearch(API_KEY, query);

            call.enqueue(new Callback<Search>() {
                @Override
                public void onResponse(Call<Search> call, Response<Search> response) {
                    swipeRefreshLayout.setRefreshing(false);
                    int statusCode = response.code();
                    List<SearchResponse>searchResponses = response.body().getResults();

                    if (!searchResponses.isEmpty()){
                        recyclerView.setAdapter(new SearchAdapter(searchResponses,getApplicationContext()));
                    }else {
                        Snackbar snackbar = Snackbar.make(recyclerView,"No Data",Snackbar.LENGTH_LONG);
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.red));
                        snackbar.show();
                    }
                }

                @Override
                public void onFailure(Call<Search> call, Throwable t) {

                    Snackbar snackbar = Snackbar.make(recyclerView,"No Data",Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.red));
                    snackbar.show();

                }
            });

        }
        if (!get){
            swipeRefreshLayout.setRefreshing(false);
        }
    }


    @Override
    public void onRefresh() {

    }
}
