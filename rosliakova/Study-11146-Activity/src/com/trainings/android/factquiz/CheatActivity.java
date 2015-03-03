package com.trainings.android.factquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends Activity {
	private static final String TAG = "CheatActivity";
	
	public static final String EXTRA_TRUE_ANSWER = 
			"com.trainings.android.factquiz.true_answer";
	public static final String EXTRA_ANSWER_SHOWN = 
			"com.trainings.android.factquiz.answer_shown";
	private static final String KEY_CHEATER = "cheater";
	
	private boolean mTrueAnswer;
	
	private TextView mAnswerTextView;
	private Button mShowAnswer;
	
	private boolean mAnswerShown;
	
	private void setAnswerShown(boolean answerShown) {
		mAnswerShown = answerShown;
		
		Intent data = new Intent();
		data.putExtra(EXTRA_ANSWER_SHOWN, mAnswerShown);
		setResult(RESULT_OK, data);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate() called");
		
		if (savedInstanceState != null) {
			setAnswerShown(savedInstanceState.getBoolean(KEY_CHEATER, false));
		}
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cheat);
		
		setAnswerShown(false);
		
		mTrueAnswer = getIntent().getBooleanExtra(EXTRA_TRUE_ANSWER, false);
		
		mAnswerTextView = (TextView)findViewById(R.id.answerTextView);
		
		mShowAnswer = (Button)findViewById(R.id.showAnswerButton);
		mShowAnswer.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (mTrueAnswer) {
					mAnswerTextView.setText(R.string.true_button);
				}
				else {
					mAnswerTextView.setText(R.string.false_button);
				}
				setAnswerShown(true);
			}
		});
	}	

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d(TAG, "onDestroy() called");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d(TAG, "onPause() called");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d(TAG, "onResume() called");
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.d(TAG, "onStart() called");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.d(TAG, "onStop() called");
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.d(TAG, "onSaveInstanceState() called");
		super.onSaveInstanceState(outState);
		
		outState.putBoolean(KEY_CHEATER, mAnswerShown);
	}

}
