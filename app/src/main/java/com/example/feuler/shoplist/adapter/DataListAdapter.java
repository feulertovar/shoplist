package com.example.feuler.shoplist.adapter;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.feuler.shoplist.R;
import com.example.feuler.shoplist.model.Items;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * RecyclerView adapter for a list of Items.
 */
public class DataListAdapter extends FirestoreAdapter<DataListAdapter.ViewHolder> {

    public interface OnItemSelectedListener {

        void onItemSelected(DocumentSnapshot Item);

    }

    private OnItemSelectedListener mListener;

    public DataListAdapter(Query query, OnItemSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_databaselist, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.list_item_name)
        TextView nameView;

        @BindView(R.id.list_item_quan)
        TextView quanView;

        @BindView(R.id.list_item_priority)
        TextView priorityView;

        @BindView(R.id.list_item_price)
        TextView priceView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final DocumentSnapshot snapshot,
                         final OnItemSelectedListener listener) {

            Items item = snapshot.toObject(Items.class);
            Resources resources = itemView.getResources();

            nameView.setText(item.getName());

            quanView.setText(Integer.toString(item.getQuantity()));

            priorityView.setText(Integer.toString(item.getPriority()));

            //setting double
            priceView.setText(Double.toString(item.getPrice()));

            // Click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onItemSelected(snapshot);
                    }
                }
            });
        }

    }
}
