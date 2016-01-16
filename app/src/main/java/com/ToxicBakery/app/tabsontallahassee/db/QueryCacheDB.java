package com.ToxicBakery.app.tabsontallahassee.db;

import android.content.Context;
import android.support.annotation.NonNull;

public class QueryCacheDB extends SimpleIdReferenceDB<LastQuery> {

    static QueryCacheDB instance;

    protected QueryCacheDB(@NonNull Context context) {
        super(context);
    }

    public static QueryCacheDB getInstance(@NonNull Context context) {
        if (instance == null) {
            synchronized (PersonDB.class) {
                if (instance == null) {
                    instance = new QueryCacheDB(context);
                }
            }
        }

        return instance;
    }

}
