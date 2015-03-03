package com.trainings.android.factquiz;

public class TrueFalse {
	private int mQuestion;	
	private boolean mTrueQuestion;
	private boolean mCheated;
	
	public TrueFalse (int question, boolean trueQuestion, boolean cheated) {
		mQuestion = question;
		mTrueQuestion = trueQuestion;
		mCheated = cheated;
	}

	public int getQuestion() {
		return mQuestion;
	}

	public void setQuestion(int question) {
		mQuestion = question;
	}

	public boolean isTrueQuestion() {
		return mTrueQuestion;
	}

	public void setTrueQuestion(boolean trueQuestion) {
		mTrueQuestion = trueQuestion;
	}

	public boolean isCheated() {
		return mCheated;
	}

	public void setCheated(boolean cheated) {
		mCheated = cheated;
	}

}
