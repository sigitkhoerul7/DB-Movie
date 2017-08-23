package sigit.dbmovieretofitcardview.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sigit.dbmovieretofitcardview.R;
import sigit.dbmovieretofitcardview.model.Details;
import sigit.dbmovieretofitcardview.model.Genre;
import sigit.dbmovieretofitcardview.rest.ApiClient;
import sigit.dbmovieretofitcardview.rest.ApiInterface;

/**
 * Created by sigit on 04/08/17.
 */

public class DetailRateActivity  extends AppCompatActivity{

    private final static String API_KEY ="cbbfb3a08172212bc7d32daa19bd7d42"; //Key DB MOVIE
    //deklarasi variabel activity
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBarLayout;
    private TextView title,rating,genre,overview;
    private FloatingActionButton fab;
    String gen ="";
    private ImageView header,back;
    private Menu collapsedMenu;
    private boolean appBarExpanded = true;
    int id;
    String key_trailer="";
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_rate);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setTitle("");
        //inisialisasi activity sesuai dengan id
        fab = (FloatingActionButton)findViewById(R.id.yutup);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        header = (ImageView) findViewById(R.id.header);
        title = (TextView)findViewById(R.id.title);
        genre = (TextView)findViewById(R.id.genre);
        rating = (TextView)findViewById(R.id.rating);
        overview = (TextView)findViewById(R.id.overview);
        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        back = (ImageView)findViewById(R.id.bg);
        Bundle extras = getIntent().getExtras();
// extra (Bundle) untuk membungkus kiriman yang akan diterima dari kelas sebelumnya yang akan di pakai di kelas ini
        if (extras!=null){
            id = extras.getInt("id");
// Menjalankan methode fetch() untuk mengambil data dari API
            fetch();

        }else {
            //jika pernyataan di atas salah maka kemabali ke activity
            startActivity(new Intent(DetailRateActivity.this,MainActivity.class));
            finish();

        }
//        untuk menjalankan bar layout

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.d(DetailRateActivity.class.getSimpleName(),"onOffsetChanged: verticalOffset"+ verticalOffset);

                //Vertical offset == 0 indicates appBar is fully expanded.
                if(Math.abs(verticalOffset)>200){
                    appBarExpanded = false;
                    invalidateOptionsMenu();
                }else {
                    appBarExpanded = true;
                    invalidateOptionsMenu();
                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Methode fetch untuk menjalankan API

    private void fetch(){

        ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);
        Call<Details> call = apiservice.getMovieDetails(id,API_KEY);

        //request api
        call.enqueue(new Callback<Details>() {
            @Override
            public void onResponse(Call<Details> call, Response<Details> response) {
                //if response is success do fetch value response to target view
                int statusCode = response.code();
                Details movies = response.body();
                if(movies.getReleaseDate()!=""){
                    String year = movies.getReleaseDate().substring(0,4);
                    collapsingToolbarLayout.setTitle(movies.getTitle()+" ("+year+")");
                }else if(movies.getReleaseDate()==""){
                    collapsingToolbarLayout.setTitle(movies.getTitle());
                }
                Glide.with(getApplicationContext()).load("https://image.tmdb.org/t/p/w342"+movies.getPosterPath()+"?api_key="+API_KEY)
                        .thumbnail(0.5f)
                        .crossFade()
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(header);
                Glide.with(getApplicationContext()).load("https://image.tmdb.org/t/p/w342"+movies.getPosterPath()+"?api_key="+API_KEY)
                        .thumbnail(0.5f)
                        .crossFade()
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(back);

                //decapsulation of genres serializable
                List<Genre> genr = response.body().getGenreIds();
                for(Genre s : genr){
                    gen += "\u2022 "+s.getName()+"\n";
                }
                //settext to target view
                title.setText(movies.getTitle());
                rating.setText("TMDB Rating : "+String.valueOf(movies.getVoteAverage()));
                overview.setText(movies.getOverview());
                genre.setText("Genre : \n"+gen);

            }
            @Override
            public void onFailure(Call<Details> call, Throwable t) {
                // Log error here since request failed
                Snackbar snackbar = Snackbar
                        .make(appBarLayout, "No Data", Snackbar.LENGTH_LONG);
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.red));
                snackbar.show();
            }
        });
    }
}
