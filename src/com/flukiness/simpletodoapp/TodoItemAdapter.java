package com.flukiness.simpletodoapp;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TodoItemAdapter extends ArrayAdapter<TodoItem> {

	private Context context;
	private int textViewResourceId;
	
	public TodoItemAdapter(Context context, int textViewResourceId, ArrayList<TodoItem> items) {
		super(context, textViewResourceId, items);
		this.context = context;
		this.textViewResourceId = textViewResourceId;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		View view =  convertView;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(textViewResourceId, null);
		}
		
		TodoItem item = (TodoItem) getItem(position);
		if (item != null) {
			TextView itemView = (TextView) view.findViewById(android.R.id.text1);
			if (itemView != null) {
				itemView.setText(item.name);
			}
		}
		
		return view;
	}

}
