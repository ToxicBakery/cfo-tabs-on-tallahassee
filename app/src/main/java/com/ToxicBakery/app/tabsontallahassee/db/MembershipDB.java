package com.ToxicBakery.app.tabsontallahassee.db;

import android.content.Context;
import android.support.annotation.NonNull;

import com.ToxicBakery.app.tabsontallahassee.models.Membership;
import com.ToxicBakery.app.tabsontallahassee.models.OrganizationDetail;

public class MembershipDB extends SimpleIdReferenceDB<Membership> {

    static MembershipDB instance;

    protected MembershipDB(@NonNull Context context) {
        super(context);
    }

    public static MembershipDB getInstance(@NonNull Context context) {
        if (instance == null) {
            synchronized (MembershipDB.class) {
                if (instance == null) {
                    instance = new MembershipDB(context);
                }
            }
        }

        return instance;
    }

}
