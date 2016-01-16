package com.ToxicBakery.app.tabsontallahassee.recycler;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ToxicBakery.app.tabsontallahassee.ActivityPerson;
import com.ToxicBakery.app.tabsontallahassee.R;
import com.ToxicBakery.app.tabsontallahassee.models.PersonAttributes;
import com.ToxicBakery.app.tabsontallahassee.models.Sponsorship;
import com.squareup.picasso.Picasso;

public class SponsorshipViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private static final String ENTITY_PERSON = "person";

    final ImageView person;
    final TextView name;
    final TextView comment;

    final Context context;

    Sponsorship sponsorship;

    public SponsorshipViewHolder(View itemView) {
        super(itemView);

        person = (ImageView) itemView.findViewById(R.id.sponsorship_person);
        name = (TextView) itemView.findViewById(R.id.sponsorship_name);
        comment = (TextView) itemView.findViewById(R.id.sponsorship_comment);

        context = itemView.getContext();

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (ENTITY_PERSON.equals(sponsorship.getEntityType())) {
            Intent intent = new Intent(context, ActivityPerson.class);
            intent.putExtra(ActivityPerson.EXTRA_UUID, sponsorship.getPerson().getId());
            context.startActivity(intent);
        }
    }

    public void bind(Sponsorship sponsorship) {
        this.sponsorship = sponsorship;

        if (ENTITY_PERSON.equals(sponsorship.getEntityType())) {
            person.setVisibility(View.VISIBLE);

            PersonAttributes personAttributes = sponsorship.getPerson();

            name.setText(personAttributes.getName());

            Picasso.with(context)
                    .load(personAttributes.getImage())
                    .into(person);

            PersonAttributes.PersonExtras personExtras = sponsorship.getPerson()
                    .getExtras();

            String residence = context.getString(
                    R.string.person_residence_since,
                    personExtras.getResidence(),
                    personExtras.getMemberSince()
            );
            comment.setText(residence);
        } else {
            person.setVisibility(View.GONE);
        }
    }

}
