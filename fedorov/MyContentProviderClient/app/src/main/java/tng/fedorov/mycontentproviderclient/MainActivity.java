package tng.fedorov.mycontentproviderclient;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private MainFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        mFragment = new MainFragment();
        transaction.replace(R.id.fragmentContainer, mFragment);
        transaction.commit();
    }

    public void onClickInsert(View view) {
        mFragment.onClickInsert(view);
    }

    public void onClickUpdate(View view) {
        mFragment.onClickUpdate(view);
    }

    public void onClickDelete(View view) {
        mFragment.onClickDelete(view);
    }
}
