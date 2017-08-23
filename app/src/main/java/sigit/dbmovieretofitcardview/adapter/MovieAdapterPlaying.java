package sigit.dbmovieretofitcardview.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import sigit.dbmovieretofitcardview.R;
import sigit.dbmovieretofitcardview.activity.AddRating;
import sigit.dbmovieretofitcardview.activity.DetailRateActivity;
import sigit.dbmovieretofitcardview.model.Res;
import sigit.dbmovieretofitcardview.model.Result;

/**
 * Created by sigit on 09/08/17.
 */

public class MovieAdapterPlaying extends RecyclerView.Adapter<MovieAdapterPlaying.MyViewHolder> {
    private Context mContext;
    private List<Result> mResult;
    private ImageLoader imageLoader;
    private DisplayImageOptions displayImageOptions;
    private final static String API_KEY ="cbbfb3a08172212bc7d32daa19bd7d42";

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title, count;
        public ImageView thumbnail, overflow;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);

        }
    }
    public MovieAdapterPlaying (List<Result> result, Context context) {
        this.mContext = context;
        this.mResult = result;
    }

    @Override
    public MovieAdapterPlaying.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_card, parent, false);

        return new MovieAdapterPlaying.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MovieAdapterPlaying.MyViewHolder holder, int position) {
        final Result result = mResult.get(position);
        holder.title.setText(result.getTitle());
        holder.count.setText("Rating : "+result.getVoteAverage().toString());

        //loading album using cover using glide liblary
        Glide.with(mContext).load("https://image.tmdb.org/t/p/h632"+result.getPosterPath()+"?api_key="+API_KEY)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.thumbnail);
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),DetailRateActivity.class);
                intent.putExtra("id",result.getId());
                view.getContext().startActivity(intent);
            }
        });

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
