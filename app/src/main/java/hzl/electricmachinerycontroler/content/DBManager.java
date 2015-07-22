package hzl.electricmachinerycontroler.content;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Iterator;
import java.util.List;

/**
 * Created by YLion on 2015/4/12.
 */
public class DBManager {
	private MySQLite helper;
	static private SQLiteDatabase db;
	private Context context;


	public DBManager(Context context, int version) {
		helper = new MySQLite(context, version);
		//因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
		//所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
		db = helper.getWritableDatabase();
	}

	public DBManager(Context context) {
		this.context=context;
		helper = new MySQLite(context);
		db = helper.getWritableDatabase();
	}

	public void addNotebookdata(notebookdata notebookdata) {
		db.beginTransaction();
		try {
			db.execSQL("INSERT INTO notebookdata VALUES(?,?, ?,?)", new Object[]{notebookdata.id, notebookdata.content, notebookdata.version, notebookdata.type});
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	static public String getLatestContent(String type){
		String string =new String();
		int version=0;
		Cursor c = db.rawQuery("SELECT * FROM notebookdata where type = ?", new String[]{type } );
		while (c.moveToNext()) {
			if( c.getInt(c.getColumnIndex("version")) >=version )
				string=c.getString( c.getColumnIndex("content") );
		}
		c.close();
		return string;
	}

	public void closeDB() {
		db.close();
	}

	public void delete() {
		db.execSQL("drop TABLE IF EXISTS notebookdata ");
		db.execSQL("CREATE TABLE IF NOT EXISTS notebookdata" + " (_id INTEGER PRIMARY KEY AUTOINCREMENT,content VARCHAR, version INT,type VARCHAR)");
	}

	public boolean refreshnotebookdata(List<notebookdata> object) {
		boolean changeFlag=false;
		String type="";
		for (int typeNum = 501; typeNum <= 504; typeNum++) {
			if(typeNum == 501){
				type="product";
			}
			if(typeNum == 502){
				type="order";
			}
			if(typeNum == 503){
				type="test";
			}
			if(typeNum == 504){
				type="qaa";
			}
			int version = 0;
			Cursor c = db.rawQuery("SELECT * FROM notebookdata where type = ?", new String[]{type});
			while (c.moveToNext()) {
				if (c.getInt(c.getColumnIndex("version")) > version)
					version=c.getInt(c.getColumnIndex("version"));
			}
			c.close();
			for (Iterator iter = object.iterator(); iter.hasNext(); ) {
				notebookdata notebookdata = (notebookdata) iter.next();
				if(notebookdata.version>version&&notebookdata.type.equalsIgnoreCase(type)){
					//db.execSQL("update notebookdata set content = ? where version = ? and type = ? ",new Object[]{notebookdata.content,notebookdata.version,type});
					db.execSQL("INSERT INTO notebookdata VALUES(?,?, ?,?) ", new Object[]{ (notebookdata.version*typeNum), notebookdata.content, notebookdata.version, notebookdata.type});
					changeFlag=true;
				}
			}
		}
		return changeFlag;
	}




}
