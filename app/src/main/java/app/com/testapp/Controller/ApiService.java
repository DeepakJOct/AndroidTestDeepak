package app.com.testapp.Controller;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    /**
     * Gets login response.
     *
     *
     * @return the login response
     */
    @GET("comments")
    Call<ResponseBody> getCommentsResponse();

    @GET("posts")
    Call<ResponseBody> getPostsResponse();

    @POST("employees")
    Call<ResponseBody> getEmployees();

    @GET("photos")
    Call<ResponseBody> getPhotos();


    //json bin
    @POST("c23cb86c2bab2fe03238")
    Call<ResponseBody> getPhotosJson();

}
