package app.com.testapp.Room.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.List;

import app.com.testapp.Controller.DataRepository;
import app.com.testapp.Listeners.ResultListener;
import app.com.testapp.MyApplication;
import app.com.testapp.Room.dao.MemberInfoDao;
import app.com.testapp.Room.models.MemberInfo;

public class LocalRepository {
    private static AppDatabase appDatabase;
    private static final Object LOCK = new Object();
    private static DataRepository dataRepository = new DataRepository();
    private static Context context;

    public LocalRepository(Context context) {
        this.context = context;
    }

    public synchronized static AppDatabase getDealsDatabase(Context context) {
        if (appDatabase == null) {
            synchronized (LOCK) {
                if (appDatabase == null) {
                    appDatabase = Room.databaseBuilder(context,
                            AppDatabase.class, "MemberInfo")
                            .fallbackToDestructiveMigration()
                            .addCallback(dbCallback).build();

                }
            }
        }
        return appDatabase;
    }
    public MemberInfoDao getMemberInfoDao(Context context){
        return getDealsDatabase(context).memberInfoDao();
    }

    public LiveData<List<MemberInfo>> getMemberInfoList(Context context){
        return getMemberInfoDao(context).getAll();
    }
    /*public Cursor getDealsCursor(Context context){
        return getMemberInfoDao(context).;
    }*/
    private static RoomDatabase.Callback dbCallback = new RoomDatabase.Callback(){
        public void onCreate (SupportSQLiteDatabase db){

        }
        public void onOpen (SupportSQLiteDatabase db){
            //first delete existing data and insert laates deals
            db.execSQL("Delete From MemberInfo");

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

        }
    };
}
