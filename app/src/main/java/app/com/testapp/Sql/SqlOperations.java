package app.com.testapp.Sql;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import app.com.testapp.Controller.DataRepository;
import app.com.testapp.Listeners.ResultListener;
import app.com.testapp.Room.models.MemberInfo;

public class SqlOperations {

    private Context context;
    private static int insertCount = 0;

    public SqlOperations() {
    }

    public static void insertServerData(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        new DataRepository().getMemberInfoListFromServer(context, new ResultListener() {
            @Override
            public void getResult(Object object, boolean isSuccess) {
                if (isSuccess) {
                    List<MemberInfo> memberInfoList = (List<MemberInfo>) object;
                    databaseHelper.onUpgrade(databaseHelper.getWritableDatabase(),
                            databaseHelper.getWritableDatabase().getVersion(),
                            databaseHelper.getWritableDatabase().getVersion() + 1);
                    if (memberInfoList != null) {
                        for (int i = 0; i < memberInfoList.size(); i++) {
                            MemberInfo m = memberInfoList.get(i);
                            boolean isInserted = databaseHelper.insertData(context,
                                    m.getPostId(),
                                    m.getId(),
                                    m.getName(),
                                    m.getEmail(),
                                    m.getBody()
                            );
                            if (isInserted) {
                                insertCount++;
                                Log.d("count-->", "insertCount" + insertCount);
                            }
                        }


                    }

                }
            }
        });
    }

    public static List<MemberInfo> getDataFromSql(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        List<MemberInfo> memberInfoList = new ArrayList<>();
        Cursor res = databaseHelper.getAllData();
        if (res.moveToFirst()) {
            while (!res.isAfterLast()) {
                MemberInfo memberInfo = new MemberInfo();
                memberInfo.setPostId(res.getInt(res.getColumnIndex("postId")));
                memberInfo.setId(res.getInt(res.getColumnIndex("id")));
                memberInfo.setName(res.getString(res.getColumnIndex("name")));
                memberInfo.setEmail(res.getString(res.getColumnIndex("email")));
                memberInfo.setBody(res.getString(res.getColumnIndex("body")));
                memberInfoList.add(memberInfo);
                res.moveToNext();
            }
        }
        return memberInfoList;
    }
}


