package edu.furb.exemplobanco;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASENAME_NAME = "contatos.sqlite";
	private static final int DATABASE_VERSION = 1;

	public DatabaseHelper(Context context) {
		super(context, DATABASENAME_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE clientes ( ");
		sb.append("_id INTEGER PRIMARY KEY, ");
		sb.append("nome TEXT, ");
		sb.append("documento TEXT, ");
		sb.append("email TEXT, ");
		sb.append("telefone TEXT)");
		db.execSQL(sb.toString());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

}
