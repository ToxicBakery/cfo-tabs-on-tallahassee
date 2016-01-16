package com.ToxicBakery.app.tabsontallahassee.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ToxicBakery.app.tabsontallahassee.R;
import com.ToxicBakery.app.tabsontallahassee.models.Action;

public class ActionViewHolder extends RecyclerView.ViewHolder {

    final TextView date;
    final TextView description;

    public ActionViewHolder(View itemView) {
        super(itemView);

        date = (TextView) itemView.findViewById(R.id.action_date);
        description = (TextView) itemView.findViewById(R.id.action_description);
    }

    public void bind(Action action) {
        date.setText(action.getDate());
        description.setText(action.getDescription());
    }
}
