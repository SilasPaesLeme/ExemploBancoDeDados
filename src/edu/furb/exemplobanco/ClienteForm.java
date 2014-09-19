package edu.furb.exemplobanco;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import edu.fubr.exemplobanco.R;

public class ClienteForm extends Activity {

	private EditText edNome;
	private EditText edDocumento;
	private EditText edTelefone;
	private EditText edEmail;
	private Button btSalvar;
	private DatabaseHelper database;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cliente);
		
		edNome = (EditText) findViewById(R.id.editNome);
		edDocumento = (EditText) findViewById(R.id.editDocumento);
		edTelefone = (EditText) findViewById(R.id.editTelefone);
		edEmail = (EditText) findViewById(R.id.editEmail);
		
		btSalvar = (Button) findViewById(R.id.btnSalvar);
		btSalvar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SQLiteDatabase db = database.getWritableDatabase();
				ContentValues values = new ContentValues();
				values.put("nome", edNome.getText().toString());
				values.put("documento", edDocumento.getText().toString());
				values.put("telefone", edTelefone.getText().toString());
				values.put("email", edEmail.getText().toString());
				db.insert("clientes", null, values);
				
				Toast.makeText(ClienteForm.this, "Cliente cadastrado com sucesso!!", Toast.LENGTH_SHORT).show();
				finish();
			}
		});
		
		database = new DatabaseHelper(this); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cliente, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
