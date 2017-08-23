package sigit.dbmovieretofitcardview.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sigit.dbmovieretofitcardview.Album;
import sigit.dbmovieretofitcardview.R;
import sigit.dbmovieretofitcardview.activity.AddRating;
import sigit.dbmovieretofitcardview.activity.DetailRateActivity;
import sigit.dbmovieretofitcardview.model.Delete;
import sigit.dbmovieretofitcardview.model.Details;
//import sigit.dbmovieretofitcardview.model.Result;
import sigit.dbmovieretofitcardview.model.GuestSession;
import sigit.dbmovieretofitcardview.model.Results;
import sigit.dbmovieretofitcardview.rest.ApiClient;
import sigit.dbmovieretofitcardview.rest.ApiInterface;

import static android.R.attr.id;

/**
 * Created by sigit on 08/08/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {
    private Context mContext;
    private List<Results> mResult;
    private ImageLoader imageLoader;
    float rating = 0.0f;
    private DisplayImageOptions displayImageOptions;
    private final static String API_KEY ="cbbfb3a08172212bc7d32daa19bd7d42";
    String idGuesst;
    private static final String TAG = AddRating.class.getSimpleName();
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title, count;
        public ImageView thumbnail, overflow, overflow2;


        public MyViewHolder(View view) {
            super(view);

            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);


        }

    }
    public MovieAdapter (List<Results> result, Context context) {
        this.mContext = context;
        this.mResult = result;
    }

    @Override
    public MovieAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_card, parent, false);

        return new MovieAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MovieAdapter.MyViewHolder holder, int position) {
        final Results result = mResult.get(position);
        holder.title.setText(result.getTitle());
        holder.count.setText("Rating : "+result.getVoteAverage().toString());

        //load album using cover using glide liblary
        Glide.with(mContext).load("https://image.tmdb.org/t/p/h632"+result.getPosterPath()+"?api_key="+API_KEY)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.thumbnail);
        //click image for view detail ACtivity
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),DetailRateActivity.class);
                intent.putExtra("id",result.getId());
                view.getContext().startActivity(intent);
            }
        });
        //click image overfow untuk menampilkan popup menu

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(),AddRating.class);
//                guesst();
                intent.putExtra("id",result.getId());
//                intent.putExtra("idGuess",idGuesst);
                view.getContext().startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mResult.size();
    }





}

