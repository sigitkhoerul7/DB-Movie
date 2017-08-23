package sigit.dbmovieretofitcardview.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sigit.dbmovieretofitcardview.R;
import sigit.dbmovieretofitcardview.model.Delete;
import sigit.dbmovieretofitcardview.model.Details;
import sigit.dbmovieretofitcardview.model.GuestSession;
import sigit.dbmovieretofitcardview.model.Rating;
import sigit.dbmovieretofitcardview.rest.ApiClient;
import sigit.dbmovieretofitcardview.rest.ApiInterface;

/**
 * Created by sigit on 16/08/17.
 */

public class AddRating extends AppCompatActivity {
    private static final String TAG = AddRating.class.getSimpleName();

    private final static String API_KEY ="cbbfb3a08172212bc7d32daa19bd7d42"; //Key DB MOVIE
    private TextView mResponseTv;
    String idGuesst;
    private RatingBar ratingBar;
    String string;
    int id;
    float rating;
    private Button button, button2;
    private TextView ratingVal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layoutrating);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingVal=(TextView) findViewById(R.id.ratingVal);
        mResponseTv = (TextView) findViewById(R.id.tv_response);



        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingVal.setText("Current Rating  : "+String.valueOf(rating));
            }
        });

        Bundle extras = getIntent().getExtras();
        guesst();
        if (extras!=null){
            id = extras.getInt("id");

            button = (Button) findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                Toast.makeText(AddRating.this, String.valueOf(ratingBar.getRating()), Toast.LENGTH_LONG).show();
                    float reting = ratingBar.getRating();
                    Toast.makeText(AddRating.this, String.valueOf(reting), Toast.LENGTH_LONG).show();
                    rating(reting);
                }

            });



        }else {
            startActivity(new Intent(AddRating.this,MainActivity.class));
            finish();
        }




    }
    public void rating(final float rate){
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        apiInterface.saveRating(id, rate, API_KEY, idGuesst).enqueue(new Callback<Rating>() {
            @Override
            public void onResponse(Call<Rating> call, Response<Rating> response) {
                if (response.isSuccessful()){
                     Rating rating = response.body();
                    string = rating.getStatusMessage();
                    mResponseTv.setText("Hasil :"+string);
                    Toast.makeText(getApplicationContext(),"Hello ",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplicationContext(),String.valueOf(rate),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Rating> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Hello Error",Toast.LENGTH_LONG).show();
            }
        });



    }
    public void guesst(){
        ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);
        Call<GuestSession> call = apiservice.getGuess(API_KEY);
        call.enqueue(new Callback<GuestSession>() {
            @Override
            public void onResponse(Call<GuestSession> call, Response<GuestSession> response) {
                int statusCode = response.code();
                GuestSession guestSession = response.body();

                idGuesst = guestSession.getGuestSessionId();
            }

            @Override
            public void onFailure(Call<GuestSession> call, Throwable t) {

            }
        });

    }
    public void del(float rate){
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Delete>call = apiInterface.deleteRating(id,rate,API_KEY,idGuesst);
        call.enqueue(new Callback<Delete>() {
            @Override
            public void onResponse(Call<Delete> call, Response<Delete> response) {
                if (response.isSuccessful()){
                    Delete delete = response.body();
                    string = delete.getStatusMessage();
                    mResponseTv.setText("Hasil :"+string);
                    Toast.makeText(getApplicationContext(),"Hello ",Toast.LENGTH_LONG).show();

                }else {
                    Toast.makeText(getApplicationContext(),"Error ",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Delete> call, Throwable t) {

            }
        });


    }




}
