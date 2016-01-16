package com.ToxicBakery.app.tabsontallahassee.fragment;

import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ToxicBakery.app.tabsontallahassee.R;
import com.ToxicBakery.app.tabsontallahassee.api.retrofit.RetrofitService;
import com.ToxicBakery.app.tabsontallahassee.api.retrofit.TabsAPI;
import com.ToxicBakery.app.tabsontallahassee.db.LastQuery;
import com.ToxicBakery.app.tabsontallahassee.db.PersonDB;
import com.ToxicBakery.app.tabsontallahassee.db.QueryCacheDB;
import com.ToxicBakery.app.tabsontallahassee.models.PersonPagedList;
import com.ToxicBakery.app.tabsontallahassee.recycler.PersonAdapter;
import com.ToxicBakery.app.tabsontallahassee.recycler.PersonItemDividerDecoration;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FragmentPersonList extends Fragment {

    public static final String TAG = "FragmentPersonList";

    private TabsAPI tabsAPI;
    private PersonDB personDB;
    private QueryCacheDB queryCacheDB;
    private PersonAdapter personAdapter;
    private Subscription personSubscription;
    private GridLayoutManager gridLayoutManager;
    private boolean loading;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tabsAPI = RetrofitService.getInstance()
                .getTabsApi();

        personDB = PersonDB.getInstance(getContext());
        queryCacheDB = QueryCacheDB.getInstance(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_person_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        personAdapter = new PersonAdapter(personDB);

        @SuppressWarnings("deprecation")
        PersonItemDividerDecoration personItemDividerDecoration = new PersonItemDividerDecoration(
                getResources().getColor(android.R.color.black),
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics())
        );

        gridLayoutManager = new GridLayoutManager(
                getContext(),
                getResources().getInteger(R.integer.person_spans)
        );

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setAdapter(personAdapter);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(personItemDividerDecoration);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!loading && isLastItemDisplaying()) {
                    loadPeople();
                }
            }
        });

        if (savedInstanceState == null) {
            loadPeople();
        }
    }

    @Override
    public void onDestroy() {
        if (personSubscription != null) {
            personSubscription.unsubscribe();
        }

        super.onDestroy();
    }

    boolean isLastItemDisplaying() {
        if (personAdapter.getItemCount() != 0) {
            int lastVisibleItemPosition = gridLayoutManager.findLastCompletelyVisibleItemPosition();

            if (lastVisibleItemPosition != RecyclerView.NO_POSITION
                    && lastVisibleItemPosition == personAdapter.getItemCount() - 1) {

                return true;
            }
        }

        return false;
    }

    void loadPeople() {
        if (!loading) {
            LastQuery lastQuery = queryCacheDB.getById(TAG);
            if (lastQuery == null) {
                personSubscription = loadPeople(1);
            } else if (lastQuery.getNextPage() != -1) {
                personSubscription = loadPeople(lastQuery.getNextPage());
                personAdapter.notifyDataSetChanged();
            } else {
                personAdapter.notifyDataSetChanged();
            }
        }
    }

    Subscription loadPeople(@IntRange(from = 1) final int page) {

        loading = true;

        return tabsAPI.getPersonList(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PersonPagedList>() {

                    LastQuery lastQuery;

                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "Loaded person list");
                        personAdapter.notifyDataSetChanged();

                        // Go forward
                        if (lastQuery != null) {
                            queryCacheDB.addOrReplace(lastQuery);
                        }

                        loading = false;
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Failed to get person list", e);

                        loading = false;
                    }

                    @Override
                    public void onNext(PersonPagedList personPagedList) {
                        Log.d(TAG, "Found " + personPagedList.getData().length + " person.");
                        personDB.addOrReplaceAll(personPagedList.getData());

                        if (lastQuery == null) {
                            int pages = personPagedList.getMeta()
                                    .getPagination()
                                    .getPages();

                            int nextPage = page < pages ? page + 1 : -1;
                            lastQuery = new LastQuery(TAG, page, nextPage);
                        }
                    }

                });
    }

}
