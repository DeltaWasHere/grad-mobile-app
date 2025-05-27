package com.example.completionistguild.callInterface;

import com.example.completionistguild.model.modelGameInfo;
import com.example.completionistguild.model.modelGameView;
import com.example.completionistguild.model.modelGuides;
import com.example.completionistguild.model.modelLeaderEntry;
import com.example.completionistguild.model.modelStatus;
import com.example.completionistguild.model.modelTags;
import com.example.completionistguild.model.modelThread;
import com.example.completionistguild.model.modelTrade;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

public interface Calls {

    @GET("search/game")
    Call<ArrayList<modelGameInfo>> getGames(@HeaderMap Map<String, String> headers, @Query("game") String game);

    @GET("view/game/{gameId}")
    Call<modelGameView> getAchievements(@HeaderMap Map<String, String> headers, @Path("gameId") String gameId);

    @GET("rate/{gameId}")
    Call<ResponseBody> rate(@HeaderMap Map<String, String> headers, @Path("gameId") String gameId);

    @GET("pin/{userId}")
    Call<modelStatus> pin(@HeaderMap Map<String, String> headers, @Path("userId") String userId);

    @GET("tag/{transaction}")
    Call<modelTags> tags(@HeaderMap Map<String, String> headers, @Path("transaction") String transaction);

    @POST("guide/{transaction}")
    Call<ArrayList<modelGuides>> guides(@HeaderMap Map<String, String> headers, @Body Map<String, String> body, @Path("transaction") String transaction);

    @GET("leaderboard/{type}")
    Call<ArrayList<modelLeaderEntry>> getLeader(@HeaderMap Map<String, String> headers, @Path("type") String type);

    @GET("vote/{vote}")
    Call<Boolean> vote(@HeaderMap Map<String, String> headers, @Path("vote") String vote);

    @GET("profile/{transaction}")
    Call<JsonElement> profile(@HeaderMap Map<String, String> headers, @Path("transaction") String transaction);

    @GET("profile/check")
    Call<Boolean> checkProfile(@HeaderMap Map<String, String> headers);

    @POST("trade/{transaction}")
    Call<ArrayList<modelTrade>> getTrade(@HeaderMap Map<String, String> headers, @Path("transaction") String transaction);

    @POST("trade/{transaction}")
    Call<Boolean> tradeTransaction(@HeaderMap Map<String, String> headers, @Body Map<String, String> body, @Path("transaction") String transaction);

    @Multipart
    @POST("trade/{transaction}")
    Call<Boolean> addTrade(@HeaderMap Map<String, String> headers, @PartMap Map<String, String> body, @Part MultipartBody.Part validation, @Path("transaction") String transaction);

    @POST("trade/getKey")
    Call<String> getKey(@HeaderMap Map<String, String> headers);

    @POST("trade/{transaction}")
    Call<ArrayList<modelTrade>> getOffers(@HeaderMap Map<String, String> headers, @Body Map<String, String> body, @Path("transaction") String transaction);

    @Streaming
    @POST("trade/getMedia")
    Call<ResponseBody> getMedia(@Body Map<String, String> body);

    @Multipart
    @POST("thread/create")
    Call<Boolean> addThread(@HeaderMap Map<String, String> headers,  @PartMap Map<String, String> body, @Part MultipartBody.Part validation);

    @POST("thread/readAll")
    Call<ArrayList<modelThread>> getThreads(@HeaderMap Map<String, String>headers);

    @Streaming
    @POST("thread/getMedia")
Call<ResponseBody> getThreadmedia(@Body Map<String, String> body);

}
