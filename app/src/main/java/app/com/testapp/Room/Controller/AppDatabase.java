package app.com.testapp.Room.Controller;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import app.com.testapp.Room.dao.MemberInfoDao;
import app.com.testapp.Room.models.MemberInfo;

@Database(entities = {MemberInfo.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MemberInfoDao memberInfoDao();
}
