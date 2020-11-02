package app.com.testapp.Controller;

import android.util.Log;

import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit;
    private static ApiService apiService;

    public static final String URL = "https://jsonplaceholder.typicode.com/";
    public static final String PHOTOS_BASE_URL = "https://api.npoint.io/";
    public static final String PHOTO_URL = PHOTOS_BASE_URL + "/photos/";



    public RetrofitClient() {
    }

    public static ApiService create() {
        if(apiService == null) {
            apiService = getClient(URL).create(ApiService.class);
        }
        return apiService;
    }

    public static ApiService createPhotosApi() {
        if(apiService == null) {
            apiService = getClient(PHOTOS_BASE_URL).create(ApiService.class);
        }
        return apiService;
    }

    public static Retrofit getClient(final String url) {
        if(retrofit == null) {
            //Use loggin interceptor to log all the requests to logcat
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    Request.Builder builder = original.newBuilder();
                    builder.addHeader("Language", "en");
                    builder.method(original.method(), original.body());
                    Request request = builder.build();
                    return chain.proceed(request);
                }
            }).readTimeout(240, TimeUnit.SECONDS)
                    .connectTimeout(240, TimeUnit.SECONDS)
                    .addInterceptor(interceptor)
                    .build();
            Log.d("RetrofitClient-->URL-->", url);

            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


}
