package com.ToxicBakery.app.tabsontallahassee.recycler;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ToxicBakery.app.tabsontallahassee.ActivityBill;
import com.ToxicBakery.app.tabsontallahassee.R;
import com.ToxicBakery.app.tabsontallahassee.models.Attributes;
import com.ToxicBakery.app.tabsontallahassee.models.Bill;

public class BillViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    final TextView title;
    final TextView identifier;
    final Context context;

    Bill bill;

    public BillViewHolder(View itemView) {
        super(itemView);

        title = (TextView) itemView.findViewById(R.id.bill_title);
        identifier = (TextView) itemView.findViewById(R.id.bill_identifier);

        context = itemView.getContext();

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, ActivityBill.class);
        intent.putExtra(ActivityBill.EXTRA_UUID, bill.getId());
        context.startActivity(intent);
    }

    public void bind(@NonNull Bill bill) {
        this.bill = bill;

        Attributes attributes = bill.getAttributes();
        title.setText(attributes.getTitle());
        identifier.setText(attributes.getIdentifier());
    }

}
