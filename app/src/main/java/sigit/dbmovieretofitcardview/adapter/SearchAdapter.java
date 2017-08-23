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
import sigit.dbmovieretofitcardview.model.Results;
import sigit.dbmovieretofitcardview.model.SearchResponse;

/**
 * Created by sigit on 10/08/17.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder>  {
    private Context mContext;
    private List<SearchResponse> mSearch;
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
    public SearchAdapter (List<SearchResponse> search, Context context) {
        this.mContext = context;
        this.mSearch = search;
    }

    @Override
    public SearchAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_card, parent, false);

        return new SearchAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SearchAdapter.MyViewHolder holder, int position) {
        final SearchResponse searchResponse = mSearch.get(position);
        holder.title.setText(searchResponse.getTitle());
        holder.count.setText("Rating : "+searchResponse.getVoteAverage().toString());

        //load album using cover using glide liblary
        Glide.with(mContext).load("https://image.tmdb.org/t/p/h632"+searchResponse.getPosterPath()+"?api_key="+API_KEY)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.thumbnail);
        //click image for view detail ACtivity
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),DetailRateActivity.class);
                intent.putExtra("id",searchResponse.getId());
                view.getContext().startActivity(intent);
            }
        });
        //click image overfow untuk menampilkan popup menu

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(),AddRating.class);
//                guesst();
                intent.putExtra("id",searchResponse.getId());
//                intent.putExtra("idGuess",idGuesst);
                view.getContext().startActivity(intent);            }
        });
    }



    //methode menampilka popup menu
    private void showPopupMenu(View view) {
        //view inflate menu Popup
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_album, popup.getMenu());
        popup.setOnMenuItemClickListener(new SearchAdapter.MyMenuItemClickListener());
        popup.show();
    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        public MyMenuItemClickListener() {

        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    Toast.makeText(mContext, "Add to Favorite", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_play_next:
                    Toast.makeText(mContext, "Play next", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return mSearch.size();
    }
}
