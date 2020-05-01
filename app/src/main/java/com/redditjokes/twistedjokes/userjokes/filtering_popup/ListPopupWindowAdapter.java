package com.redditjokes.twistedjokes.userjokes.filtering_popup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.redditjokes.twistedjokes.R;

import java.util.List;

public class ListPopupWindowAdapter extends BaseAdapter {
    private List<ListPopupItem> items;

    public ListPopupWindowAdapter(List<ListPopupItem> items) {
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public ListPopupItem getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.popup_filtering_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvTitle.setText(getItem(position).getTitle());
        holder.ivImage.setImageResource(getItem(position).getImageRes());
        return convertView;
    }

    static class ViewHolder {
        TextView tvTitle;
        ImageView ivImage;

        ViewHolder(View view) {
            tvTitle = view.findViewById(R.id.text);
            ivImage = view.findViewById(R.id.image);
        }
    }
}