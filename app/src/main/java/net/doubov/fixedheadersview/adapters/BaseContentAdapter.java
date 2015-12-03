package net.doubov.fixedheadersview.adapters;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class BaseContentAdapter<Content, ContentHolder extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<ContentHolder> {

    public static final String TAG = BaseContentAdapter.class.getSimpleName();

    protected List<List<Content>> contentData = new ArrayList<>();

    public void setData(Content[][] content, boolean notifyDataSetChanged) {
        contentData.clear();
        for (Content[] row : content) {
            contentData.add(Arrays.asList(row));
        }

        if (notifyDataSetChanged) {
            notifyDataSetChanged();
        }
    }

    public void setData(List<List<Content>> content) {
        contentData.clear();
        contentData.addAll(content);
        notifyDataSetChanged();
    }

    protected boolean hasData() {
        return !contentData.isEmpty();
    }

    protected int getRowCount() {
        return contentData.size();
    }

    protected int getColCount() {
        return hasData() ? contentData.get(0).size() : 0;
    }

    protected int getRow(int position) {
        return position / getColCount();
    }

    protected int getCol(int position) {
        return position % getColCount();
    }

    protected Content getItem(int position) {
        int row = getRow(position);
        int col = getCol(position);
        return contentData.get(row).get(col);
    }

    @Override
    public int getItemCount() {
        return getRowCount() * getColCount();
    }

}
