package com.ToxicBakery.app.tabsontallahassee.models;

@SuppressWarnings("unused")
public class Attributes {

    private LegislativeSession legislativeSession;
    private String created_at;
    private String updated_at;
    private String identifier;
    private String[] subject;
    private String[] classification;
    private String title;
    private Sponsorship[] sponsorships;
    private Document[] documents;
    private Version[] versions;
    private Source[] sources;
    private Action[] actions;

    public String[] getClassification() {
        return classification;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public Document[] getDocuments() {
        return documents;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Source[] getSources() {
        return sources;
    }

    public Sponsorship[] getSponsorships() {
        return sponsorships;
    }

    public String[] getSubject() {
        return subject;
    }

    public String getTitle() {
        return title;
    }

    public String getUpdatedAt() {
        return updated_at;
    }

    public Version[] getVersions() {
        return versions;
    }

    public Action[] getActions() {
        return actions;
    }

}
