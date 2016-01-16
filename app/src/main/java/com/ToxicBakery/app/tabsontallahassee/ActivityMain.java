package com.ToxicBakery.app.tabsontallahassee;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.ToxicBakery.app.tabsontallahassee.api.retrofit.RetrofitService;
import com.ToxicBakery.app.tabsontallahassee.api.retrofit.TabsAPI;
import com.ToxicBakery.app.tabsontallahassee.db.OrganizationDetailDB;
import com.ToxicBakery.app.tabsontallahassee.fragment.FragmentBillList;
import com.ToxicBakery.app.tabsontallahassee.fragment.FragmentPersonList;
import com.ToxicBakery.app.tabsontallahassee.models.OrganizationDetail;
import com.ToxicBakery.app.tabsontallahassee.models.OrganizationDetailPagedList;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ActivityMain extends AppCompatActivity {

    private static final String TAG = "ActivityMain";

    private ViewPagerAdapter viewPagerAdapter;
    private OrganizationDetailDB organizationDetailDB;
    private TabsAPI tabsAPI;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Explode explode = new Explode();
        explode.setDuration(400);

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(explode);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

/*        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        organizationDetailDB = OrganizationDetailDB.getInstance(getApplicationContext());
        tabsAPI = RetrofitService.getInstance()
                .getTabsApi();

        loadOrganizationDetails();

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this);

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(viewPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void loadOrganizationDetails() {
        if (organizationDetailDB.size() == 0) {
            tabsAPI.getOrganizationDetailList(1)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<OrganizationDetailPagedList>() {

                        @Override
                        public void onCompleted() {
                            Log.d(TAG, "Loaded organizations");
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG, "Failed to get organizations.", e);
                        }

                        @Override
                        public void onNext(OrganizationDetailPagedList organizationDetailPagedList) {
                            Log.d(TAG, "Loaded organizations " + organizationDetailPagedList.getData().length);
                            OrganizationDetail[] data = organizationDetailPagedList.getData();
                            organizationDetailDB.addOrReplaceAll(data);
                        }

                    });
        }
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {

        private final FragmentTitlePair[] fragmentTitlePairs;

        public ViewPagerAdapter(@NonNull FragmentManager fm,
                                @NonNull Context context) {

            super(fm);

            fragmentTitlePairs = new FragmentTitlePair[]{
                    new FragmentTitlePair(new FragmentPersonList(), context.getString(R.string.fragment_person_list)),
                    new FragmentTitlePair(new FragmentBillList(), context.getString(R.string.fragment_bill_list))
            };
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentTitlePairs[position].getFragment();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitlePairs[position].getTitle();
        }

        @Override
        public int getCount() {
            return fragmentTitlePairs.length;
        }

    }

    static class FragmentTitlePair extends Pair<Fragment, String> {

        public FragmentTitlePair(@NonNull Fragment first,
                                 @NonNull String second) {

            super(first, second);
        }

        public Fragment getFragment() {
            return first;
        }

        public String getTitle() {
            return second;
        }

    }

}
