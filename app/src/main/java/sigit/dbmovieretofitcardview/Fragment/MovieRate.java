package sigit.dbmovieretofitcardview.Fragment;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.PUT;
import sigit.dbmovieretofitcardview.R;
import sigit.dbmovieretofitcardview.activity.MainActivity;
import sigit.dbmovieretofitcardview.adapter.MovieRateAdapter;
import sigit.dbmovieretofitcardview.model.Movie;
import sigit.dbmovieretofitcardview.model.Result;
import sigit.dbmovieretofitcardview.rest.ApiClient;
import sigit.dbmovieretofitcardview.rest.ApiInterface;


public class MovieRate extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private final static String API_KEY ="cbbfb3a08172212bc7d32daa19bd7d42";

    private static final String TAG = MainActivity.class.getSimpleName();
//deklarasi

    private MovieRateAdapter mAdapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    public static MovieRate newInstance() {
        MovieRate fragment = new MovieRate();
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //menjalankan layout fragment_movie_rate diatas layout utama dengan bantuan inflater

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        return inflater.inflate(R.layout.fragment_movie_rate, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InitView(view);
        getActivity().setTitle("");

    }

    private void InitView(View view){
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        swipeRefreshLayout =(SwipeRefreshLayout)view.findViewById(R.id.swiprefreshlayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark);
        swipeRefreshLayout.setOnRefreshListener(this);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this.getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MovieRate.GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());



    }

    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        fetch(true);
    }



    //list in rate movie
    private void fetch(boolean get){
        if(get){
            //init retrofit build & interface class
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            Call<Movie> call = apiService.getTopRatedMovies(API_KEY);
            call.enqueue(new Callback<Movie>() {
                @Override
                public void onResponse(Call<Movie> call, Response<Movie> response) {
                    int statusCode = response.code();
                    swipeRefreshLayout.setRefreshing(false);
                    List<Result> result = response.body().getResults();
                    recyclerView.setAdapter(new MovieRateAdapter(result,getActivity().getApplicationContext()));
                }

                @Override
                public void onFailure(Call<Movie> call, Throwable t) {
                    Log.e(TAG, t.toString());

                }
            });

        }
        if (!get){
            swipeRefreshLayout.setRefreshing(false);
        }

    }
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect rect, View view, RecyclerView parent, RecyclerView.State state){
            int position = parent.getChildAdapterPosition(view);//item position
            int column = position % spanCount;// item colomn

            if (includeEdge){
                rect.left = spacing - column * spacing / spanCount;
                rect.right = (column + 1) * spacing / spanCount;

                if (position<spanCount){// top edge
                    rect.top = spacing;
                }
                rect.bottom = spacing; //item bottom
            } else {
                rect.left = column * spacing / spanCount;
                rect.right = spacing - (column + 1)* spacing /spanCount;
                if (position>= spanCount){
                    rect.top = spacing;
                }
            }

        }
    }

    private int dpToPx(int dp){
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
    @Override
    public void onRefresh(){
        fetch(true);
    }
}
