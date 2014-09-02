package com.flukiness.simpletodoapp;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;


public class TodoActivity extends Activity {
	private static final int EDIT_REQUEST_CODE = 0;
	
	ArrayList<TodoItem> items;
	TodoItemAdapter itemsAdapter;
	ListView lvItems;
	EditText etNewItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        lvItems = (ListView) findViewById(R.id.lvItems);
        etNewItem = (EditText) findViewById(R.id.etNewItem);
        
        readItems();
        itemsAdapter = new TodoItemAdapter(this, items);
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
    		TodoItem item = new TodoItem(text);
    		itemsAdapter.add(item);
        	item.save();
    	}
    	
    	etNewItem.setText("");
    }
    
    public void editTodoItem(int itemPosition) {
    	Intent i = new Intent(this, EditItemActivity.class);
    	TodoItem item = items.get(itemPosition);
    	i.putExtra("itemText", item.name);
    	i.putExtra("itemPosition", itemPosition);
    	if (item.dueDate != null) {
    		i.putExtra("itemDueDate", item.dueDate);
    	}
    	startActivityForResult(i, EDIT_REQUEST_CODE);
    }
    
    public void removeTodoItem(int itemPosition) {
    	TodoItem item = items.remove(itemPosition);
		itemsAdapter.notifyDataSetChanged();
		item.delete();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// Finish editing the item
    	if (requestCode == EDIT_REQUEST_CODE && resultCode == RESULT_OK) {
    		String itemText = data.getExtras().getString("itemText");
    		int itemPosition = data.getExtras().getInt("itemPosition");
    		Date dueDate = (Date)data.getExtras().getSerializable("itemDueDate");
    		
    		if (itemPosition == -1) {
    			System.out.println("*** Uh oh, the index of the edited item could not be found!");
    		} else {
    			TodoItem item = items.get(itemPosition);
    			item.name = itemText;
    			item.dueDate = dueDate;
    			itemsAdapter.notifyDataSetChanged();
    			item.save();
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
    	items = new ArrayList<TodoItem>(TodoItem.getAll());
    }
}
