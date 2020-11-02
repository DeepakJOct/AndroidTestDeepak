package app.com.testapp.Controller;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import app.com.testapp.Listeners.ResultListener;
import app.com.testapp.Listeners.SuccessListener;
import app.com.testapp.Model.models.WebImage;
import app.com.testapp.Model.utils.Prefs;
import app.com.testapp.Room.Controller.AppDatabase;
import app.com.testapp.Room.models.MemberInfo;
import app.com.testapp.Sql.DatabaseHelper;

public class DataRepository {

    public void getMemberInfoListFromServer(Context context, ResultListener resultListener) {
        List<MemberInfo> memberInfoList = new ArrayList<>();
        ApiOperations.newInstance().getCommentsResponse(context, new ResultListener() {
            @Override
            public void getResult(Object object, boolean isSuccess) {
                List<MemberInfo> list = (List<MemberInfo>) object;
                memberInfoList.addAll(list);
                Log.d("DataRepo::", list.size() + "<--mainList memberInfoList:" + memberInfoList.size());
                resultListener.getResult(memberInfoList, true);

            }
        });
    }

    public void getPhotosFromServer(Context context, ResultListener resultListener) {
        List<WebImage> webImageList = new ArrayList<>();
        ApiOperations.newInstance().getPhotosResponse(context, new ResultListener() {
            @Override
            public void getResult(Object object, boolean isSuccess) {
                resultListener.getResult(object, true);
            }
        });
    }
}
