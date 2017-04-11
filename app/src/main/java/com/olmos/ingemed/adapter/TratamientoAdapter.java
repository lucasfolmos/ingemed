package com.olmos.ingemed.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.olmos.ingemed.R;
import com.olmos.ingemed.interfaces.ITratamientos;
import com.olmos.ingemed.model.TratamientoObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class TratamientoAdapter extends RealmRecyclerViewAdapter<TratamientoObject, TratamientoAdapter.TratamientoViewHolder> {

    Context context;
    int selected_position = 0;
    ITratamientos iTratamientos;

    public TratamientoAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<TratamientoObject> data, ITratamientos iTratamientos, boolean autoUpdate) {
        super(context, data, autoUpdate);
        this.context = context;
        this.iTratamientos = iTratamientos;
    }


    @Override
    public TratamientoAdapter.TratamientoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.recycler_tratamientos, parent, false);
        return new TratamientoAdapter.TratamientoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TratamientoAdapter.TratamientoViewHolder holder, final int position) {
        final TratamientoObject tratamientoObject = getData().get(position);

        holder.text.setText(tratamientoObject.getName());

        if(selected_position == position){
            // Here I am just highlighting the background
            holder.itemView.setBackgroundResource(R.drawable.inner_shadow_bkg_green);
            holder.text.setTextColor(Color.BLACK);
            holder.dash.setBackgroundColor(Color.BLACK);
            iTratamientos.setTratamientoActual(tratamientoObject);
        }else{
            holder.itemView.setBackgroundResource(R.drawable.inner_shadow_bkg);
            holder.text.setTextColor(Color.WHITE);
            holder.dash.setBackgroundColor(Color.parseColor("#8ef1a0"));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Updating old as well as new positions
                notifyItemChanged(selected_position);
                selected_position = position;
                notifyItemChanged(selected_position);

                // Do your another stuff for your onClick

            }
        });
    }

    class TratamientoViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.recycler_tratamiento_dash)
        public View dash;

        @BindView(R.id.recycler_tratamiento_text)
        public TextView text;

        public View card;

        public TratamientoViewHolder(View itemView) {
            super(itemView);
            card = itemView;

            ButterKnife.bind(this, itemView);
        }
    }
}

