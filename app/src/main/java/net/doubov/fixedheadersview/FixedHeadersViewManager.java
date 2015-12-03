package net.doubov.fixedheadersview;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class FixedHeadersViewManager<TopType, SideType, ContentType> {

    public static final String TAG = FixedHeadersViewManager.class.getSimpleName();
    public static final boolean DEBUG = false;

    private Context context;
    private RelativeLayout container;

    private FrameLayout blankView;
    private RecyclerView topView;
    private RecyclerView sideView;
    private RecyclerView contentView;

    private BaseContentAdapter<ContentType, ? extends RecyclerView.ViewHolder> contentAdapter;
    private FixedHeadersAdapter<TopType, ? extends RecyclerView.ViewHolder> topAdapter;
    private FixedHeadersAdapter<SideType, ? extends RecyclerView.ViewHolder> sideAdapter;

    private FixedHeadersViewManager(Builder<TopType, SideType, ContentType> builder) {
        this.context = builder.context;
        this.container = builder.container;
        this.topAdapter = builder.topAdapter;
        this.sideAdapter = builder.sideAdapter;
        this.contentAdapter = builder.contentAdapter;

        blankView = setupTableSpace();
        topView = setupTopHeaderView();
        sideView = setupSideHeader();
        contentView = setupContentView();
        addViewsToContainer(container);

        topView.setAdapter(topAdapter);
        sideView.setAdapter(sideAdapter);
        contentView.setAdapter(contentAdapter);
    }

    private void setColumnCount(int columnCount) {
        FixedGridLayoutManager gridLayoutManager = (FixedGridLayoutManager) contentView.getLayoutManager();
        gridLayoutManager.setTotalColumnCount(columnCount);
    }

    private void addViewsToContainer(RelativeLayout container) {
        container.addView(blankView);
        container.addView(topView);
        container.addView(sideView);
        container.addView(contentView);
    }

    public void setContainer(RelativeLayout container) {
        if (this.container.getChildCount() > 0) {
            this.container.removeAllViews();
        }
        this.container = container;
        addViewsToContainer(container);
    }

    public void setData(TopType[] topData, SideType[] sideData, ContentType[][] contentData) {
        topAdapter.setData(topData, false);
        sideAdapter.setData(sideData, false);
        contentAdapter.setData(contentData, false);

        // now that we have data, set the # of columns
        setColumnCount(contentAdapter.getColCount());

        setupSpaceLayoutParams();
        setupContentViewLayoutParams();
        setupSideHeaderLayoutParams();
        setupTopHeaderLayoutParams();

        topAdapter.notifyDataSetChanged();
        sideAdapter.notifyDataSetChanged();
        contentAdapter.notifyDataSetChanged();
    }

    RecyclerView.OnScrollListener mScrollListener =
            new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    int id = recyclerView.getId();
                    switch (id) {
                        case R.id.tableTopHeader:
                        case R.id.tableResultSetHeader:
//                            Log.d(TAG, "scrollBy: " + dx + "/" + dy);
                            // temporarily remove the scroll listener to prevent recursive calls to onScrolled
                            contentView.removeOnScrollListener(this);
                            contentView.scrollBy(dx, dy);
                            contentView.addOnScrollListener(this);
                            break;
                        case R.id.tableContent:
//                            Log.d(TAG, "scrollBy: " + dx + "/" + dy);
                            // temporarily remove the scroll listener to prevent recursive calls to onScrolled
                            sideView.removeOnScrollListener(this);
                            topView.removeOnScrollListener(this);

                            sideView.scrollBy(dx, dy);
                            topView.scrollBy(dx, dy);

                            sideView.addOnScrollListener(this);
                            topView.addOnScrollListener(this);
                            break;
                    }
                }
            };

    private void setupTopHeaderLayoutParams() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) topView.getLayoutParams();

        params.height = (int) context.getResources()
                .getDimension(R.dimen.table_cell_height);
        params.width = (int) context.getResources()
                .getDimension(R.dimen.table_cell_width) * contentAdapter.getItemCount();

    }

    private RecyclerView setupTopHeaderView() {
        // Dates Header RV
        RecyclerView rv = new RecyclerView(context);
        rv.setId(R.id.tableTopHeader);
        rv.setTag(R.id.tableTopHeader);
        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );

        params.addRule(RelativeLayout.RIGHT_OF, R.id.tableSpace);

        rv.setLayoutParams(params);

        LinearLayoutManager topManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        rv.setLayoutManager(topManager);

        rv.addOnScrollListener(mScrollListener);
        return rv;
    }

    private FrameLayout setupTableSpace() {
        // Empty space layout
        FrameLayout space = new FrameLayout(context);
        space.setId(R.id.tableSpace);
        if (Build.VERSION.SDK_INT < 21) {
            space.setBackground(context.getResources().getDrawable(R.drawable.new_table_header_background));
        } else {
            space.setBackground(context.getDrawable(R.drawable.new_table_header_background));
        }

        RelativeLayout.LayoutParams spaceParams =
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
        space.setLayoutParams(spaceParams);
        return space;
    }

    private void setupSpaceLayoutParams() {
        RelativeLayout.LayoutParams spaceParams = (RelativeLayout.LayoutParams) blankView.getLayoutParams();

        spaceParams.width = (int) context.getResources()
                .getDimension(R.dimen.table_cell_width);
        spaceParams.height = (int) context.getResources()
                .getDimension(R.dimen.table_cell_height);
    }

    private void setupSideHeaderLayoutParams() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) sideView.getLayoutParams();

        params.width = (int) context.getResources()
                .getDimension(R.dimen.table_cell_width);
        params.height = (int) context.getResources()
                .getDimension(R.dimen.table_cell_height) * contentAdapter.getRowCount();
    }

    private RecyclerView setupSideHeader() {
        // ResultSet Header RV
        RecyclerView rv = new RecyclerView(context);
        rv.addOnScrollListener(mScrollListener);

        rv.setId(R.id.tableResultSetHeader);
        rv.setTag(R.id.tableResultSetHeader);

        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
        params.addRule(RelativeLayout.BELOW, R.id.tableSpace);

        rv.setLayoutParams(params);

        RecyclerView.LayoutManager rsManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(rsManager);
        return rv;
    }

    private void setupContentViewLayoutParams() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) contentView.getLayoutParams();

        params.height = (int) context.getResources()
                .getDimension(R.dimen.table_cell_height) * contentAdapter.getRowCount();

        params.width = (int) context.getResources()
                .getDimension(R.dimen.table_cell_width) * contentAdapter.getColCount();
    }

    private RecyclerView setupContentView() {
        // content RV
        RecyclerView rv = new SnapRecyclerView(context);
        rv.addOnScrollListener(mScrollListener);

        rv.setId(R.id.tableContent);
        rv.setTag(R.id.tableContent);

        RelativeLayout.LayoutParams contentParams =
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );

        contentParams.addRule(RelativeLayout.RIGHT_OF, R.id.tableSpace);
        contentParams.addRule(RelativeLayout.BELOW, R.id.tableSpace);
        rv.setLayoutParams(contentParams);

        FixedGridLayoutManager fixedGridLayoutManager = new FixedGridLayoutManager(context);
        rv.setLayoutManager(fixedGridLayoutManager);
        return rv;
    }

    public static class Builder<T, S, C> {

        private Context context;
        private RelativeLayout container;
        private BaseContentAdapter<C, ? extends RecyclerView.ViewHolder> contentAdapter;
        private FixedHeadersAdapter<T, ? extends RecyclerView.ViewHolder> topAdapter;
        private FixedHeadersAdapter<S, ? extends RecyclerView.ViewHolder> sideAdapter;

        public Builder<T, S, C> context(Context context) {
            this.context = context;
            return this;
        }

        public Builder<T, S, C> container(RelativeLayout container) {
            this.container = container;
            return this;
        }

        public <VH extends RecyclerView.ViewHolder> Builder<T, S, C> contentAdapter(BaseContentAdapter<C, VH> contentAdapter) {
            this.contentAdapter = contentAdapter;
            return this;
        }

        public <VH extends RecyclerView.ViewHolder> Builder<T, S, C> topAdapter(FixedHeadersAdapter<T, VH> topAdapter) {
            this.topAdapter = topAdapter;
            return this;
        }

        public <VH extends RecyclerView.ViewHolder> Builder<T, S, C> sideAdapter(FixedHeadersAdapter<S, VH> sideAdapter) {
            this.sideAdapter = sideAdapter;
            return this;
        }

        public FixedHeadersViewManager<T, S, C> build() {
            if (this.context == null
                || this.container == null
                || this.contentAdapter == null
                || this.topAdapter == null
                || this.sideAdapter == null) {
                throw new IllegalStateException("Make sure to set all the builder parameters");
            }

            return new FixedHeadersViewManager<>(this);
        }
    }

}
