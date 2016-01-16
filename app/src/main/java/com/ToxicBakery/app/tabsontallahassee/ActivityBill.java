package com.ToxicBakery.app.tabsontallahassee;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.ToxicBakery.app.tabsontallahassee.api.retrofit.RetrofitService;
import com.ToxicBakery.app.tabsontallahassee.api.retrofit.TabsAPI;
import com.ToxicBakery.app.tabsontallahassee.db.BillDB;
import com.ToxicBakery.app.tabsontallahassee.db.BillDetailDB;
import com.ToxicBakery.app.tabsontallahassee.models.Attributes;
import com.ToxicBakery.app.tabsontallahassee.models.Bill;
import com.ToxicBakery.app.tabsontallahassee.models.BillDataWrapper;
import com.ToxicBakery.app.tabsontallahassee.recycler.BillDetailAdapter;
import com.ToxicBakery.app.tabsontallahassee.recycler.DividerItemDecoration;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ActivityBill extends AppCompatActivity {

    public static final String EXTRA_UUID = "EXTRA_UUID";

    private static final String TAG = "ActivityBill";

    private TabsAPI tabsAPI;
    private BillDetailDB billDetailDB;
    private String uuid;
    private BillDetailAdapter billDetailAdapter;
    private Subscription subscription;

    private TextView billTitle;
    private TextView billIdentifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        tabsAPI = RetrofitService.getInstance()
                .getTabsApi();

        billDetailDB = BillDetailDB.getInstance(getApplicationContext());

        billTitle = (TextView) findViewById(R.id.bill_title);
        billIdentifier = (TextView) findViewById(R.id.bill_identifier);

        billDetailAdapter = new BillDetailAdapter();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, null);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setAdapter(billDetailAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);

        Intent intent = getIntent();
        if (intent != null) {
            uuid = intent.getStringExtra(EXTRA_UUID);
        }

        if (uuid != null) {
            loadBill();

            Bill bill = BillDB.getInstance(getApplicationContext())
                    .getById(uuid);

            if (bill != null) {
                Attributes attributes = bill.getAttributes();
                billTitle.setText(attributes.getTitle());

                attributes.getClassification();
                billIdentifier.setText(attributes.getIdentifier());
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (subscription != null) {
            subscription.unsubscribe();
        }

        super.onDestroy();
    }

    void updateView(Bill bill) {
        billDetailAdapter.set(bill);
    }

    void notifyBillLoaded() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Bill bill = billDetailDB.getById(uuid);
                updateView(bill);
            }
        });
    }

    void loadBill() {
        Bill bill = billDetailDB.getById(uuid);
        if (bill == null) {
            loadBillFromNetwork();
        } else {
            notifyBillLoaded();
        }
    }

    void loadBillFromNetwork() {
        subscription = tabsAPI.getBill(uuid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BillDataWrapper>() {
                    @Override
                    public void onCompleted() {
                        notifyBillLoaded();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Failed to load bill.", e);
                    }

                    @Override
                    public void onNext(BillDataWrapper billDataWrapper) {
                        Bill bill = billDataWrapper.getData();
                        billDetailDB.addOrReplace(bill);
                    }
                });
    }

}
