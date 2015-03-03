package com.trainings.android.factquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends Activity {
	
	private static final String TAG = "QuizActivity";
	private static final String KEY_INDEX = "index";
	private static final String KEY_CHEATER = "cheater";
	
	private Button mTrueButton;
	private Button mFalseButton;
	private ImageButton mNextButton;
	private ImageButton mPrevButton;
	private Button mCheatButton;
	
	private TextView mQuestionTextView;
	
	private TrueFalse[] mQuestionBank = new TrueFalse[] {
			new TrueFalse(R.string.question_animal, false, false),
			new TrueFalse(R.string.question_capital, true, false),
			new TrueFalse(R.string.question_color, false, false),
			new TrueFalse(R.string.question_mountain, true, false),
			new TrueFalse(R.string.question_russia, false, false),
	};
	
	private int mCurrentIndex = 0;
	
	private void UpdateQuestion() {
		int question = mQuestionBank[mCurrentIndex].getQuestion();
		mQuestionTextView.setText(question);
	}
	
	private void CheckAnswer(boolean userAnswerIsTrue) {
		boolean correctAnswerIsTrue = mQuestionBank[mCurrentIndex].isTrueQuestion();
		
		int messageResId = 0;
		
		if (mQuestionBank[mCurrentIndex].isCheated()) {
			messageResId = R.string.cheat_toast;
		}
		else {
			if (userAnswerIsTrue == correctAnswerIsTrue) {
				messageResId = R.string.correct_toast;
			}
			else {
				messageResId = R.string.incorrect_toast;
			}
		}
		
		Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
	}
	
	@Override
	protected void onActivityResult (int requestCode, int resultCode, Intent data) {
		if (data == null) {
			return;
		}
		
		boolean cheater = data.getBooleanExtra(CheatActivity.EXTRA_ANSWER_SHOWN, false);
		mQuestionBank[mCurrentIndex].setCheated(cheater);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate() called");
		
		if (savedInstanceState != null) {
			mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
			boolean cheater = savedInstanceState.getBoolean(KEY_CHEATER, false);
			mQuestionBank[mCurrentIndex].setCheated(cheater);
		}
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz);
		
		mQuestionTextView = (TextView)findViewById(R.id.question_text_view);
		mQuestionTextView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
				UpdateQuestion();
			}
		});
		
		mTrueButton = (Button)findViewById(R.id.true_button);
		mTrueButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CheckAnswer(true);				
			}
		});
		
		mFalseButton = (Button)findViewById(R.id.false_button);
		mFalseButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CheckAnswer(false);
			}
		});
		
		mNextButton = (ImageButton)findViewById(R.id.next_button);
		mNextButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
				UpdateQuestion();
			}
		});
		
		mPrevButton = (ImageButton)findViewById(R.id.prev_button);
		mPrevButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mCurrentIndex = (mQuestionBank.length + (mCurrentIndex - 1)) % mQuestionBank.length;
				UpdateQuestion();
			}
		});

		
		mCheatButton = (Button)findViewById(R.id.cheat_button);
		mCheatButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(QuizActivity.this, CheatActivity.class);
				boolean trueAnswer = mQuestionBank[mCurrentIndex].isTrueQuestion();
				intent.putExtra(CheatActivity.EXTRA_TRUE_ANSWER, trueAnswer);
				startActivityForResult(intent, 0);
			}
		});
		
		UpdateQuestion();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_quiz, menu);
		return true;
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
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		Log.d(TAG, "onSaveInstanceState() called");
		
		outState.putInt(KEY_INDEX, mCurrentIndex);
		outState.putBoolean(KEY_CHEATER, mQuestionBank[mCurrentIndex].isCheated());
	}

}
