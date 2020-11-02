package app.com.testapp.Sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Main.db";
    private static final String TABLE_NAME = "MemberInfo";
    private static final String COL_1 = "idx";
    private static final String COL_2 = "postId";
    private static final String COL_3 = "id";
    private static final String COL_4 = "name";
    private static final String COL_5 = "email";
    private static final String COL_6 = "body";
    //data types
    private static final String DATA_TYPE_1 = "INTEGER PRIMARY KEY AUTOINCREMENT";
    private static final String DATA_TYPE_2 = "varchar(200)";
    public static final String DATA_TYPE_3 = "int(20)";
    public static final String DATA_TYPE_4 = "char(64)";
    private static final String DATA_TYPE_5 = "text";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + COL_1 + " " + DATA_TYPE_1 + ", " +
                COL_2 + " " + DATA_TYPE_3 + ", " +
                COL_3 + " " + DATA_TYPE_3 + ", " +
                COL_4 + " " + DATA_TYPE_2 + ", " +
                COL_5 + " " + DATA_TYPE_5 + ", " +
                COL_6 + " " + DATA_TYPE_5 + ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(Context context, int postId, int id, String name, String email, String body ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, postId);
        contentValues.put(COL_3, id);
        contentValues.put(COL_4, name);
        contentValues.put(COL_5, email);
        contentValues.put(COL_6, body);
        long rowInserted = db.insert(TABLE_NAME, null, contentValues);
        db.close();
        if (rowInserted != -1) {
//            CommonUtils.showToastMessage(context, "Saved");
            return true;
        } else {
            /*CommonUtils.showToastMessage(context, "Error");*/
            return false;
        }

    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }

    public Cursor getSelectedItemDetails(String notesTitle) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.query
                (
                        TABLE_NAME,
                        new String[]{COL_2, COL_3, COL_4},
                        COL_2 + "=?",
                        new String[]{notesTitle}, null, null, null, null
                );
        return res;
    }

    public void updateNotes(Context context, String[] noteItems, String oldNotesTitle) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (noteItems == null) {
            /*CommonUtils.showToastMessage(context, "Save failed");*/
            return;
        }
        ContentValues cv = new ContentValues();
        cv.put(COL_2, noteItems[0]);
        cv.put(COL_3, noteItems[1]);
        cv.put(COL_4, noteItems[2]);
//        CommonUtils.showToastMessage(context, "Items--> " + noteItems[0] + "\n" + noteItems[1]);
        long result = db.update(TABLE_NAME, cv, COL_2 + "=?", new String[]{oldNotesTitle});
        db.close();
        if (result > 0) {
            /*CommonUtils.showToastMessage(context, "Save successful-->" + result);
            CommonUtils.showToastMessage(context, "Items--> " + noteItems[0] + "\n" + noteItems[1] + oldNotesTitle );*/
        } else {
//            CommonUtils.showToastMessage(context, "Something went wrong-->" + result);
        }
    }

    public boolean deleteNotes(String noteTitle) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COL_2 + "=?", new String[]{noteTitle}) > 0;
    }

    /*public Cursor orderBy(String sortOption) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("DatabaseHelper", "orderBy--->sortOption--> " + sortOption);
        if (sortOption.equalsIgnoreCase(Constants.ASCENDING)) {
            Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + COL_2 + " ASC", null);
            Log.d("DatabaseHelper", "orderBy--->check_c_null--> " + c.getColumnName(0));
            return c;
        } else if (sortOption.equalsIgnoreCase(Constants.DESCENDING)) {
            Cursor c  = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + COL_2 + " DESC", null);
            Log.d("DatabaseHelper", "orderBy--->check_c_null--> " + c.getColumnName(0));
            return c;
        } else if (sortOption.equalsIgnoreCase(Constants.DATE_MODIFIED)) {
            Cursor c  = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY datetime(" + COL_5 + ") DESC", null);
            Log.d("DatabaseHelper", "orderBy--->check_c_null--> " + c.getColumnName(0));
            return c;
        } else {
            Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
            return res;
        }
    }*/

}
