package com.example.android.newsapp;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Kundan on 13-12-2017.
 * Recycle view adpter for news Items
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private List<News> mList;
    private Context mContext;

    public NewsAdapter(Context context, List<News> news) {
        mContext = context;
        mList = news;
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView mHeading, mSubHeading;
        ImageView mImage;
        CardView mCardParent;
        RelativeLayout mRelativeParent;

        ViewHolder(View itemView) {
            super(itemView);
            mHeading = (TextView) itemView.findViewById(R.id.row_item_heading);
            mSubHeading = (TextView) itemView.findViewById(R.id.row_item_subHeading);
            mImage = (ImageView) itemView.findViewById(R.id.row_item_image);
            mCardParent = (CardView) itemView.findViewById(R.id.row_item_card_view_parent);
            mRelativeParent = (RelativeLayout) itemView.findViewById(R.id.row_item_relative_layout_parent);
        }
    }

    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        //Card View layout
        View newsView = layoutInflater.inflate(R.layout.recycler_view_row_item_design_2, parent, false);
        //Relative layout layout
        //View newsView = layoutInflater.inflate(R.layout.recycler_view_row_item,parent,false);

        return new ViewHolder(newsView);
    }

    @Override
    public void onBindViewHolder(NewsAdapter.ViewHolder holder, int position) {
        final News item = mList.get(position);

        holder.mHeading.setText(item.getHeading());
        holder.mSubHeading.setText(item.getSubHeading());
        holder.mCardParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    builder.setToolbarColor(mContext.getColor(R.color.colorLight));
                }
                // set toolbar color and/or setting custom actions before invoking build()
                // Once ready, call CustomTabsIntent.Builder.build() to create a CustomTabsIntent
                CustomTabsIntent customTabsIntent = builder.build();
                // and launch the desired Url with CustomTabsIntent.launchUrl()
                customTabsIntent.launchUrl(mContext, Uri.parse(item.getUrlSite()));
            }
        });
        Picasso.with(mContext).load(item.getUrlImage()).into(holder.mImage);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


}
