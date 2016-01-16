package com.ToxicBakery.app.tabsontallahassee.db;

import android.content.Context;
import android.support.annotation.NonNull;

import com.ToxicBakery.app.tabsontallahassee.models.Person;

public class PersonDB extends SimpleIdReferenceDB<Person> {

    static PersonDB instance;

    protected PersonDB(@NonNull Context context) {
        super(context);
    }

    public static PersonDB getInstance(@NonNull Context context) {
        if (instance == null) {
            synchronized (PersonDB.class) {
                if (instance == null) {
                    instance = new PersonDB(context);
                }
            }
        }

        return instance;
    }

}
