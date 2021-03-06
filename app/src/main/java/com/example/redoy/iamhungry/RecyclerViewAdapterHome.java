package com.example.redoy.iamhungry;

import android.content.Context;
import android.graphics.Color;
import android.widget.Filter;
import android.widget.Filterable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by redoy.ahmed on 11-Feb-2018.
 */

public class RecyclerViewAdapterHome extends RecyclerView.Adapter<RecyclerViewAdapterHome.RecyclerViewHolderHome> implements Filterable {

    private ArrayList<HomeItem> itemList;
    private ArrayList<HomeItem> itemListFiltered;
    private Context context;
    private static FragmentManager fragmentManager;

    public RecyclerViewAdapterHome(Context context, ArrayList<HomeItem> itemList, FragmentManager fragmentManager) {
        this.itemList = itemList;
        this.itemListFiltered = itemList;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public RecyclerViewHolderHome onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_list_home, parent, false);
        RecyclerViewHolderHome rcv = new RecyclerViewHolderHome(layoutView, fragmentManager);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolderHome holder, int position) {
        holder.itemName.setText(itemListFiltered.get(position).getName());
        holder.itemPhoto.setImageResource(itemListFiltered.get(position).getPhoto());
        holder.relativeLayout.setBackgroundColor(Color.parseColor(itemListFiltered.get(position).getColor()));
    }

    @Override
    public int getItemCount() {
        return this.itemListFiltered.size();
    }

    public static class RecyclerViewHolderHome extends RecyclerView.ViewHolder {

        @BindView(R.id.list_item_textView)
        public TextView itemName;

        @BindView(R.id.list_item_imageView)
        public ImageView itemPhoto;
        @BindView(R.id.relativeLayout)
        public RelativeLayout relativeLayout;

        public RecyclerViewHolderHome(final View itemView, final FragmentManager fragmentManager) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*Fragment mFragment;
                    if (getAdapterPosition() == 0) {
                        mFragment = new FragmentTutorial();
                    } else if (getAdapterPosition() == 1) {
                        mFragment = new FragmentPractise();
                    } else if (getAdapterPosition() == 2) {
                        mFragment = new FragmentCodeExamples();
                    } else if (getAdapterPosition() == 3) {
                        mFragment = new FragmentQuiz();
                    } else {
                        mFragment = new FragmentLinks();
                    }

                    if (mFragment != null) {
                        fragmentManager.beginTransaction().replace(R.id.container, mFragment).commit();
                    }*/
                }
            });
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    itemListFiltered = itemList;
                } else {
                    ArrayList<HomeItem> filteredList = new ArrayList<>();
                    for (HomeItem row : itemList) {
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    itemListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = itemListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                itemListFiltered = (ArrayList<HomeItem>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}

