package com.ToxicBakery.app.tabsontallahassee.models;

import com.ToxicBakery.app.tabsontallahassee.db.IdTagged;

@SuppressWarnings("unused")
public class PersonAttributes implements IdTagged {

    private String family_name;
    private String birth_date;
    private String updated_at;
    private String created_at;
    private String sort_name;
    private String biography;
    private String summary;
    private String id;
    private String gender;
    private String name;
    private String death_date;
    private String national_identity;
    private String image;
    private String give_name;
    private String[] locked_fields;
    private PersonExtras extras;
    private Contact[] contact_details;

    public String getBiography() {
        return biography;
    }

    public String getBirthDate() {
        return birth_date;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public String getDeathDate() {
        return death_date;
    }

    public PersonExtras getExtras() {
        return extras;
    }

    public String getFamilyName() {
        return family_name;
    }

    public String getGender() {
        return gender;
    }

    public String getGiveName() {
        return give_name;
    }

    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String[] getLockedFields() {
        return locked_fields;
    }

    public String getName() {
        return name;
    }

    public String getNationalIdentity() {
        return national_identity;
    }

    public String getSortName() {
        return sort_name;
    }

    public String getSummary() {
        return summary;
    }

    public String getUpdatedAt() {
        return updated_at;
    }

    public Contact[] getContacts() {
        return contact_details;
    }

    public static class PersonExtras {

        private String residence;
        private String member_since;

        public String getMemberSince() {
            return member_since;
        }

        public String getResidence() {
            return residence;
        }
    }

}
