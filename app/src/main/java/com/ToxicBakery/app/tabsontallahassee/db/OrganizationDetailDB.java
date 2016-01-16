package com.ToxicBakery.app.tabsontallahassee.db;

import android.content.Context;
import android.support.annotation.NonNull;

import com.ToxicBakery.app.tabsontallahassee.models.OrganizationDetail;

public class OrganizationDetailDB extends SimpleIdReferenceDB<OrganizationDetail> {

    static OrganizationDetailDB instance;

    protected OrganizationDetailDB(@NonNull Context context) {
        super(context);
    }

    public static OrganizationDetailDB getInstance(@NonNull Context context) {
        if (instance == null) {
            synchronized (OrganizationDetailDB.class) {
                if (instance == null) {
                    instance = new OrganizationDetailDB(context);
                }
            }
        }

        return instance;
    }

}
