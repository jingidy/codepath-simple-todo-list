package com.flukiness.simpletodoapp;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ToggleButton;

public class EditItemDialog extends DialogFragment {
	EditText etEditItem;
	Button btnSave;
	ToggleButton tbtnDueDate;
	DatePicker dpDueDate;
	int itemPosition;
	Date dueDate;
	
	public interface EditItemDialogListener {
		void onEditFinished(int itemPosition, String itemText, Date itemDueDate);
	}
	
	public EditItemDialog() {
		// Required empty constructor.
	}
	
	public static EditItemDialog newInstance(String title, int itemPosition, 
			TodoItem item) {
		
		EditItemDialog fragment = new EditItemDialog();
		Bundle args = new Bundle();
		args.putString("title", title);
		args.putInt("itemPosition", itemPosition);
		args.putString("itemText", item.name);
		args.putSerializable("itemDueDate", item.dueDate);
		
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_edit_item, container);
		etEditItem = (EditText) view.findViewById(R.id.etEditItem);
		btnSave = (Button) view.findViewById(R.id.btnSave);
		tbtnDueDate = (ToggleButton) view.findViewById(R.id.tbtnDueDate);
		dpDueDate = (DatePicker) view.findViewById(R.id.dpDueDate);
		
		Bundle args = getArguments();
		getDialog().setTitle(args.getString("title"));
		itemPosition = args.getInt("itemPosition");
		String itemText = args.getString("itemText");
		etEditItem.setText(itemText);
		etEditItem.selectAll();
		dueDate = (Date)args.getSerializable("itemDueDate");
		if (dueDate != null) {
			GregorianCalendar c = new GregorianCalendar();
			c.setTime(dueDate);
			dpDueDate.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), 
					c.get(Calendar.DATE));
			if (!tbtnDueDate.isChecked()) {
				tbtnDueDate.toggle();
			}
		}
		updateDueDateVisibility(null);

		tbtnDueDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				updateDueDateVisibility(v);
			}
		});
		
		btnSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finishEditing(v);
			}
		});
		
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

		return view;
	}

	public void updateDueDateVisibility(View v) {
		dpDueDate.setVisibility(tbtnDueDate.isChecked() ? View.VISIBLE : View.GONE);
	}

	public void finishEditing(View v) {
		boolean isEmpty = etEditItem.getText().toString().trim().isEmpty();
		if (isEmpty) {
			dismiss();
			return;
		}
		
		String itemText = etEditItem.getText().toString();
		if (tbtnDueDate.isChecked()) {
			GregorianCalendar c = new GregorianCalendar(dpDueDate.getYear(), 
					dpDueDate.getMonth(), dpDueDate.getDayOfMonth());
			dueDate = c.getTime();
		} else {
			dueDate = null;
		}

		EditItemDialogListener listener = (EditItemDialogListener)getActivity();
		listener.onEditFinished(itemPosition,  itemText, dueDate);
		dismiss();
	}

	private void validate() {
		boolean isEmpty = etEditItem.getText().toString().trim().isEmpty();
		btnSave.setText(isEmpty ? R.string.button_cancel : R.string.button_save);
	}
}
