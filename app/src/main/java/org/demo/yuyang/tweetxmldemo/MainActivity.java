package org.demo.yuyang.tweetxmldemo;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import org.demo.yuyang.tweetxmldemo.fragment.ListFragment;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();

        ListFragment fragment = new ListFragment();

        transaction.add(R.id.fragment_container, fragment);

        transaction.commit();
    }
}
