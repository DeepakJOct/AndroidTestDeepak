package app.com.testapp.Controller;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import app.com.testapp.Listeners.ApiListener;
import app.com.testapp.Listeners.ResultListener;
import app.com.testapp.Listeners.RetrofitServiceListener;
import app.com.testapp.Model.models.Comment;
import app.com.testapp.Model.models.WebImage;
import app.com.testapp.Model.utils.CommonUtils;
import app.com.testapp.MyApplication;
import app.com.testapp.Room.models.MemberInfo;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class ApiOperations {

    public static final ApiOperations ourInstance = new ApiOperations();

    public static ApiOperations newInstance() {
        return ourInstance;
    }

    public ApiOperations() {

    }

    public void getCommentsResponse(Context context, ResultListener resultListener) {
        if(CommonUtils.checkInternetConnection(context)) {
            //Create an object of Call<ResponseBody>
            Call<ResponseBody> responseBodyCall;
            responseBodyCall = RetrofitClient.create().getCommentsResponse();

            //require a callback
            responseBodyCall.enqueue(new ApiListener(new RetrofitServiceListener() {
                @Override
                public void onSuccess(String result, int pos, Throwable t) {
                    Log.d("Response--> Comments-->", "Result--> " + result);
                    parseMemberInfoResponse(context, result, resultListener);
                }
            }, "Getting Data", true, context));
        } else {
            CommonUtils.showNoInternet(context);
        }
    }

    public void parseMemberInfoResponse(final Context context, String result, ResultListener resultListener) {
        try {
            JSONArray objectList = new JSONArray(result);
            List<MemberInfo> resultList = new ArrayList<>();
            for (int i = 0; i < objectList.length(); i++) {
                JSONObject object = objectList.getJSONObject(i);
                MemberInfo memberInfo = new MemberInfo();
                memberInfo.setId(object.getInt("id"));
                memberInfo.setName(object.getString("name"));
                memberInfo.setEmail(object.getString("email"));
                memberInfo.setBody(object.getString("body"));
                resultList.add(memberInfo);
            }
            resultListener.getResult(resultList, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getPhotosResponse(Context context, ResultListener resultListener) {
        if(CommonUtils.checkInternetConnection(context)) {
            Call<ResponseBody> call = RetrofitClient.create().getPhotos();
            call.enqueue(new ApiListener(new RetrofitServiceListener() {
                @Override
                public void onSuccess(String result, int pos, Throwable t) {
                    parsePhotosResponse(context, result, resultListener);
                }
            }, "Getting Photos", true, context));
        }
    }

    public void parsePhotosResponse(final Context context, String result, ResultListener resultListener) {
        try {
//            JSONArray objectList = new JSONArray(result);
            Log.d("result-->", result.toString());
            Gson gson = new GsonBuilder().create();
            List<WebImage> resultList = gson.fromJson(result, new TypeToken<List<WebImage>>() {}.getType());
            resultListener.getResult(resultList, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
