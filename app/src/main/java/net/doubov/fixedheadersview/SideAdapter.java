package net.doubov.fixedheadersview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SideAdapter extends FixedHeadersAdapter<String, SideAdapter.Holder> {

    @Override
    public SideAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(SideAdapter.Holder holder, int position) {
        holder.text1.setText(data.get(position));
        holder.text2.setText(String.format("P: %d", position));
    }

    public static class Holder extends RecyclerView.ViewHolder {

        private TextView text1;
        private TextView text2;

        public Holder(View itemView) {
            super(itemView);

            text1 = (TextView) itemView.findViewById(R.id.text1);
            text2 = (TextView) itemView.findViewById(R.id.text2);
        }
    }

}
