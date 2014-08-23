package com.flukiness.simpletodoapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditItemActivity extends Activity {
	EditText etEditItem;
	Button btnSave;
	int itemPosition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item);
		etEditItem = (EditText) findViewById(R.id.etEditItem);
		btnSave = (Button) findViewById(R.id.btnSave);
		
		itemPosition = getIntent().getIntExtra("itemPosition", -1);
		String itemText = getIntent().getStringExtra("itemText");
		etEditItem.setText(itemText);
		etEditItem.selectAll();
		
		etEditItem.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// Do nothing.
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				validate();
				
			}
			@Override
			public void afterTextChanged(Editable s) {
				// Do nothing.
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_item, menu);
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
	
	public void finishEditing(View v) {
		Intent data = new Intent();
		boolean isEmpty = etEditItem.getText().toString().trim().isEmpty();
		if (!isEmpty) {
			data.putExtra("itemText", etEditItem.getText().toString());
			data.putExtra("itemPosition", itemPosition);
		}
		setResult(isEmpty ? RESULT_CANCELED : RESULT_OK, data);
		finish();
	}
	
	private void validate() {
		boolean isEmpty = etEditItem.getText().toString().trim().isEmpty();
		btnSave.setText(isEmpty ? R.string.button_cancel : R.string.button_save);
	}
}
