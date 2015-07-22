package hzl.electricmachinerycontroler.content;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by YLion on 2015/4/12.
 */
public class MySQLite extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "notebookdata1.db";
	private static final int DATABASE_VERSION = 1;

	public MySQLite(Context context) {
		//CursorFactory设置为null,使用默认值
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public MySQLite(Context context, int version) {
		//CursorFactory设置为null,使用默认值
		super(context, DATABASE_NAME, null, version);
	}

	public MySQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	public MySQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("drop TABLE IF EXISTS notebookdata ");
		db.execSQL("CREATE TABLE IF NOT EXISTS notebookdata" + " (_id INTEGER PRIMARY KEY AUTOINCREMENT,content VARCHAR, version INT,type VARCHAR)");
		db.execSQL("INSERT INTO notebookdata VALUES(?,?, ?,?)", new Object[]{1,"first",0,"order"});
		db.execSQL("INSERT INTO notebookdata VALUES(?,?, ?,?)", new Object[]{2,"first",0,"test"});
		db.execSQL("INSERT INTO notebookdata VALUES(?,?, ?,?)", new Object[]{3,"first",0,"product"});
		db.execSQL("INSERT INTO notebookdata VALUES(?,?, ?,?)", new Object[]{4,"first",0,"qaa"});
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop TABLE IF EXISTS notebookdata ");
	}
}
