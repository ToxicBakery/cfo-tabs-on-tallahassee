package com.ToxicBakery.app.tabsontallahassee.models;

import com.ToxicBakery.app.tabsontallahassee.db.IdTagged;
import com.google.gson.annotations.SerializedName;

public class Person implements IdTagged {

    private String id;
    private Relationships relationships;

    @SerializedName("attributes")
    private PersonAttributes personAttributes;

    @Override
    public String getId() {
        return id;
    }

    public PersonAttributes getPersonAttributes() {
        return personAttributes;
    }

    public String[] getMemberships() {
        if (relationships == null) {
            return new String[0];
        }

        Memberships memberships = relationships.getMemberships();
        Membership[] data = memberships.getData();

        String[] out = new String[data.length];
        for (int i = 0; i < data.length; i++) {
            Membership membership = data[i];
            out[i] = membership.getId();
        }

        return out;
    }

    static class Relationships {

        private Memberships memberships;

        public Memberships getMemberships() {
            return memberships;
        }

    }

    static class Memberships {

        private Membership[] data;

        public Membership[] getData() {
            return data;
        }

    }

    static class Membership {

        private String id;

        public String getId() {
            return id;
        }

    }

}
