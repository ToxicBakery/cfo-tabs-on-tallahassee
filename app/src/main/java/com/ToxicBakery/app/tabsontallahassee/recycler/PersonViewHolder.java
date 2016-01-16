package com.ToxicBakery.app.tabsontallahassee.recycler;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ToxicBakery.app.tabsontallahassee.ActivityPerson;
import com.ToxicBakery.app.tabsontallahassee.R;
import com.ToxicBakery.app.tabsontallahassee.models.Person;
import com.ToxicBakery.app.tabsontallahassee.models.PersonAttributes;
import com.github.florent37.picassopalette.BitmapPalette;
import com.github.florent37.picassopalette.PicassoPalette;
import com.squareup.picasso.Picasso;

public class PersonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    final TextView name;
    final View nameBg;
    final ImageView image;
    final Context context;
    final int defaultBgColor;

    String uuid;

    @SuppressWarnings("deprecation")
    public PersonViewHolder(View itemView) {
        super(itemView);

        name = (TextView) itemView.findViewById(R.id.person_name);
        nameBg = itemView.findViewById(R.id.person_name_bg);
        image = (ImageView) itemView.findViewById(R.id.person_image);

        context = itemView.getContext();

        defaultBgColor = context.getResources()
                .getColor(R.color.colorPrimaryDark);

        itemView.findViewById(R.id.person_overlay)
                .setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, ActivityPerson.class);
        intent.putExtra(ActivityPerson.EXTRA_UUID, uuid);
        context.startActivity(intent);
    }

    public void bind(@NonNull Person person) {
        uuid = person.getId();

        PersonAttributes personAttributes = person.getPersonAttributes();
        name.setText(formatName(personAttributes.getName()));

        final String personImage = personAttributes.getImage();
        if (personImage != null
                && !personImage.isEmpty()) {

            Picasso.with(context)
                    .load(personImage)
                    .placeholder(R.drawable.blank_profile_image)
                    .into(image,
                            PicassoPalette.with(personImage, image)
                                    .intoCallBack(new BitmapPalette.CallBack() {

                                        @Override
                                        public void onPaletteLoaded(Palette palette) {
                                            int bgColor = palette.getDarkMutedColor(defaultBgColor);
                                            nameBg.setBackgroundColor(bgColor);
                                        }
                                    })
                    );
        }
    }

    String formatName(String name) {
        if (name == null) {
            return context.getString(R.string.na);
        } else if (name.contains(",")) {
            return name.substring(0, name.indexOf(","));
        }

        return name;
    }

}
