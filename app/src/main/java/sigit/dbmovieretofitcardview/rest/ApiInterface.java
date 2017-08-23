package sigit.dbmovieretofitcardview.rest;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import sigit.dbmovieretofitcardview.model.Cast;
import sigit.dbmovieretofitcardview.model.Delete;
import sigit.dbmovieretofitcardview.model.Details;
import sigit.dbmovieretofitcardview.model.GuestSession;
import sigit.dbmovieretofitcardview.model.Movie;
import sigit.dbmovieretofitcardview.model.MoviePopular;
import sigit.dbmovieretofitcardview.model.NowPlaying;
import sigit.dbmovieretofitcardview.model.Rating;
import sigit.dbmovieretofitcardview.model.Result;
import sigit.dbmovieretofitcardview.model.Search;
import sigit.dbmovieretofitcardview.model.TV;

/**
 * Created by sigit on 01/08/17.
 */

public interface ApiInterface {

    @POST("movie/{id}/rating")
    @FormUrlEncoded
    Call<Rating> saveRating(@Path("id") int id, @Field("value") float rating,@Query("api_key")String apiKey,@Query("guest_session_id")String guest_session_id);

    @DELETE("movie/{id}/rating")
    Call<Delete> deleteRating(@Path("id") int id, @Field("value") float rating,@Query("api_key")String apiKey,@Query("guest_session_id")String guest_session_id);
    @GET("movie/top_rated")
    Call<Movie> getTopRatedMovies(@Query("api_key")String apiKey);

    @GET("authentication/guest_session/new")
    Call<GuestSession> getGuess( @Query("api_key")String apiKey);

    @GET("movie/popular")
    Call<MoviePopular> getPopular(@Query("api_key") String apiKey);

    @GET("movie/now_playing")
    Call<NowPlaying> getNowPlaying(@Query("api_key") String apiKey);


//    @GET("movie/{id}")
//    Call<Result>getMovieDetails(@Path("id") int id, @Query("api_key")String apiKey);

    @GET("movie/{id}/credits")
    Call<Cast> getCredits(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/{id}")
    Call<Details> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("search/movie")
    Call<Search> getSearch(@Query("api_key") String apiKey, @Query("query") String query);


}
