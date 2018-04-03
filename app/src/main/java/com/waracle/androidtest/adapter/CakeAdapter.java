package com.waracle.androidtest.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.waracle.androidtest.ImageLoader;
import com.waracle.androidtest.R;
import com.waracle.androidtest.model.Cake;

import java.util.ArrayList;
import java.util.List;

public class CakeAdapter extends BaseAdapter {

    private final List<Cake> cakeList;
    private final ImageLoader imageLoader;

    public CakeAdapter() {
        cakeList = new ArrayList<>();
        imageLoader = new ImageLoader();
    }

    @Override
    public int getCount() {
        return cakeList.size();
    }

    @Override
    public Object getItem(int position) {
        return cakeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_cake,
                    parent, false);

            viewHolder = new ViewHolder();
            viewHolder.title = convertView.findViewById(R.id.title);
            viewHolder.description = convertView.findViewById(R.id.desc);
            viewHolder.image = convertView.findViewById(R.id.image);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Cake actualCake = (Cake) getItem(position);

        viewHolder.title.setText(actualCake.getTitle());
        viewHolder.description.setText(actualCake.getDescription());

        viewHolder.image.setImageBitmap(null);
        imageLoader.load(actualCake.getImageUrl(), viewHolder.image);

        return convertView;
    }

    public void setItems(List<Cake> cakesToAdd) {
        cakeList.clear();
        cakeList.addAll(cakesToAdd);

        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView title;
        TextView description;
        ImageView image;
    }
}