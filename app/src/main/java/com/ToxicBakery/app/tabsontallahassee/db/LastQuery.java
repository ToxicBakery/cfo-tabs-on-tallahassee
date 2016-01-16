package com.ToxicBakery.app.tabsontallahassee.db;

import android.support.annotation.NonNull;

public class LastQuery implements IdTagged {

    String query;
    int currentPage;
    int nextPage;

    public LastQuery(@NonNull String query,
                     int currentPage,
                     int nextPage) {

        this.query = query;
        this.currentPage = currentPage;
        this.nextPage = nextPage;
    }

    @Override
    public String getId() {
        return query;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getNextPage() {
        return nextPage;
    }

}
