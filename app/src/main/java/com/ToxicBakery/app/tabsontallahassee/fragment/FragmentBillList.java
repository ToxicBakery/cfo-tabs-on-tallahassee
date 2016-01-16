package com.ToxicBakery.app.tabsontallahassee.fragment;

import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ToxicBakery.app.tabsontallahassee.R;
import com.ToxicBakery.app.tabsontallahassee.api.retrofit.RetrofitService;
import com.ToxicBakery.app.tabsontallahassee.api.retrofit.TabsAPI;
import com.ToxicBakery.app.tabsontallahassee.db.BillDB;
import com.ToxicBakery.app.tabsontallahassee.db.LastQuery;
import com.ToxicBakery.app.tabsontallahassee.db.QueryCacheDB;
import com.ToxicBakery.app.tabsontallahassee.models.BillPagedList;
import com.ToxicBakery.app.tabsontallahassee.models.PersonPagedList;
import com.ToxicBakery.app.tabsontallahassee.recycler.BillAdapter;
import com.ToxicBakery.app.tabsontallahassee.recycler.DividerItemDecoration;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FragmentBillList extends Fragment {

    public static final String TAG = "FragmentBillList";

    private TabsAPI tabsAPI;
    private BillDB billDB;
    private QueryCacheDB queryCacheDB;
    private Subscription billSubscription;
    private BillAdapter billAdapter;
    private LinearLayoutManager linearLayoutManager;
    private boolean loading;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tabsAPI = RetrofitService.getInstance()
                .getTabsApi();

        billDB = BillDB.getInstance(getContext());
        queryCacheDB = QueryCacheDB.getInstance(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bill_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        billAdapter = new BillAdapter(billDB);

        @SuppressWarnings("deprecation")
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), null);

        linearLayoutManager = new LinearLayoutManager(getContext());

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setAdapter(billAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!loading && isLastItemDisplaying()) {
                    loadBills();
                }
            }
        });

        if (savedInstanceState == null) {
            loadBills();
        }
    }

    @Override
    public void onDestroy() {
        if (billSubscription != null) {
            billSubscription.unsubscribe();
        }

        super.onDestroy();
    }

    boolean isLastItemDisplaying() {
        if (billAdapter.getItemCount() != 0) {
            int lastVisibleItemPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();

            if (lastVisibleItemPosition != RecyclerView.NO_POSITION
                    && lastVisibleItemPosition == billAdapter.getItemCount() - 1) {

                return true;
            }
        }

        return false;
    }

    void loadBills() {
        if (!loading) {
            LastQuery lastQuery = queryCacheDB.getById(TAG);
            if (lastQuery == null) {
                billSubscription = loadBills(71);
            } else if (lastQuery.getNextPage() != -1) {
                billSubscription = loadBills(lastQuery.getNextPage());
            } else {
                billAdapter.notifyDataSetChanged();
            }
        }
    }

    Subscription loadBills(@IntRange(from = 1) final int page) {

        loading = true;

        return tabsAPI.getBillsList(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BillPagedList>() {

                    LastQuery lastQuery;

                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "Loaded bill list");
                        billAdapter.notifyDataSetChanged();

                        // Go forward
                        if (lastQuery != null) {
                            queryCacheDB.addOrReplace(lastQuery);
                        }

                        loading = false;
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Failed to get bill list", e);

                        loading = false;
                    }

                    @Override
                    public void onNext(BillPagedList billPagedList) {
                        Log.d(TAG, "Found " + billPagedList.getData().length + " bill.");
                        billDB.addOrReplaceAll(billPagedList.getData());

                        if (lastQuery == null) {
                            int nextPage = page - 1;
                            lastQuery = new LastQuery(TAG, page, nextPage);
                        }
                    }

                });
    }

}
