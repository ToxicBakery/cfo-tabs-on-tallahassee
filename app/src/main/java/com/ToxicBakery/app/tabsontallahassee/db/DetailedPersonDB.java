package com.ToxicBakery.app.tabsontallahassee.db;

import android.content.Context;
import android.support.annotation.NonNull;

import com.ToxicBakery.app.tabsontallahassee.models.Person;

public class DetailedPersonDB extends SimpleIdReferenceDB<Person> {

    static DetailedPersonDB instance;

    protected DetailedPersonDB(@NonNull Context context) {
        super(context);
    }

    public static DetailedPersonDB getInstance(@NonNull Context context) {
        if (instance == null) {
            synchronized (DetailedPersonDB.class) {
                if (instance == null) {
                    instance = new DetailedPersonDB(context);
                }
            }
        }

        return instance;
    }

}
