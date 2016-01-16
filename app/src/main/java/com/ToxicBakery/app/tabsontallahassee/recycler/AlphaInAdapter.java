package com.ToxicBakery.app.tabsontallahassee.recycler;

import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

/**
 * Based on recyclerview-animators
 *
 * @param <T>
 * @see <a href="https://github.com/wasabeef/recyclerview-animators">https://github.com/wasabeef/recyclerview-animators</a>
 */
abstract class AlphaInAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {

    private int lastPosition = -1;

    @Override
    public void onBindViewHolder(T holder, int position) {

        if (position > lastPosition) {
            View view = holder.itemView;

            ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 0.5f, 1f);
            alpha.setDuration(getDuration()).start();
            alpha.setInterpolator(getInterpolator());

            lastPosition = position;
        }
    }

    protected Interpolator getInterpolator() {
        return new LinearInterpolator();
    }

    protected int getDuration() {
        return 300;
    }

}
