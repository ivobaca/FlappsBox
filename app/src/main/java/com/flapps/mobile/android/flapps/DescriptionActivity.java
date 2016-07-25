package com.flapps.mobile.android.flapps;

import com.flapps.mobile.android.flapps.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class DescriptionActivity extends BackIconActivity {

	public Intent resultIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        setTitle(R.string.activity_description_title);
        
        Bundle b = getIntent().getExtras();
        String text = b.getString("description");
        
        resultIntent = new Intent();
        resultIntent.putExtra("description", text);
        setResult(RESULT_OK, resultIntent);
        
        EditText et = (EditText) findViewById(R.id.text_description);
        et.setText(text);
	}

	public void done(View v) {
		EditText et = (EditText) findViewById(R.id.text_description);
		DescriptionActivity.this.resultIntent.putExtra("description", et.getText().toString() );
		DescriptionActivity.this.setResult(RESULT_OK, resultIntent);
		DescriptionActivity.this.finish();
	}

}
