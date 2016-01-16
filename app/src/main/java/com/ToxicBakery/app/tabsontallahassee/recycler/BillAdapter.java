package com.ToxicBakery.app.tabsontallahassee.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ToxicBakery.app.tabsontallahassee.R;
import com.ToxicBakery.app.tabsontallahassee.db.BillDB;
import com.ToxicBakery.app.tabsontallahassee.models.Bill;

public class BillAdapter extends AlphaInAdapter<BillViewHolder> {

    private final BillDB billDB;

    public BillAdapter(BillDB billDB) {
        this.billDB = billDB;
    }

    @Override
    public BillViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bill, parent, false);

        return new BillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BillViewHolder holder, int position) {
        Bill bill = billDB.getAt(position);
        if (bill == null) {
            throw new IndexOutOfBoundsException("No bill found for " + position);
        }
        holder.bind(bill);

        super.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return billDB.size();
    }

}
