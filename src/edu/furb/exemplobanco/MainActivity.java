package edu.furb.exemplobanco;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.fubr.exemplobanco.R;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MainActivity extends Activity {

	private DatabaseHelper helper;
	private ListView listClientes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		helper = new DatabaseHelper(this);
		listClientes = (ListView) findViewById(R.id.listClientes);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		String[] from = new String[]{"nome", "telefone"};
		int[] to = new int[]{android.R.id.text1, android.R.id.text2};
		SimpleAdapter adapter = new SimpleAdapter(this, listarClientes(), android.R.layout.simple_list_item_2, from, to);
		listClientes.setAdapter(adapter);
	}
	
	private List<Map<String, String>> listarClientes() {
		List<Map<String, String>> dados = new ArrayList<>();
		
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT nome, telefone FROM clientes ORDER BY nome", null);
		cursor.moveToFirst();
		for(int i = 0; i < cursor.getCount(); i++){
			Map<String, String> rec = new HashMap<>();
			rec.put("nome", cursor.getString(0));
			rec.put("telefone", cursor.getString(1));
			dados.add(rec);
			cursor.moveToNext();
		}
		return dados;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.novo_cliente) {
			Intent i = new Intent(this, ClienteForm.class);
			startActivity(i);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
