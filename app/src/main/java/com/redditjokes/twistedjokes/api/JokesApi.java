package com.redditjokes.twistedjokes.api;






import com.redditjokes.twistedjokes.api.models.OurJoke;
import com.redditjokes.twistedjokes.api.models.ResultsUserJoke;
import com.redditjokes.twistedjokes.api.models.UserJoke;

import java.util.List;

import io.reactivex.Flowable;


import retrofit2.http.Body;
import retrofit2.http.GET;


import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JokesApi {


//      @Headers("Authorization : Basic YWRtaW46ZWxpdGVtZWR1bGEwOTAw")
        @GET("api/twistedjokes/jokes/")
        Flowable<List<OurJoke>> getAllOurJokes();

        @GET("api/twistedjokes/jokes/{id}")
        Flowable<OurJoke> getOurJokeById(@Path("id") String id);

        @PATCH("api/twistedjokes/jokes/{id}/")
        Flowable<OurJoke> updateOurJoke(@Body OurJoke userJoke, @Path("id") String  jokeId);

        /*For searching with build_ups use '_' with query*/
        @GET("api/twistedjokes/userjokes/")
        Flowable<ResultsUserJoke> getAllUserJokes(@Query("page") int pageNumber);

        @GET("api/twistedjokes/userjokes/{id}")
        Flowable<UserJoke> getUserJokeById(@Path("id") String id);

        @POST("api/twistedjokes/userjokes/")
        Flowable<UserJoke> adduserJoke(@Body UserJoke userJoke);

        @PATCH("api/twistedjokes/userjokes/{id}/")
        Flowable<UserJoke> updateUserJoke(@Body UserJoke userJoke, @Path("id") String  jokeId);

        @GET("api/twistedjokes/userjokes/") // ?q={id/category/title}
        Flowable<ResultsUserJoke> getUserJokesOrderedBy (@Query("ordering") String ordering_field, @Query("page") int pageNumber);


        @GET("api/twistedjokes/userjokes/") // ?q={id/category/title}
        Flowable<ResultsUserJoke> getUserJokesQueryAndOrderedBy (@Query("ordering") String ordering_field, @Query("page") int pageNumber,@Query("page") String query);


}
