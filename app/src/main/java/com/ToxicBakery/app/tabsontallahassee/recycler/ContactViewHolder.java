package com.ToxicBakery.app.tabsontallahassee.recycler;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ToxicBakery.app.tabsontallahassee.R;
import com.ToxicBakery.app.tabsontallahassee.models.Contact;

public class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private static final String TAG = "ContactViewHolder";
    private static final String TYPE_ADDRESS = "address";
    private static final String TYPE_VOICE = "voice";

    final TextView note;
    final TextView value;
    final Context context;

    Contact contact;

    public ContactViewHolder(View itemView) {
        super(itemView);

        this.context = itemView.getContext();
        note = (TextView) itemView.findViewById(R.id.contact_note);
        value = (TextView) itemView.findViewById(R.id.contact_value);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (contact.getType()) {
            case TYPE_ADDRESS:
                openMap();
                break;
            case TYPE_VOICE:
                openTelephone();
                break;
        }
    }

    public void bind(Contact contact) {
        this.contact = contact;
        note.setText(contact.getNote());
        value.setText(contact.getValue());
    }

    void openMap() {
        try {
            Uri uri = Uri.parse("geo:0,0?q=" + contact.getValue());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage("com.google.android.apps.maps");
            context.startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "Maps not available.", e);
        }
    }

    void openTelephone() {
        try {
            Uri uri = Uri.parse("tel:" + contact.getValue());
            Intent intent = new Intent(Intent.ACTION_DIAL, uri);
            context.startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "Failed to dial number, possibly not a phone.", e);
        }
    }

}
