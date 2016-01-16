package com.ToxicBakery.app.tabsontallahassee.models;

@SuppressWarnings("unused")
public class OrganizationAttributes {

    private String created_at;
    private String updated_at;
    private String name;
    private String image;
    private String classification;
    private String founding_date;
    private String dissolution_date;

    public String getClassification() {
        return classification;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public String getDissolutionDate() {
        return dissolution_date;
    }

    public String getFoundingDate() {
        return founding_date;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getUpdatedAt() {
        return updated_at;
    }

}
