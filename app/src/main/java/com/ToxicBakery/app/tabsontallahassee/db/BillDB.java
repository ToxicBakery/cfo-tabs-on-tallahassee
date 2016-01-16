package com.ToxicBakery.app.tabsontallahassee.db;

import android.content.Context;
import android.support.annotation.NonNull;

import com.ToxicBakery.app.tabsontallahassee.models.Bill;

public class BillDB extends SimpleIdReferenceDB<Bill> {

    static BillDB instance;

    protected BillDB(@NonNull Context context) {
        super(context);
    }

    public static BillDB getInstance(@NonNull Context context) {
        if (instance == null) {
            synchronized (BillDB.class) {
                if (instance == null) {
                    instance = new BillDB(context);
                }
            }
        }

        return instance;
    }

}
