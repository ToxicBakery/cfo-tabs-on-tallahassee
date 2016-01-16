package com.ToxicBakery.app.tabsontallahassee.models;

@SuppressWarnings("unused")
public class Sponsorship {

    private String classification;
    private String entity_type;
    private String name;
    private boolean primary;
    private PersonAttributes person;

    public String getClassification() {
        return classification;
    }

    public String getEntityType() {
        return entity_type;
    }

    public String getName() {
        return name;
    }

    public PersonAttributes getPerson() {
        return person;
    }

    public boolean isPrimary() {
        return primary;
    }

}
