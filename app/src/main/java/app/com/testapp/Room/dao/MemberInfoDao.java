package app.com.testapp.Room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import app.com.testapp.Room.models.MemberInfo;

@Dao
public interface MemberInfoDao {

    @Query("SELECT * FROM MemberInfo")
    LiveData<List<MemberInfo>> getAll();

    @Insert
    void insert(MemberInfo task);

    @Delete
    void delete(MemberInfo task);

    @Update
    void update(MemberInfo task);
}
