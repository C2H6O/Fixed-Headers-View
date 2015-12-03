package net.doubov.fixedheadersview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TopAdapter extends FixedHeadersAdapter<String, TopAdapter.Holder> {

    @Override
    public TopAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(TopAdapter.Holder holder, int position) {
        holder.text1.setText(data.get(position));
        holder.text2.setText(String.format("P: %d", position));
    }

    public static class Holder extends RecyclerView.ViewHolder {

        public TextView text1;
        public TextView text2;

        public Holder(View itemView) {
            super(itemView);

            text1 = (TextView) itemView.findViewById(android.R.id.text1);
            text2 = (TextView) itemView.findViewById(android.R.id.text2);
        }
    }

}
