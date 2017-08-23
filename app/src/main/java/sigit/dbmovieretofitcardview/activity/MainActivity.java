package sigit.dbmovieretofitcardview.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sigit.dbmovieretofitcardview.Fragment.MovieRate;
import sigit.dbmovieretofitcardview.Fragment.MovieTopPopular;
import sigit.dbmovieretofitcardview.Fragment.NowPlayingFragment;
import sigit.dbmovieretofitcardview.R;
import sigit.dbmovieretofitcardview.helper.CircleTransform;
import sigit.dbmovieretofitcardview.model.Search;
import sigit.dbmovieretofitcardview.model.SearchResponse;
import sigit.dbmovieretofitcardview.rest.ApiClient;
import sigit.dbmovieretofitcardview.rest.ApiInterface;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
//    private RecyclerView recyclerView;

   // Deklarasi Komponen
    private BottomNavigationView navigationView;
    NavigationView nView;
    View navHeader;

    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;

    MaterialSearchView searchView;
    ListView listView;
    List<SearchResponse> lsresponse;
    private static final String urlColepsing = "http://allswalls.com/images/black-cat-creative-wallpaper-1.jpg";
    private static final String urlNavHeaderBg = "http://api.androidhive.info/images/nav-menu-header-bg.jpg";
    private static final String urlProfileImg = "https://www.programmableweb.com/sites/default/files/styles/facebook_scale_width_200/public/the-movie-db-api.png?itok=ImW3MQle";


    private static final String TAG = MainActivity.class.getSimpleName();

    private final static String API_KEY ="cbbfb3a08172212bc7d32daa19bd7d42";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        Deklarasi Variable
        searchView = (MaterialSearchView)findViewById(R.id.search_view);
        listView =(ListView)findViewById(R.id.listView);
//        klik item listener pada listView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object lv =listView.getItemAtPosition(i);
                searchView.setQuery(lv.toString(),false);
                listView.setAdapter(null);
                searchView.closeSearch();
                Intent intent = new Intent(getApplicationContext(),DetailSearchActivity.class);
                intent.putExtra("query",lv.toString());
                startActivity(intent);
            }
        });
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        nView = (NavigationView) findViewById(R.id.nav_view);
        nView.setNavigationItemSelectedListener(this);
        navHeader = nView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        imgNavHeaderBg = (ImageView)navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView)navHeader.findViewById(R.id.img_profile);

        loadNavHeader();

//        //display fragment by param id
        displaySelectedScreen(R.id.nav_home);
        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
//        metode untuk menjalankan coolepsingLayout
        initCollapsingToolbar();

//        menjalankan navigasi bottom layout
        navigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.navigation_rate:
                                //get selected bottom navigation item to avoid looping commit fragment
                                if(navigationView.getMenu().findItem(R.id.navigation_rate).isChecked()){
                                    return true;
                                }else{
                                    selectedFragment = MovieRate.newInstance();
                                }
                                break;
                            case R.id.navigation_movie:
                                if(navigationView.getMenu().findItem(R.id.navigation_movie).isChecked()){
                                    return true;
                                }else{
                                    selectedFragment = MovieTopPopular.newInstance();

                                }
                                break;
                            case R.id.navigation_tv:
                                if(navigationView.getMenu().findItem(R.id.navigation_tv).isChecked()){
                                    return true;
                                }else{
                                    selectedFragment = NowPlayingFragment.newInstance();
                                }
                                break;
                        }
                        //displaying fragment on selected navigation menu item
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.content, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, MovieRate.newInstance());
        transaction.commit();
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener(){
            @Override
            public void onSearchViewShown(){

            }
            @Override
            public void onSearchViewClosed(){
                listView.setAdapter(null);
                listView.animate();
            }
        });

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query){
                Intent intent = new Intent(getApplicationContext(),DetailSearchActivity.class);
                intent.putExtra("query",query);
                listView.setAdapter(null);

                startActivity(intent);
                return false;
            }
            @Override
            public boolean onQueryTextChange(final String newText){
                if (newText!= null && !newText.isEmpty()){
                    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                    //get URL
                    Call<Search>call = apiInterface.getSearch(API_KEY,newText);
//                    requet URL
                    call.enqueue(new Callback<Search>() {
                        @Override
                        public void onResponse(Call<Search> call, Response<Search> response) {
                            int statusCode = response.code();
                            //fetch response value to movie mosel class
                            lsresponse = response.body().getResults();
                            //avoid force close the app while change frgment
                            List<String> ls = new ArrayList<String>();
                            for (SearchResponse item : lsresponse ){
                                if (item.getTitle().toLowerCase(Locale.getDefault()).contains(newText))
                                    ls.add(item.getTitle());
                            }
                            ArrayAdapter adapter = new ArrayAdapter(MainActivity.this,android.R.layout.simple_list_item_1,ls);
                            listView.setAdapter(adapter);

                        }

                        @Override
                        public void onFailure(Call<Search> call, Throwable t) {

                        }
                    });

                }else {
                    listView.setAdapter(null);
                }
                return true;
            }
        });



        try {
            Glide.with(this).load(urlColepsing)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into((ImageView)findViewById(R.id.backdrop));
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    private void loadNavHeader() {
        // name, website
        txtName.setText("Movie DB");
        txtWebsite.setText("https://www.themoviedb.org/");

        // loading header background image
        Glide.with(this).load(urlNavHeaderBg)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgNavHeaderBg);

        // Loading profile image
        Glide.with(this).load(urlProfileImg)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);

        // showing dot next to notifications label
//        nView.getMenu().getItem(3).setActionView(R.layout.menu_dot);
    }

    public void onBackPressed(){
        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }
    private void displaySelectedScreen(int itemId){
        //Creating Fragment Object which is selected
        Fragment fragment = null;
        switch (itemId){
            case R.id.nav_home:
                fragment = new Fragment();
                break;
            case R.id.nav_photos:
                fragment = new Fragment();
                break;
            case R.id.nav_movies:
                fragment = new Fragment();
                break;
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.content, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        //calling the method displayselectedscreen and passing the id of selected menu
        displaySelectedScreen(item.getItemId());
        return true;
    }


    //inizilizing collepsing toolbar
    //will show and hide the toolbar tittle and scroll

    private void initCollapsingToolbar(){
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(" ");
        final AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        //hiding & Showing the title when toolbar expended and collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener(){
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout1, int verticalOffset){
                if (scrollRange == -1){
                    scrollRange = appBarLayout1.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset ==0){
                    collapsingToolbarLayout.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow){
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.serach,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }
    public boolean onCreateOptionsMen(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_drawer, menu);
        return true;
    }

}
