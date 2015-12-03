package net.doubov.fixedheadersview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ContentAdapter extends BaseContentAdapter<String, ContentAdapter.ContentHolder> {

    @Override
    public void onBindViewHolder(ContentHolder holder, int position) {
        String item = getItem(position);
        holder.text1.setText(item);
        holder.text2.setText(String.format("[%d][%d]", getRow(position), getCol(position)));
    }

    @Override
    public ContentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        return new ContentHolder(v);
    }

    public static class ContentHolder extends RecyclerView.ViewHolder {

        private TextView text1;
        private TextView text2;

        public ContentHolder(View itemView) {
            super(itemView);

            text1 = (TextView) itemView.findViewById(android.R.id.text1);
            text2 = (TextView) itemView.findViewById(android.R.id.text2);
        }
    }

}
