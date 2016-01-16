package com.ToxicBakery.app.tabsontallahassee.models;

import com.ToxicBakery.app.tabsontallahassee.db.IdTagged;

public class Membership implements IdTagged {

    private String id;
    private Attributes attributes;
    private Relationships relationships;

    @Override
    public String getId() {
        return id;
    }

    public String getRole() {
        return attributes.getRole();
    }

    public String getOrganizationId() {
        return relationships.getOrganization()
                .getData()
                .getId();
    }

    static class Attributes {

        private String role;

        public String getRole() {
            return role;
        }
    }

    static class Relationships {

        private Organization organization;

        public Organization getOrganization() {
            return organization;
        }

    }

    static class Organization {

        private Data data;

        public Data getData() {
            return data;
        }

    }

    static class Data {

        private String id;

        public String getId() {
            return id;
        }

    }

}
