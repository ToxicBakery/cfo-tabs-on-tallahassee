package com.ToxicBakery.app.tabsontallahassee.recycler;

import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ToxicBakery.app.tabsontallahassee.R;
import com.ToxicBakery.app.tabsontallahassee.dialog.DialogDownloadFragment;
import com.ToxicBakery.app.tabsontallahassee.models.Document;
import com.ToxicBakery.app.tabsontallahassee.models.Link;

public class DocumentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    final TextView title;
    final TextView url;
    final Context context;

    Document document;

    public DocumentViewHolder(View itemView) {
        super(itemView);

        title = (TextView) itemView.findViewById(R.id.document_title);
        url = (TextView) itemView.findViewById(R.id.document_url);

        context = itemView.getContext();

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Link[] links = document.getLinks();
        if (links != null && links.length > 0) {
            Link link = links[0];

            DialogFragment dialogFragment = DialogDownloadFragment.createInstance(
                    context.getString(R.string.header_documents),
                    document.getNote(),
                    link.getUrl()
            );

            // FIXME Really shouldn't assume the context is an activity but YOLO
            dialogFragment.show(
                    ((AppCompatActivity) context).getSupportFragmentManager(),
                    DialogDownloadFragment.TAG
            );
        }
    }

    public void bind(Document document) {
        this.document = document;

        title.setText(document.getNote());

        Link[] links = document.getLinks();
        if (links != null && links.length > 0) {
            Link link = links[0];
            url.setText(link.getUrl());
        }
    }
}
