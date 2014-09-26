package edu.furb.exemplobanco;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import edu.fubr.exemplobanco.R;

public class MainActivity extends ListActivity implements OnItemClickListener, OnClickListener, OnItemLongClickListener{

	private DatabaseHelper helper;
	private String selecionado;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		helper = new DatabaseHelper(this);
		getListView().setOnItemClickListener(this);
		getListView().setOnItemLongClickListener(this);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		carregarLista();
	}

	private void carregarLista() {
		String[] from = new String[]{"nome", "telefone"};
		int[] to = new int[]{android.R.id.text1, android.R.id.text2};
		setListAdapter(new SimpleAdapter(this, listarClientes(), android.R.layout.simple_list_item_2, from, to));
	}
	
	private List<Map<String, String>> listarClientes() {
		List<Map<String, String>> dados = new ArrayList<>();
		
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT _id, nome, telefone FROM clientes ORDER BY nome", null);
		cursor.moveToFirst();
		for(int i = 0; i < cursor.getCount(); i++){
			Map<String, String> rec = new HashMap<>();
			rec.put("_id", cursor.getString(0));
			rec.put("nome", cursor.getString(1));
			rec.put("telefone", cursor.getString(2));
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
	
	@Override
	protected void onDestroy() {
		helper.close();
		super.onDestroy();
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
		Map<String,String> registro = (Map<String, String>) adapter.getItemAtPosition(position);
		selecionado = registro.get("_id");
		acaoDialog().show();
	}

	private void editar() {
		Intent i = new Intent(this, ClienteForm.class);
		i.putExtra("_id", selecionado);
		startActivity(i);
		Toast.makeText(this, "Registro " + selecionado, Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> adapter, View view, int position,
			long id) {
		Map<String,String> registro = (Map<String, String>) adapter.getItemAtPosition(position);
		selecionado = registro.get("_id"); 
		excluir();
		return true;		
	}

	private void excluir() {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("clientes", "_id = ?", new String[]{selecionado});
		Toast.makeText(this, "Registro deletado " + selecionado, Toast.LENGTH_SHORT).show();
		carregarLista();
	}
	
	private AlertDialog acaoDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.dialog_titulo));
		builder.setItems(new CharSequence[]{getString(R.string.dialog_editar),getString(R.string.dialog_excluir)}, this);
		
		return builder.create();
	}
	
	private AlertDialog confirmationDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.dialog_confirma));
		builder.setPositiveButton(getString(R.string.dialog_sim), new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				excluir();
			}
		});
		builder.setNegativeButton(getString(R.string.dialog_nao), null);
		return builder.create();
	}

	@Override
	public void onClick(DialogInterface dialog, int item) {
		switch (item) {
		case 0:
			editar();
			break;
		case 1:
			confirmationDialog().show();
			break;
		}
	}
}
