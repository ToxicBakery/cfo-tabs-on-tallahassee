package com.ToxicBakery.app.tabsontallahassee.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ToxicBakery.app.tabsontallahassee.R;
import com.ToxicBakery.app.tabsontallahassee.db.OrganizationDetailDB;
import com.ToxicBakery.app.tabsontallahassee.models.Contact;
import com.ToxicBakery.app.tabsontallahassee.models.Membership;
import com.ToxicBakery.app.tabsontallahassee.models.OrganizationDetail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PersonDetailAdapter extends AlphaInAdapter<RecyclerView.ViewHolder> {

    static final int TYPE_CONTACT_HEADER = 1;
    static final int TYPE_CONTACT = 2;
    static final int TYPE_MEMBERSHIP_HEADER = 3;
    static final int TYPE_MEMBERSHIP = 4;

    private final List<Contact> contactList;
    private final List<Membership> membershipList;
    private final OrganizationDetailDB organizationDetailDB;

    public PersonDetailAdapter(OrganizationDetailDB organizationDetailDB) {

        this.organizationDetailDB = organizationDetailDB;

        contactList = new ArrayList<>();
        membershipList = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case TYPE_CONTACT_HEADER:
            case TYPE_MEMBERSHIP_HEADER:
                View headerView = layoutInflater.inflate(R.layout.item_header, parent, false);
                return new HeaderViewHolder(headerView);
            case TYPE_CONTACT:
                View contactView = layoutInflater.inflate(R.layout.item_contact, parent, false);
                return new ContactViewHolder(contactView);
            case TYPE_MEMBERSHIP:
                View membershipView = layoutInflater.inflate(R.layout.item_membership, parent, false);
                return new MembershipViewHolder(membershipView);
        }

        throw new IllegalStateException("Unknown type " + viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)) {
            case TYPE_CONTACT_HEADER:
                ((HeaderViewHolder) holder).bind(R.string.header_contact);
                break;
            case TYPE_CONTACT:
                int realContactPosition = getContactPosition(position);
                Contact contact = contactList.get(realContactPosition);
                ((ContactViewHolder) holder).bind(contact);
                break;
            case TYPE_MEMBERSHIP_HEADER:
                ((HeaderViewHolder) holder).bind(R.string.header_membership);
                break;
            case TYPE_MEMBERSHIP:
                Membership membership = membershipList.get(position - 1);
                OrganizationDetail organizationDetail = organizationDetailDB.getById(membership.getOrganizationId());
                ((MembershipViewHolder) holder).bind(membership, organizationDetail);
                break;
        }

        super.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return membershipList.size() +
                contactList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_MEMBERSHIP_HEADER;
        }

        final int membershipSize = membershipList.size();
        if (position == membershipSize) {
            return TYPE_CONTACT_HEADER;
        } else if (position < membershipSize) {
            return TYPE_MEMBERSHIP;
        } else {
            return TYPE_CONTACT;
        }
    }

    int getContactPosition(int position) {
        // Determine the real data position subtracting headers and membership count
        int membershipCount = membershipList.size();
        if (membershipCount > 0) {
            ++membershipCount;
        }

        return position - membershipCount;
    }

    public void set(Contact[] contacts, Membership[] memberships) {
        contactList.clear();
        membershipList.clear();

        if (contacts != null) {
            contactList.addAll(Arrays.asList(contacts));
            membershipList.addAll(Arrays.asList(memberships));
        }

        notifyDataSetChanged();
    }

}
