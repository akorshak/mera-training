package com.training.android.texteditor;

import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class EditorActivity extends Activity {

	EditText editText;
	SharedPreferences preferences;
	final String SAVE_TEXT_KEY = "save_text_key";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editor);
		
		editText = (EditText)findViewById(R.id.edit_text);

		String text = "";
		try {
			preferences = getPreferences(MODE_PRIVATE);
			text = preferences.getString(SAVE_TEXT_KEY, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	    editText.setText(text);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_editor, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.menu_item_save:
			SaveText();
		    Toast.makeText(this, R.string.toast_saved, Toast.LENGTH_SHORT).show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		SaveText();
	    super.onPause();
	}
	
	void SaveText() {
		preferences = getPreferences(MODE_PRIVATE);
	    Editor ed = preferences.edit();
	    ed.putString(SAVE_TEXT_KEY, editText.getText().toString());
	    ed.commit();
	}

}
