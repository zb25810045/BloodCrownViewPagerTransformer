package com.example.zb.bloodcrownviewpagertransformer;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.zb.bloodcrownviewpagertransformer.transformer.AlpheScaleTransformer;
import com.example.zb.bloodcrownviewpagertransformer.transformer.GooglePageTransformer;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private List<Fragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
//        mViewPager.setPageTransformer( true,new GooglePageTransformer() );
        mViewPager.setPageTransformer(true, new AlpheScaleTransformer());
        mViewPager.setAdapter(new mAdapter(getSupportFragmentManager(), getmFragments()));

    }

    public List<Fragment> getmFragments() {

        ArrayList<Integer> colors = new ArrayList();
        colors.add(0xFF33B5E5);
        colors.add(0xFFAA66CC);
        colors.add(0xFF99CC00);
        colors.add(0xFFFFBB33);
        colors.add(0xFFFF4444);

        List<Fragment> mFragments = new ArrayList();
        int size = colors.size();

        for (int i = 0; i < size; i++) {
            MFragment mFragments1 = new MFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(MFragment.TAG_COLOR, colors.get(i));
            bundle.putString(MFragment.TAG_CONTENT, "页面：" + (i + 1));
            mFragments1.setArguments(bundle);
            mFragments.add(mFragments1);
        }

        return mFragments;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_google) {
            mViewPager.setPageTransformer(true, new GooglePageTransformer());
            return true;
        }

        if (id == R.id.action_scale_alphe) {
            mViewPager.setPageTransformer(true, new AlpheScaleTransformer());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
