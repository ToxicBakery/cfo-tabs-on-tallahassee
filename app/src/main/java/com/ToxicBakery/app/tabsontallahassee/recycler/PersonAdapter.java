package com.ToxicBakery.app.tabsontallahassee.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ToxicBakery.app.tabsontallahassee.R;
import com.ToxicBakery.app.tabsontallahassee.db.PersonDB;
import com.ToxicBakery.app.tabsontallahassee.models.Person;

public class PersonAdapter extends AlphaInAdapter<PersonViewHolder> {

    private final PersonDB personDB;

    public PersonAdapter(PersonDB personDB) {
        this.personDB = personDB;
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_person, parent, false);

        return new PersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        Person person = personDB.getAt(position);
        if (person == null) {
            throw new IndexOutOfBoundsException("No person found for " + position);
        }
        holder.bind(person);

        super.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return personDB.size();
    }

}
