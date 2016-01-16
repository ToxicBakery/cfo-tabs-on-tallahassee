package com.ToxicBakery.app.tabsontallahassee.db;

import android.content.Context;
import android.support.annotation.NonNull;

import com.ToxicBakery.app.tabsontallahassee.models.Bill;

public class BillDetailDB extends SimpleIdReferenceDB<Bill> {

    static BillDetailDB instance;

    protected BillDetailDB(@NonNull Context context) {
        super(context);
    }

    public static BillDetailDB getInstance(@NonNull Context context) {
        if (instance == null) {
            synchronized (BillDetailDB.class) {
                if (instance == null) {
                    instance = new BillDetailDB(context);
                }
            }
        }

        return instance;
    }

}
