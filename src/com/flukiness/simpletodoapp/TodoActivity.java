package com.flukiness.simpletodoapp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.*;


public class TodoActivity extends Activity {
	private static final int EDIT_REQUEST_CODE = 0;
	
	ArrayList<String> items;
	ArrayAdapter<String> itemsAdapter;
	ListView lvItems;
	EditText etNewItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        lvItems = (ListView) findViewById(R.id.lvItems);
        etNewItem = (EditText) findViewById(R.id.etNewItem);
        
        readItems();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        
        setupListViewListener();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.todo, menu);
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
    
    public void addTodoItem(View v) {
    	String text = etNewItem.getText().toString();
    	// Only add non-empty items
    	if (!text.trim().isEmpty()) {    	
    		itemsAdapter.add(text);
        	saveItems();
    	}
    	
    	etNewItem.setText("");
    }
    
    public void editTodoItem(int itemPosition) {
    	Intent i = new Intent(this, EditItemActivity.class);
    	i.putExtra("itemText", items.get(itemPosition));
    	i.putExtra("itemPosition", itemPosition);
    	startActivityForResult(i, EDIT_REQUEST_CODE);
    }
    
    public void removeTodoItem(int itemPosition) {
    	items.remove(itemPosition);
		itemsAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// Finish editing the item
    	if (requestCode == EDIT_REQUEST_CODE && resultCode == RESULT_OK) {
    		String itemText = data.getExtras().getString("itemText");
    		int itemPosition = data.getExtras().getInt("itemPosition");
    		
    		if (itemPosition == -1) {
    			System.out.println("*** Uh oh, the index of the edited item could not be found!");
    		} else {
    			items.set(itemPosition, itemText);
    			itemsAdapter.notifyDataSetChanged();
    			saveItems();
    		}
    	}
    }

    private void setupListViewListener() {
    	lvItems.setOnItemLongClickListener(new OnItemLongClickListener() {
    		@Override
    		public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long rowId) {
    			removeTodoItem(position);
    			return true;
    		}
    	});
    	
    	lvItems.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				editTodoItem(position);
			}
    	});
    }
    
    private void readItems() {
    	File filesDir = getFilesDir();
    	File todoFile = new File(filesDir, "todo.txt");
    	try {
    		items = new ArrayList<String>(FileUtils.readLines(todoFile));
    	} catch (IOException e) {
    		items = new ArrayList<String>();
    		e.printStackTrace();
    	}

    }
    
    private void saveItems() {
    	File filesDir = getFilesDir();
    	File todoFile = new File(filesDir, "todo.txt");
    	try {
    		FileUtils.writeLines(todoFile,  items);
    	} catch(IOException e) {
    		e.printStackTrace();
    	}
    }
}
