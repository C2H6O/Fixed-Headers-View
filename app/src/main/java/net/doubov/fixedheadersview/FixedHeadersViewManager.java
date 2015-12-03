package net.doubov.fixedheadersview;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.util.List;

public class FixedHeadersViewManager<TopType, SideType, ContentType> {

    public static final String TAG = FixedHeadersViewManager.class.getSimpleName();
    public static final boolean DEBUG = false;

    private Context context;
    private RelativeLayout container;

    private FrameLayout mSpace;
    private RecyclerView topView;
    private RecyclerView sideView;
    private RecyclerView contentView;

    private BaseContentAdapter<ContentType, ? extends RecyclerView.ViewHolder> contentAdapter;
    private FixedHeadersAdapter<TopType, ? extends RecyclerView.ViewHolder> topAdapter;
    private FixedHeadersAdapter<SideType, ? extends RecyclerView.ViewHolder> sideAdapter;

//    private TableCellClickedListener mListener;

    private FixedHeadersViewManager(Builder<TopType, SideType, ContentType> builder) {
        this.context = builder.context;
        this.container = builder.container;
        this.topAdapter = builder.topAdapter;
        this.sideAdapter = builder.sideAdapter;
        this.contentAdapter = builder.contentAdapter;
    }

    private void setup() {
        mSpace = setupTableSpace();
        topView = setupTopHeaderView();
        sideView = setupSideHeader();
        contentView = setupContentView();
        addViewsToContainer(container);
//        setupAdapters();

        FixedGridLayoutManager gridLayoutManager = (FixedGridLayoutManager) contentView.getLayoutManager();
        gridLayoutManager.setTotalColumnCount(contentAdapter.getColCount());
    }

    private void addViewsToContainer(RelativeLayout container) {
        container.addView(mSpace);
        container.addView(topView);
        container.addView(sideView);
        container.addView(contentView);
    }

    public RelativeLayout getContainer() {
        return container;
    }

    public void setContainer(RelativeLayout container) {
        if (this.container.getChildCount() > 0) {
            this.container.removeAllViews();
        }
        this.container = container;
        addViewsToContainer(container);
    }

    public void setContentData(List<List<ContentType>> contentData) {
        contentAdapter.setData(contentData);
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

            FixedHeadersViewManager<T, S, C> manager = new FixedHeadersViewManager<>(this);
            manager.setup();

            return manager;
        }

    }


//    public boolean hasData() {
//        return sideData != null && sideData.size() > 0;
//    }
//
//    public void setData(List<S> resultSets) {
////        if (DEBUG) {
////            Log.d(TAG, "setData: " + resultSets);
////        }
//        sideData.clear();
//        topData.clear();
//        contentData.clear();
//
//        if (resultSets.size() > 0) {
//            sideData.addAll(resultSets);
//            topData.addAll(generateDatesHeaderList(sideData));
//
//            Result[][] resultsMatrix = generateContentData(sideData, topData, isTransposed);
//
//            for (Result[] row : resultsMatrix) {
//                contentData.add(Arrays.asList(row));
//            }
//        }
//
//        // Adjust the LayoutParameters of Views to the size of the new data;
//        setupSideHeaderLayoutParams(isTransposed);
//        setupTopHeaderLayoutParams(isTransposed);
//        setupContentViewLayoutParams();
//        setupSpaceLayoutParams();
//
//        topView.getAdapter().notifyDataSetChanged();
//        sideView.getAdapter().notifyDataSetChanged();
//
//        BaseContentAdapter contentAdapter = (BaseContentAdapter) contentView.getAdapter();
//        contentAdapter.onDataChanged();
//    }

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
//
//    public void setupAdapters() {
//        if (DEBUG) {
//            Log.d(TAG, "setupAdapters");
//        }
//        DatesHeaderAdapter datesAdapter = new DatesHeaderAdapter(vhDateUtil, dateTypeFormat);
//        datesAdapter.setData(topData);
//        topView.setAdapter(datesAdapter);
//
//
//        ResultSetsHeaderAdapter rsAdapter = new ResultSetsHeaderAdapter();
//        rsAdapter.setData(sideData);
//        sideView.setAdapter(rsAdapter);
//
//        BaseContentAdapter contentAdapter = new BaseContentAdapter(context);
//        contentAdapter.setData(contentData);
//        contentAdapter.setListener(this);
//        contentView.setAdapter(contentAdapter);
//    }

    private void setupTopHeaderLayoutParams(boolean isTransposed) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) topView.getLayoutParams();

        params.height = (int) context.getResources().getDimension(R.dimen.side_header_height);
        params.width = (int) context.getResources()
                        .getDimension(R.dimen.side_header_width) * contentAdapter.getItemCount();

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

        LinearLayoutManager datesLM = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        rv.setLayoutManager(datesLM);

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
        RelativeLayout.LayoutParams spaceParams = (RelativeLayout.LayoutParams) mSpace.getLayoutParams();

        spaceParams.width = (int) context.getResources().getDimension(R.dimen.side_header_width);
        spaceParams.height = (int) context.getResources().getDimension(R.dimen.side_header_height);
    }

    private void setupSideHeaderLayoutParams() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) sideView.getLayoutParams();

        params.width = (int) context.getResources().getDimension(R.dimen.side_header_width);
        params.height = (int) context.getResources()
                        .getDimension(R.dimen.side_header_height) * contentAdapter.getRowCount();
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
                        .getDimension(R.dimen.side_header_height) * contentAdapter.getRowCount();

        params.width = (int) context.getResources()
                        .getDimension(R.dimen.side_header_width) * contentAdapter.getColCount();


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

}
