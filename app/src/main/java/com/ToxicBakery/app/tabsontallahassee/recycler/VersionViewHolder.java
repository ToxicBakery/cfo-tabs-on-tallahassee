package com.ToxicBakery.app.tabsontallahassee.recycler;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ToxicBakery.app.tabsontallahassee.R;
import com.ToxicBakery.app.tabsontallahassee.dialog.DialogDownloadFragment;
import com.ToxicBakery.app.tabsontallahassee.models.Link;
import com.ToxicBakery.app.tabsontallahassee.models.Version;

public class VersionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    final TextView title;
    final TextView comment;
    final Context context;

    Version version;

    public VersionViewHolder(View itemView) {
        super(itemView);

        title = (TextView) itemView.findViewById(R.id.version_title);
        comment = (TextView) itemView.findViewById(R.id.version_comment);

        context = itemView.getContext();

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Link[] links = version.getLinks();
        if (links != null && links.length > 0) {
            Link link = links[0];

            DialogFragment dialogFragment = DialogDownloadFragment.createInstance(
                    version.getNote(),
                    link.getText(),
                    link.getUrl()
            );

            // FIXME Really shouldn't assume the context is an activity but YOLO
            dialogFragment.show(
                    ((AppCompatActivity) context).getSupportFragmentManager(),
                    DialogDownloadFragment.TAG
            );
        }
    }

    public void bind(Version version) {
        this.version = version;

        title.setText(version.getNote());

        // TODO Find version with multiple links then determine how best to handle visually
        Link[] links = version.getLinks();
        if (links != null && links.length > 0) {
            Link link = links[0];
            comment.setText(link.getText());
        } else {
            comment.setText(R.string.na);
        }
    }
}
