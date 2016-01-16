package com.ToxicBakery.app.tabsontallahassee.models;

import com.ToxicBakery.app.tabsontallahassee.db.IdTagged;

public class OrganizationDetail implements IdTagged {

    private String id;
    private OrganizationAttributes attributes;

    @Override
    public String getId() {
        return id;
    }

    public OrganizationAttributes getAttributes() {
        return attributes;
    }

}
