package net.doubov.fixedheadersview.adapters;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class FixedHeadersAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected List<T> data = new ArrayList<>();

    public void setData(List<T> data, boolean notifyDataSetChanged) {
        this.data.clear();
        this.data.addAll(data);
        if (notifyDataSetChanged) {
            notifyDataSetChanged();
        }
    }

    public void setData(T[] data, boolean notifyDataSetChanged) {
        this.data.clear();
        this.data.addAll(Arrays.asList(data));
        if (notifyDataSetChanged) {
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}
