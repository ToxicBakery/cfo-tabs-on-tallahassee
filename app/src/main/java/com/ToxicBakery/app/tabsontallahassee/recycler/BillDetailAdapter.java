package com.ToxicBakery.app.tabsontallahassee.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ToxicBakery.app.tabsontallahassee.R;
import com.ToxicBakery.app.tabsontallahassee.models.Action;
import com.ToxicBakery.app.tabsontallahassee.models.Attributes;
import com.ToxicBakery.app.tabsontallahassee.models.Bill;
import com.ToxicBakery.app.tabsontallahassee.models.Document;
import com.ToxicBakery.app.tabsontallahassee.models.Sponsorship;
import com.ToxicBakery.app.tabsontallahassee.models.Version;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BillDetailAdapter extends AlphaInAdapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 1;
    private static final int TYPE_VERSIONS = 2;
    private static final int TYPE_SPONSORSHIPS = 3;
    private static final int TYPE_DOCUMENTS = 4;
    private static final int TYPE_ACTIONS = 5;

    private final List<Action> actions;
    private final List<Sponsorship> sponsorships;
    private final List<Document> documents;
    private final List<Version> versions;

    int size;
    int versionStart;
    int sponsorshipStart;
    int documentsStart;
    int actionsStart;

    boolean hasVersions;
    boolean hasSponsors;
    boolean hasDocuments;
    boolean hasActions;

    public BillDetailAdapter() {
        actions = new ArrayList<>();
        sponsorships = new ArrayList<>();
        documents = new ArrayList<>();
        versions = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case TYPE_HEADER:
                View header = layoutInflater.inflate(R.layout.item_header, parent, false);
                return new HeaderViewHolder(header);
            case TYPE_VERSIONS:
                View version = layoutInflater.inflate(R.layout.item_version, parent, false);
                return new VersionViewHolder(version);
            case TYPE_SPONSORSHIPS:
                View sponsorship = layoutInflater.inflate(R.layout.item_sponsorship, parent, false);
                return new SponsorshipViewHolder(sponsorship);
            case TYPE_DOCUMENTS:
                View document = layoutInflater.inflate(R.layout.item_document, parent, false);
                return new DocumentViewHolder(document);
            case TYPE_ACTIONS:
                View action = layoutInflater.inflate(R.layout.item_action, parent, false);
                return new ActionViewHolder(action);
        }

        throw new IllegalStateException("Unknown type " + viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (hasVersions && position == versionStart) {
            ((HeaderViewHolder) holder).bind(R.string.header_versions);
        } else if (hasSponsors && position == sponsorshipStart) {
            ((HeaderViewHolder) holder).bind(R.string.header_sponsorships);
        } else if (hasDocuments && position == documentsStart) {
            ((HeaderViewHolder) holder).bind(R.string.header_documents);
        } else if (hasActions && position == actionsStart) {
            ((HeaderViewHolder) holder).bind(R.string.header_actions);
        } else {
            // Not header
            if (hasVersions && position < sponsorshipStart) {
                Version version = versions.get(position - 1);
                ((VersionViewHolder) holder).bind(version);
            } else if (hasSponsors && position < documentsStart) {
                Sponsorship sponsorship = sponsorships.get(position - sponsorshipStart - 1);
                ((SponsorshipViewHolder) holder).bind(sponsorship);
            } else if (hasDocuments && position < actionsStart) {
                Document document = documents.get(position - documentsStart - 1);
                ((DocumentViewHolder) holder).bind(document);
            } else if (hasActions) {
                // Flip the display of actions to most recent is first
                int actionPosition = position - actionsStart - 1;
                // Subtract 2 to remove the count of the header and then off by 1 for the length -> zero base
                Action action = actions.get(getActionsCount() - 2 - actionPosition);
                ((ActionViewHolder) holder).bind(action);
            }
        }

        super.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemViewType(int position) {
        if (position < sponsorshipStart) {
            return position == versionStart ? TYPE_HEADER : TYPE_VERSIONS;
        } else if (position < documentsStart) {
            return position == sponsorshipStart ? TYPE_HEADER : TYPE_SPONSORSHIPS;
        } else if (position < actionsStart) {
            return position == documentsStart ? TYPE_HEADER : TYPE_DOCUMENTS;
        } else {
            return position == actionsStart ? TYPE_HEADER : TYPE_ACTIONS;
        }
    }

    @Override
    public int getItemCount() {
        return size;
    }

    public void set(Bill bill) {
        Attributes attributes = bill.getAttributes();
        Version[] versions = attributes.getVersions();
        Sponsorship[] sponsorships = attributes.getSponsorships();
        Document[] documents = attributes.getDocuments();
        Action[] actions = attributes.getActions();

        this.versions.clear();
        if (versions != null) {
            this.versions.addAll(Arrays.asList(versions));
        }

        this.sponsorships.clear();
        if (sponsorships != null) {
            this.sponsorships.addAll(Arrays.asList(sponsorships));
        }

        this.documents.clear();
        if (documents != null) {
            this.documents.addAll(Arrays.asList(documents));
        }

        this.actions.clear();
        if (actions != null) {
            this.actions.addAll(Arrays.asList(actions));
        }

        calculateSize();

        notifyDataSetChanged();
    }

    void calculateSize() {
        int versionsCount = getVersionsCount();
        int sponsorshipsCount = getSponsorshipsCount();
        int documentsCount = getDocumentsCount();
        int actionsCount = getActionsCount();

        size = versionsCount
                + sponsorshipsCount
                + documentsCount
                + actionsCount;

        versionStart = 0;
        sponsorshipStart = versionStart + versionsCount;
        documentsStart = sponsorshipStart + sponsorshipsCount;
        actionsStart = documentsStart + documentsCount;

        hasVersions = versionsCount > 0;
        hasSponsors = sponsorshipsCount > 0;
        hasDocuments = documentsCount > 0;
        hasActions = actionsCount > 0;
    }

    int getVersionsCount() {
        int count = versions.size();
        return count == 0 ? 0 : ++count;
    }

    int getSponsorshipsCount() {
        int count = sponsorships.size();
        return count == 0 ? 0 : ++count;
    }

    int getDocumentsCount() {
        int count = documents.size();
        return count == 0 ? 0 : ++count;
    }

    int getActionsCount() {
        int count = actions.size();
        return count == 0 ? 0 : ++count;
    }

}
