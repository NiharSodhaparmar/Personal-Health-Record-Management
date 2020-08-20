package com.example.phrm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomGridAdapter extends BaseAdapter {

    Context context;
    private final String[] values;
    private final int[] images;
    View view;
    LayoutInflater layoutInflater;

    public CustomGridAdapter(Context context, String[] values, int[] images) {
        this.context = context;
        this.values = values;
        this.images = images;
    }

    @Override
    public int getCount() {
        return values.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            view = new View(context);
            view = layoutInflater.inflate(R.layout.grid_item, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.gridImage);
            TextView textView = (TextView) view.findViewById(R.id.gridText);
            imageView.setImageResource(images[i]);
            textView.setText(values[i]);
        }
        return view;
    }
}
