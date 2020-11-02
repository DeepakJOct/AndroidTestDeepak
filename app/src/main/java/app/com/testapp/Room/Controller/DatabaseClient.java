package app.com.testapp.Room.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.google.firebase.database.DatabaseReference;

import java.util.List;

import app.com.testapp.Controller.DataRepository;
import app.com.testapp.Listeners.ResultListener;
import app.com.testapp.Listeners.SuccessListener;
import app.com.testapp.MyApplication;
import app.com.testapp.Room.dao.MemberInfoDao;
import app.com.testapp.Room.models.MemberInfo;

public class DatabaseClient {
    private Context context;
    private static DatabaseClient mInstance;

    private DataRepository dataRepository = new DataRepository();

    //our database object
    private AppDatabase appDatabase;
    private SuccessListener listener;

    public DatabaseClient(Context context) {
        this.context = context;
        appDatabase = Room.databaseBuilder(context, AppDatabase.class, "MemberInfo")
                .fallbackToDestructiveMigration()
                .addCallback(dbCallback)
                .build();
    }


    public static synchronized DatabaseClient getInstance(Context context) {
        if(mInstance == null) {
            mInstance = new DatabaseClient(context);
        }
        return mInstance;
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }

    /*public List<MemberInfo> getMemberInfoList() {
        return getAppDatabase().memberInfoDao().getAll();
    }*/

    public RoomDatabase.Callback dbCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            db.execSQL("Delete from MemberInfo");
            dataRepository.getMemberInfoListFromServer(context, new ResultListener() {
                @Override
                public void getResult(Object object, boolean isSuccess) {
                    if(isSuccess) {
                        List<MemberInfo> list = (List<MemberInfo>) object;
                        Log.d("List-->DBClient::", list.size() + "");
                        for(MemberInfo mi : list) {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("postId", mi.getPostId());
                            contentValues.put("id", mi.getId());
                            contentValues.put("name", mi.getName());
                            contentValues.put("email", mi.getEmail());
                            contentValues.put("body", mi.getBody());
                            db.insert("MemberInfo", OnConflictStrategy.IGNORE, contentValues);
                        }
                    }
                }
            });

            /*for (int i = 0; i < memberInfoList.size() ; i++) {
                MemberInfo mi = memberInfoList.get(i);
                if(mi != null) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("postId", mi.getPostId());
                    contentValues.put("id", mi.getId());
                    contentValues.put("name", mi.getName());
                    contentValues.put("email", mi.getEmail());
                    contentValues.put("body", mi.getBody());
                }
            }*/
            /*for(MemberInfo mi : memberInfoList) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("postId", mi.getPostId());
                contentValues.put("id", mi.getId());
                contentValues.put("name", mi.getName());
                contentValues.put("email", mi.getEmail());
                contentValues.put("body", mi.getBody());
                db.insert("MemberInfo", OnConflictStrategy.IGNORE, contentValues);
            }*/
        }
    };
}
