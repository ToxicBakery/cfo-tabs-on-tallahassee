package com.ToxicBakery.app.tabsontallahassee.recycler;

import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ToxicBakery.app.tabsontallahassee.R;

public class HeaderViewHolder extends RecyclerView.ViewHolder {

    final TextView headerTitle;

    public HeaderViewHolder(View itemView) {
        super(itemView);

        headerTitle = (TextView) itemView.findViewById(R.id.header_text);
    }

    public void bind(@StringRes int titleId) {
        headerTitle.setText(titleId);
    }

}
