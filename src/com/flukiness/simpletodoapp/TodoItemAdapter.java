package com.flukiness.simpletodoapp;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TodoItemAdapter extends ArrayAdapter<TodoItem> {

	private Context context;
	private static int textViewResourceId = android.R.layout.simple_list_item_2;
	private static DateFormat df = DateFormat.getDateInstance();
	
	public TodoItemAdapter(Context context, ArrayList<TodoItem> items) {
		super(context, textViewResourceId, items);
		this.context = context;
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
			TextView dateView = (TextView) view.findViewById(android.R.id.text2);
			if (dateView != null) {
				String dateStr = "";
				if (item.dueDate != null) {
					dateStr = df.format(item.dueDate);
					if (dateStr.equals(df.format(new Date()))) {
						dateStr = context.getString(R.string.label_today);
					}
				}
				dateView.setText(dateStr);
			}
		}
		
		return view;
	}

}
