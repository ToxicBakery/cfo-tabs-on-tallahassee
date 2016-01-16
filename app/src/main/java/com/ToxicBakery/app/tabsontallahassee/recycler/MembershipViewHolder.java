package com.ToxicBakery.app.tabsontallahassee.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ToxicBakery.app.tabsontallahassee.R;
import com.ToxicBakery.app.tabsontallahassee.models.Membership;
import com.ToxicBakery.app.tabsontallahassee.models.OrganizationAttributes;
import com.ToxicBakery.app.tabsontallahassee.models.OrganizationDetail;

public class MembershipViewHolder extends RecyclerView.ViewHolder {

    final TextView organizationName;
    final TextView organizationRole;

    public MembershipViewHolder(View itemView) {
        super(itemView);

        organizationName = (TextView) itemView.findViewById(R.id.membership_organization);
        organizationRole = (TextView) itemView.findViewById(R.id.membership_role);
    }

    public void bind(Membership membership,
                     OrganizationDetail organization) {

        OrganizationAttributes attributes = organization.getAttributes();

        organizationRole.setText(membership.getRole());
        organizationName.setText(attributes.getName());
    }

}
